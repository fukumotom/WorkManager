<%@page import="test.test.ojt.model.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
	User user = (User) session.getAttribute("registUser");
%>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザ登録確認画面</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/app/css/styles.css">
</head>
<body>
	<table>
		<tr>
			<td>ユーザ名：</td>
			<td><%=user.getUserName()%></td>
		</tr>
		<tr>
			<td><a href="/WorkManager/RegisterForm"><input type="button"
					value="戻る"></a></td>
			<td><a href="/WorkManager/RegisterForm?action=confirm"><input
					type="button" value="登録"></a></td>
		</tr>
	</table>
</body>
</html>