<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1, maximum-scale=1">
<meta name="format-detection" content="telephone=no, address=no, email=no">
<title>Test Google OAuth</title>

<% 
	String HEMS_USER_URL = "http://localhost:8080/users";
	String success = request.getParameter("success");
	
	if (success != null && success.equals("true")){
		out.println("<script>alert('Success create user');</script>");
		session.setAttribute("session_user_id", request.getParameter("user_id"));
		session.setAttribute("session_user_token", request.getParameter("access_token"));
		response.sendRedirect("/index");
	}
%>

<script>
	function submitRequest()
	{
		var userInfo = {
			userId: document.joinForm.userId.value,
			accessToken: "<%=request.getParameter("access_token")%>",
			accountType: "<%=request.getParameter("account_type")%>",
			givenName: document.joinForm.givenName.value,
			familyName: document.joinForm.familyName.value,
			email: document.joinForm.email.value,
			emSN: document.joinForm.emSN.value,
			emPassword: document.joinForm.emPassword.value
		};
		var jsonstr = JSON.stringify(userInfo);
		
		var xmlhttp = new XMLHttpRequest();
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4) {
				if (xmlhttp.status == 200) {
					window.location.href="/join?success=true&user_id="+userInfo.userId+"&access_token="+userInfo.accessToken;
				} else {
					var err = JSON.parse(xmlhttp.responseText);
					alert(err.message);
				}
			}
	    }
		
	    xmlhttp.open("POST", "<%=HEMS_USER_URL%>", true);
	    xmlhttp.setRequestHeader("Content-Type", "application/json");
	    xmlhttp.send(jsonstr);
	}
	
</script>
</head>
<body>
	<div style="height: 50px;"></div>
	<div class="content" align="center">
	
	<p style="padding-bottom: 5px;">
		<h2>LG HEMS Join</h2>
	</p>
	<p style="padding-bottom: 5px;">
		<form name="joinForm" method="post" action=" ">
		<table border="0"> 
			<tr>
				<td align="right">User ID</td>
				<td width="10"></td>
				<td colspan="4"><input type="text" id="userId" style="width:200px" maxlength="50" value="<%=request.getParameter("user_id")%>" disabled></td>
			</tr>
			<tr>
				<td height="10"></td>
			</tr>
			<tr>
				<td align="right">GivenName</td>
				<td width="10"></td>
				<td colspan="4"><input type="text" id="givenName" style="width:200px" maxlength="50" value=""></td>
			</tr>
			<tr>
				<td align="right">FamilyName</td>
				<td width="10"></td>
				<td colspan="4"><input type="text" id="familyName" style="width:200px" maxlength="50" value=""></td>
			</tr>
			<tr>
				<td align="right">Email</td>
				<td width="10"></td>
				<td colspan="4"><input type="text" id="email" style="width:200px" maxlength="50" value="<%=request.getParameter("user_id")%>"></td>
			</tr>
			<tr>
				<td height="10"></td>
			</tr>
			<tr>
				<td align="right">EM Serial no.</td>
				<td width="10"></td>
				<td colspan="4"><input type="text" id="emSN" style="width:200px" maxlength="50" value=""></td>
			</tr>
			<tr>
				<td align="right">EM Password</td>
				<td width="10"></td>
				<td colspan="4"><input type="text" id="emPassword" style="width:200px" maxlength="50" value=""></td>
			</tr>
		</table>
		</form>
	</p>
	<p style="padding-bottom:5px;">
		<input type="button" class="btn" width="80px" onclick="submitRequest();" value="Join HEMS Member">
	</p>
	
	</div>
</body>
</html>