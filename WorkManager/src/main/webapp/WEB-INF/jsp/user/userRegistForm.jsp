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
	<%
		String errMsg = (String) request.getAttribute("syze_user");
		String errMsg2 = (String) request.getAttribute("syze_pass");
		String errMsg3 = (String) request.getAttribute("confirm_pass");
	%>
	<form method="post" action="/WorkManager/RegisterForm">
		<table>
			<tr>
				<td><label for="j_username" class="userForm">ユーザ名：</label></td>
				<td><input type="text" name="j_username" id="j_username"
					style="ime-mode: disabled" maxlength="20" class="loginForm" /></td>
				<%
					if (errMsg != null) {
				%>
				<td id="errMsgStyle"><%=errMsg%></td>
				<%
					}
				%>
			</tr>

			<tr>
				<td><label for="password" class="userForm">パスワード：</label></td>
				<td><input type="password" name="password" id="password"
					class="userForm" /></td>
				<%
					if (errMsg2 != null) {
				%>
				<td id="errMsgStyle"><%=errMsg2%></td>
				<%
					} else if (errMsg3 != null) {
				%>
				<td id="errMsgStyle"><%=errMsg3%></td>
				<%
					}
				%>
			</tr>
			<tr>
				<td><label for="passwordConfirm" class="userForm">パスワード確認：</label></td>
				<td><input type="password" name="passwordConfirm"
					id="passwordConfirm" class="userForm" /></td>
			</tr>
			<tr>
				<td><div align="center">
						<input type="submit" name="actionBtn" value="戻る" />
					</div></td>
				<td><div align="center">
						<input type="submit" name="actionBtn" value="確認" />
					</div></td>
			</tr>
		</table>
	</form>
</body>
</html>