<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" >
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/app/css/styles.css" >
		<title>ユーザ登録フォーム</title>
	</head>
	<body>
	<h1>スタイルお試し</h1>
	<%
		String errMsg = (String)request.getAttribute("syze_user");
		String errMsg2 = (String)request.getAttribute("syze_pass");
		String errMsg3 = (String)request.getAttribute("confirm_pass");
	%>
		<form method="post" action="/WorkManager/RegisterForm" >
			<table>
				<tr>
					<td>ユーザ名：</td><td><input type="text" name="userName"></td>
					<% if(errMsg != null){ %>
						<td id="errMsgStyle"><%=errMsg %></td>
					<% } %>
				</tr>
				
				<tr>
					<td>パスワード：</td><td><input type="password" name="password"></td>
					<% if(errMsg2 != null){ %>
						<td id="errMsgStyle"><%=errMsg2 %></td>
					<% } else if(errMsg3 != null){ %>
						<td id="errMsgStyle"><%=errMsg3 %></td>
					<% } %>
				</tr>
				<tr>
					<td>パスワード確認：</td><td><input type="password" name="passwordConfirm"></td>
				</tr>
				<tr>
					<td><a href="/WorkManager/Menu"><input type="button" value="戻る"></a></td><td><input type="submit" name="確認"></td><td />
				</tr>
			</table>
		</form>
	</body>
</html>