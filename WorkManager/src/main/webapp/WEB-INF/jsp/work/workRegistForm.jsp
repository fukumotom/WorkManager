<%@page import="jp.kigami.ojt.common.util.DateUtils"%>
<%@page import="jp.kigami.ojt.model.Work"%>
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
	<%
		String state = (String) request.getAttribute("state");
		String stateStr = "";
		if ("working".equals(state)) {
			stateStr = "作業中";

			Object obj = request.getAttribute("working");
			Work work;
			if (obj instanceof Work) {
				work = (Work) obj;
				request.setAttribute("work", work);
			}

		} else {
			stateStr = "未作業";
		}
		request.setAttribute("stateStr", stateStr);
	%>
	<h2>現在の状況:${stateStr}</h2>
	<%
		if ("作業中".equals(stateStr)) {
	%>
	<table>
		<tr>
			<th>開始時間</th>
		</tr>
		<tr>
			<td>${work.startTime}</td>
		</tr>
	</table>
	<br />
	<table>
		<tr>
			<th>作業内容</th>
			<th>備考</th>
		</tr>
		<tr>
			<td>${work.contents}</td>
			<td>${work.note}</td>
		</tr>
	</table>
	<form method="post" action="/WorkManager/WorkRegister">
		<input type="hidden" name="id" value="${work.id}"> <input
			type="submit" name="action" value="作業終了">
	</form>
	<%
		}
		String now = DateUtils.getNowTimeStr();
		request.setAttribute("now", now);
	%>
	<form method="post" action="/WorkManager/WorkRegister">
		<table>
			<tr>
				<th>開始時間</th>
			</tr>
			<tr>
				<td><input type="text" name="startTime" value="${now}"></td>
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
		<input type="hidden" name="state" value="${stateStr}"> <input
			type="hidden" name="id" value="${work.id}"><input
			type="submit" name="action" value="作業開始">
	</form>
</body>
</html>