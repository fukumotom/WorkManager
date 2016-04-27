<%@page import="test.test.ojt.model.Work"%>
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
			<th>備考 ${work.id}</th>
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
</body>
</html>