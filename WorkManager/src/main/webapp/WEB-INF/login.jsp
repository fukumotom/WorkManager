<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
	<head>
		<meta charset="UTF-8" >
		<title>ログイン画面</title>
	</head>
	<body>
		
		<% if("true".equals(request.getParameter("error"))) {%><p>ID または PW が違います。</pli><%} %>
		<h2>ユーザ情報を入力してください。</h2>
		<form method="post" action="j_security_check">
			<table>
				<tr>
					<td>ユーザ名：</td><td><input type="text" name="j_username"></td>
				</tr>
				<tr>
					<td>パスワード：</td><td><input type="password" name="j_password"></td>
				</tr>
	
				<tr>
					<td></td><td><input type="submit" name="ログイン"></td>
				</tr>
			</table>
		</form>
		<a href="/WorkManager/RegisterForm">
			<input type="button" value="ユーザ新規登録">
		</a>
    </body>
</html>