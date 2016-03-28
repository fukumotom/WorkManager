<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" >
		<title>ユーザ登録フォーム</title>
	</head>
	<body>
		<form method="post" action="/WorkManager/RegisterForm">
			<table>
				<tr>
					<td>ユーザ名：</td><td><input type="text" name="userName"></td>
				</tr>
				<tr>
					<td>パスワード：</td><td><input type="password" name="password"></td>
				</tr>
				<tr>
					<td>パスワード確認：</td><td><input type="password" name="passwordConfirm"></td>
				</tr>
				<tr>
					<td><a href="/WorkManager/Menu"><input type="button" value="戻る"></a></td><td><input type="submit" name="確認"></td>
				</tr>
			</table>
		</form>
	</body>
</html>