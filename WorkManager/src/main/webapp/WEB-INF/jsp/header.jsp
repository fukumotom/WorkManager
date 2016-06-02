<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/app/css/styles.css">
</head>
<table class="centerTable">
	<tr>
		<td align="left">
			<form method="get" action="/WorkManager/Menu">
				<input type="submit" value="メニュー">
			</form>
		</td>
		<td align="right">
			<form method="get" action="/WorkManager/Logout">
				<input type="submit" value="ログアウト">
			</form>
		</td>
	</tr>
</table>
