<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<%
  String name = request.getUserPrincipal().getName();
%>
<html>
	<head>
		<meta charset="UTF-8" >
		<title>メニュー画面</title>
	</head>
	<body>
		<h1>メニュー</h1>
		<p>ようこそ！ <%=name %> さん</p>
		
		<form method="get" action="/WorkManager/Logout">
			<input type="submit" value="ログアウト">
		</form>
		
		<form method="get" action="/WorkManager/Logout">
			<input type="submit" value="作業登録/終了">
		</form>
		
		<form method="get" action="/WorkManager/Logout">
			<input type="submit" value="作業リスト">
		</form>
	</body>
</html>