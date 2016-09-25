<%@page import="jp.co.alpha.kgmwmr.model.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザ登録確認画面</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/app/css/styles.css">
</head>
<body>
	<form method="get" action="/WorkManager/RegisterForm?action=confirm">
		<table>
			<tr>
				<td>ユーザ名：</td>
				<td>${form.userName}</td>
			</tr>
			<tr>
				<td><input type="submit" name="returnBtn" value="戻る" /></td>
				<td><input type="submit" name="registBtn" value="登録" /></td>
			</tr>
		</table>
	</form>
</body>
</html>