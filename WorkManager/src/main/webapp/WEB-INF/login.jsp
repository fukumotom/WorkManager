<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/app/css/styles.css">
<title>ログイン画面</title>
</head>
<body>
	<%
		if ("true".equals(request.getParameter("error"))) {
	%><h2 id="errMsg">ID または PW が違います。</h2>
	<%
		}
	%>
	<h2>ユーザ情報を入力してください。</h2>
	<form method="post" action="j_security_check">
		<table>

			<tr>
				<td><label for="j_username" class="loginForm">ユーザ名：</label></td>
				<td><input type="text" name="j_username" id="j_username"
					style="ime-mode: disabled" maxlength="20" class="loginForm" /></td>
			</tr>
			<tr>
				<td><label for="j_password" class="loginForm">パスワード：</label></td>
				<td><input type="password" name="j_password" id="j_password"
					maxlength="20" class="loginForm" /></td>
			</tr>

			<tr>
				<td colspan="2"><div align="center">
						<input type="submit" value="ログイン" />
					</div></td>
			</tr>
		</table>
	</form>
	<form method="get" action="/WorkManager/RegisterForm">
		<input type="submit" name="userRegit" value="ユーザ新規登録" />
	</form>
</body>
</html>