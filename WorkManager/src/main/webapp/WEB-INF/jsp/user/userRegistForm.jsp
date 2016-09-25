<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/app/css/styles.css">
<title>ユーザ登録フォーム</title>
</head>
<body>
	<h1>
		<label>ユーザ新規登録フォーム</label>
	</h1>
	<h2 id="errMsg">${form.errMsgs}</h2>
	<form method="post" action="/WorkManager/RegisterForm">
		<table>
			<tr>
				<td><label for="j_username" class="userForm">ユーザ名：</label></td>
				<td><input type="text" name="j_username"
					style="ime-mode: disabled" maxlength="20" class="loginForm" /></td>
			</tr>
			<tr>
				<td><label for="password" class="userForm">パスワード：</label></td>
				<td><input type="password" name="password" class="userForm" /></td>
			</tr>
			<tr>
				<td><label for="passwordConfirm" class="userForm">パスワード確認：</label></td>
				<td><input type="password" name="confirmPassword"
					class="userForm" /></td>
			</tr>
			<tr>
				<td><div align="center">
						<input type="submit" name="returnBtn" value="戻る" />
					</div></td>
				<td><div align="center">
						<input type="submit" name="confilmBtn" value="確認" />
					</div></td>
			</tr>
		</table>
	</form>
</body>
</html>