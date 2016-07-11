<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "java.util.*" %> 
<%@ page import = "java.text.*" %>
<%@ page import = "java.io.*" %>
<%@ page import = "java.net.*" %>
<%@ page import = "org.json.simple.*" %>
<%@ page import = "org.json.simple.parser.*" %>
<%@ page import="java.sql.*" %>

<html>
<head>
	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1, maximum-scale=1">
	<meta name="format-detection" content="telephone=no, address=no, email=no">
	<title>Test </title>
	<link rel="stylesheet" href="css.css">
	
<% 
	String SERVER_URL	 = "https://accounts.google.com/o/oauth2/token";
	String CLIENT_ID	 = "505972002650-kugtircr83k68jm4as5dhsbo832am5en.apps.googleusercontent.com";
	String CLIENT_SECRET = "pg4XYYFIVckPDhS6SOdd4jxb";
	String REDIRECT_URI  = "http://localhost:8080/success";
	
	String str_buffer;
	String json_result;
	String access_token;
	String refresh_token;
	String scope;
	
	//Authorization
	String auth_code = request.getParameter("code");
		
	//HTTP POST
	URL AccessTokenURL = new URL(SERVER_URL);
	HttpURLConnection http = (HttpURLConnection)AccessTokenURL.openConnection();

	http.setDoOutput(true);
	http.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
		
	//AccessTokenURL query string
	StringBuffer buffer = new StringBuffer();
	buffer.append("grant_type=authorization_code&code="+auth_code+"&client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET+"&redirect_uri="+REDIRECT_URI);
		
	
	OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
	PrintWriter writer = new PrintWriter(outStream);
	writer.write(buffer.toString());
	writer.flush();
		
	InputStreamReader inputStream = new InputStreamReader(http.getInputStream(), "UTF-8");
	BufferedReader bufferReader = new BufferedReader(inputStream);
	StringBuilder builder = new StringBuilder();
	
	while((str_buffer = bufferReader.readLine()) != null){
		builder.append(str_buffer+"\n");
	}
		
	json_result = builder.toString();
		
	JSONParser jsonParser = new JSONParser();
	JSONObject jsonObject = (JSONObject)jsonParser.parse(json_result);
	 
	
	session.setAttribute("session_user_token", (String)jsonObject.get("access_token"));
	
	response.sendRedirect("/index");
%>

</head>

<body oncontextmenu="return false" class="background-image">
<div style="height:50px;"></div>

<div class="content">
	<p style="height:20px;"></p>
	<p style="padding-bottom:10px;">
	json_result: <%=json_result%> <br>
	</p>
</div>
<br><br>
</body>
</html>