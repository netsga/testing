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
	String SCOPE = "email";
	String APPROVAL_PROMPT = "force";
	String ACCESS_TYPE = "online";
	
	String GOOGLE_VALIDATION_URL = "https://www.googleapis.com/oauth2/v3/tokeninfo";
	String HEMS_USER_URL = "http://localhost:8080/users";
	
	String session_user_id = (String)session.getAttribute("session_user_id");
	String session_user_token = (String)session.getAttribute("session_user_token");

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
</script>
</head>
<body>

	<div style="height: 50px;"></div>
	<div class="content" align="center">

<%
	//Session Check
	if (session_user_token == null || session_user_token.equals("")) {
%>
		session_user_id: <%=session_user_id%> <br>
		session_user_token: <%=session_user_token%> <br>
		
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
			//Token이 유효하면 HEMS DB 조회
			String callHemsUserUrl = HEMS_USER_URL + "/" + user_id + ".anything";
						
			resultValid = callAPI(callHemsUserUrl);
			
			//HEMS DB에 없는 사용자면 등록 프로세스
			if (resultValid == null || resultValid.equals("")) {
				response.sendRedirect("/join?user_id="+user_id+"&access_token="+session_user_token+"&account_type=google");
			}
			else {
				jsonObject = (JSONObject)jsonParser.parse(resultValid);
				
				user_id = (String)jsonObject.get("userId");
				account_type = (String)jsonObject.get("accountType");
				access_token = (String)jsonObject.get("accessToken");
			}
		}
%>
		<p style="padding-bottom: 5px;">
			<h2>LG HEMS Main</h2>
		</p>
		
		<p style="padding-bottom: 5px;">
			user_id: <%=user_id%> <br>
			account_type: <%=account_type%> <br>
			access_token: <%=access_token%> <br>
		</p>
		
		<p style="padding-bottom: 5px;">
			<input type="button" class="btn" width="80px" onclick="submitLogout();" value="Logout">
		</p>
<%
	}
%>
		
	</div>
</body>
</html>