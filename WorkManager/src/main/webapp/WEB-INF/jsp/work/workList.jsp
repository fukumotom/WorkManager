<?xml version="1.0" encoding="UTF-8"?>
<%@page import="test.test.ojt.model.Work"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/app/css/styles.css">
<title>作業リスト画面</title>
</head>
<%
	Object obj = request.getAttribute("workList");
	ArrayList<Work> workList = new ArrayList<Work>();

	if (obj instanceof ArrayList<?>) {
		ArrayList<?> list = (ArrayList<?>) obj;
		for (Object o : list) {
			if (o instanceof Work) {
				workList.add((Work) o);
			}
		}
	}
	String errMsg = (String) request.getAttribute("errMsg");
	String listDate = (String) request.getAttribute("listDate");
%>

<body>
	<jsp:include page="/WEB-INF/jsp/header.jsp" />
	<%
		if (errMsg != null) {
	%>
	<h2 id=errMsg>${errMsg}</h2>
	<%
		}
	%>
	<H1>${listDate}の作業リスト</H1>
	<form method="post" action="/WorkManager/WorkList">
		<table>
			<tr>
				<th></th>
				<th>開始時間</th>
				<th>終了時間</th>
				<th>作業時間</th>
				<th>作業内容</th>
				<th>備考</th>
			</tr>
			<%
				for (int index = 0; index < workList.size(); index++) {
					Work work = workList.get(index);
					request.setAttribute("work", work);
			%>
			<tr>
				<td>
					<%
						if (index == 0) {
					%> <input type="radio" name="id" value="${work.id}"
					checked="checked">
					<%
						} else {
					%> <input type="radio" name="id" value="${work.id}">
					<%
						}
					%>
				</td>
				<td>${work.startTime}</td>
				<td>${work.endTime}</td>
				<td>${work.workingTime}</td>
				<td>${work.contents}</td>
				<td>${work.note}</td>
			</tr>
			<%
				}
			%>
		</table>
		<table>
			<tr>
				<td><input type="submit" value="挿入" name="insertBtn"></td>
				<td><input type="submit" value="追加" name="addBtn"></td>
				<td><input type="submit" value="削除" name="deleteBtn"></td>
				<td><input type="submit" value="保存" name="saveBtn"></td>
				<td><input type="checkbox" value="1" name="deleteFlg">
						削除を含む <input type="text" name="workDate"> (yyyy/MM/dd) <input
					type="submit" value="履歴" name="historyBtn"></td>
			</tr>
		</table>

	</form>
</body>
</html>
