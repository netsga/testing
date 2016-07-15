<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.net.*"%>
<%@ page import="javax.net.ssl.*"%>
<%@ page import="org.json.simple.*"%>
<%@ page import="org.json.simple.parser.*"%>
<%
	String SERVER_URL = "https://accounts.google.com/o/oauth2/auth";
	String CLIENT_ID = "505972002650-kugtircr83k68jm4as5dhsbo832am5en.apps.googleusercontent.com";
	String REDIRECT_URI = "http://localhost:8080/success";
	String SCOPE = "email profile";
	String APPROVAL_PROMPT = "force";
	String ACCESS_TYPE = "online";
	
	String GOOGLE_VALIDATION_URL = "https://www.googleapis.com/oauth2/v3/tokeninfo";
	String HEMS_USER_URL = "http://localhost:8080/users";
	
	String session_user_id = (String)session.getAttribute("session_user_id");
	String session_user_token = (String)session.getAttribute("session_user_token");

	String hems_id = null;
	String user_id = null;
	String account_type = null;
	String access_token = null;
%>
<%!
	private String callAPI(String apiUrl) {
		BufferedReader in = null;
		StringBuilder builder = new StringBuilder();
		
		try {
			URL url = new URL(apiUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("GET");
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			
			String str_buffer = "";
	
			while ((str_buffer = in.readLine()) != null) {
				builder.append(str_buffer + "\n");
			}			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		
		return builder.toString();
}
%>

<html>
<head>
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1, maximum-scale=1">
<meta name="format-detection" content="telephone=no, address=no, email=no">
<title>Test Google OAuth</title>
<script>
		function submitRequest()
		{
			document.location.href = '<%=SERVER_URL%>?client_id=<%=CLIENT_ID%>&redirect_uri=<%=REDIRECT_URI%>&scope=<%=SCOPE%>&approval_prompt=<%=APPROVAL_PROMPT%>&access_type=<%=ACCESS_TYPE%>&response_type=code';
		}
		
		function submitLogout()
		{
			document.location.href = '/logout';
		}
		
		function callAPI(api_type, hems_id, result_txt)
		{
			var api_url='http://localhost:8080/users/api/' + api_type + '/' + hems_id;
			var xmlhttp;
			
			document.getElementById(result_txt).innerHTML = 'loading...';
			
			if(window.XMLHttpRequest)
			{
				xmlhttp = new XMLHttpRequest();
			}
			else if(window.ActiveXObject)
			{  
				xmlhttp = new ActiveXObject("MSXML2.XMLHTTP");
			   
				if(!xmlhttp)
				{
					xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
				}
			}
			else
			{
				alert("Browser is not supported");
			}

			xmlhttp.onreadystatechange=function() {
				if (xmlhttp.readyState==4) 
				{
					if (xmlhttp.status==200)
					{
						var user_response = JSON.parse(xmlhttp.responseText);
						var inner_txt = '<b>guid:</b> ' + user_response.guid + '<br>'
									  + '<b>owner:</b> ' + user_response.owner + '<br>'
						 			  + '<b>channel:</b> ' + user_response.channel + '<br><br>';
						if (api_type == 'location') {
							inner_txt += '<b>PowerOutFromProducers:</b> ' + user_response.tagValues.PowerOutFromProducers.value + '<br>'
									   + '<b>PowerConsumedFromGrid:</b> ' + user_response.tagValues.PowerConsumedFromGrid.value + '<br>'
									   + '<b>PowerConsumedFromProducer:</b> ' + user_response.tagValues.PowerConsumedFromProducers.value + '<br>'
									   + '<b>WorkProduced:</b> ' + user_response.tagValues.WorkProduced.value + '<br>'
									   + '<b>StateDevice:</b> ' + user_response.tagValues.StateDevice.value + '<br>';
						} else if (api_type == 'smartplug') {
							inner_txt += '<b>PowerIn:</b> ' + user_response.tagValues.PowerIn.value + '<br>'
									  + '<b>IdName:</b> ' + user_response.tagValues.IdName.value + '<br>'
									  + '<b>StateDevice:</b> ' + user_response.tagValues.StateDevice.value + '<br>';
						}
						document.getElementById(result_txt).innerHTML = inner_txt;
					}
				}
			}
		
			xmlhttp.open("GET",api_url,true);
			xmlhttp.send();
		}
</script>
</head>
<body>

	<div style="height: 50px;"></div>
	<div class="content" align="center">

<%
	//Session Check
	if (session_user_token == null || session_user_token.equals("")) {
%>
<!-- 
		session_user_id: <%=session_user_id%> <br>
		session_user_token: <%=session_user_token%> <br>
 -->		
		<p style="padding-bottom: 5px;">
			<h2>LG HEMS LOGIN</h2>
		</p>
		
		<p style="padding-bottom: 5px;">
			<input type="button" class="btn" width="80px" onclick="submitRequest();" value="Google Login">
		</p>
<%		
	//세션 있으면 
	} else {
		//Google Token validation
		String callValidationUrl = GOOGLE_VALIDATION_URL + "?access_token=" + session_user_token;
		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = null;
		
		String resultValid = callAPI(callValidationUrl);
		jsonObject = (JSONObject)jsonParser.parse(resultValid);
		
		user_id = (String)jsonObject.get("email");

		//Token is not valid
		if (user_id == null) {
			session.invalidate();
			response.sendRedirect("/index");
		} else {
			//Token이 유효하면 user id로 HEMS DB 조회
			String callHemsUserUrl = HEMS_USER_URL + "/" + user_id + ".anything";

			resultValid = callAPI(callHemsUserUrl);
			
			//HEMS DB에 없는 사용자면 등록 프로세스
			if (resultValid == null || resultValid.equals("")) {
				response.sendRedirect("/join?user_id="+user_id+"&access_token="+session_user_token+"&account_type=google");
			}
			else {
				jsonObject = (JSONObject)jsonParser.parse(resultValid);
				
				hems_id = (String)jsonObject.get("hemsId");
				user_id = (String)jsonObject.get("userId");
				account_type = (String)jsonObject.get("accountType");
				access_token = (String)jsonObject.get("accessToken");
				
				//hems user id가 있지만 등록된 토큰이 다를 경우 DB에 token 정보 update
				if (!session_user_token.equals(access_token)) {
					String callUpdateTokenUrl = HEMS_USER_URL + "/" + hems_id + "/" + session_user_token + ".anything";
					resultValid = callAPI(callUpdateTokenUrl);
					
					response.sendRedirect("/index");
				}
			}
		}
%>
		<p style="padding-bottom: 5px;">
			<h2>LG HEMS Main</h2>
		</p>
		
		<p style="padding-bottom: 5px;">
			<table border="1" cellpadding="5" cellspacing="0">
				<tr>
					<td>hems_id</td>
					<td width="200"><%=hems_id%></td>
				</tr>
				<tr>
					<td>3rd party user_id</td>
					<td><%=user_id%></td>
				</tr>
				<tr>
					<td>3rd party account_type</td>
					<td><%=account_type%></td>
				</tr>
				<tr>
					<td>access_token</td>
					<td><%=access_token%></td>
				</tr>
			</table>
		</p>
		
		<p style="padding-bottom: 5px;" width="200">
			<table border="0" cellpadding="5" cellspacing="0">
				<tr>
					<td align="center"><input type="button" class="btn" width="80px" onclick="callAPI('location', '<%=hems_id%>', 'result_location');" value="Location API call"></td>
					<td align="center"><input type="button" class="btn" width="80px" onclick="callAPI('smartplug', '<%=hems_id%>', 'result_smartplug');" value="Smart plug API call"></td>
				</tr>
				<tr valign="top">
					<td width="400"><div id="result_location" align="left"></div></td>
					<td width="400"><div id="result_smartplug" align="left"></div></td>
				</tr>
			</table>
		</p>
		
		<p style="padding-bottom: 5px;" align="right">
			<input type="button" class="btn" width="80px" onclick="submitLogout();" value="Logout">
		</p>
<%
	}
%>
		
	</div>
</body>
</html>