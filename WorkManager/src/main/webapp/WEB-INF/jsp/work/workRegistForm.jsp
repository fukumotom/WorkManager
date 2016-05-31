<%@page import="jp.kigami.ojt.common.util.ConstantDef"%>
<%@page import="jp.kigami.ojt.form.WorkRegisterForm"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/style.css">
<title>作業登録フォーム</title>
</head>
<body>
	<jsp:include page="/WEB-INF/jsp/header.jsp" />

	<h2>現在の状況:${form.workingStates}</h2>
	<%
		WorkRegisterForm form = (WorkRegisterForm) request
				.getAttribute("form");
		if (ConstantDef.WOKING_STATE_WORKING
				.equals(form.getWorkingStates())) {
	%>
	<table>
		<tr>
			<th>開始時間</th>
		</tr>
		<tr>
			<td>${form.work.startTime}</td>
		</tr>
	</table>
	<br />
	<table>
		<tr>
			<th>作業内容</th>
			<th>備考</th>
		</tr>
		<tr>
			<td>${form.work.contents}</td>
			<td>${form.work.note}</td>
		</tr>
	</table>
	<form method="post" action="/WorkManager/WorkRegister">
		<input type="hidden" name="id" value="${work.id}"> <input
			type="submit" name="action" value="作業終了">
	</form>
	<%
		}
	%>
	<form method="post" action="/WorkManager/WorkRegister">
		<table>
			<tr>
				<th>開始時間</th>
			</tr>
			<tr>
				<td><input type="text" name="startTime" value="${form.nowTime}"></td>
			</tr>
		</table>
		<br />
		<table>
			<tr>
				<th>作業内容</th>
				<th>備考</th>
			</tr>
			<tr>
				<td><input type="text" name="contents"></td>
				<td><input type="text" name="note"></td>
			</tr>
		</table>
		<input type="hidden" name="workingStates"
			value="${form.workingStates}"><input type="submit"
			name="action" value="作業開始">
	</form>
</body>
</html>