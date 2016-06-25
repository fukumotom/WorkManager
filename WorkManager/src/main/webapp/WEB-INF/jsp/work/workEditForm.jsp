<%@page import="jp.co.alpha.kgmwmr.common.util.DateUtils"%>
<%@page import="jp.co.alpha.kgmwmr.model.Work"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/style.css">
<title>作業編集フォーム</title>
</head>
<body>
	<jsp:include page="/WEB-INF/jsp/header.jsp" />
	<%
		Object obj = request.getAttribute("editWork");
		Work editWork;
		if (obj instanceof Work) {
			editWork = (Work) obj;
			request.setAttribute("editWork", editWork);
		}
	%>
	<form method="post" action="/WorkManager/WorkEdit">
		<table>
			<tr>
				<th>開始時間</th>
			</tr>
			<tr>
				<td><input type="text" name="startTime"
					value="${editWork.startTime}" /></td>
			</tr>
			<tr>
				<th>終了時間</th>
			</tr>
			<tr>
				<td><input type="text" name="endTime"
					value="${editWork.endTime}" /></td>
			</tr>
			<tr>
				<th>作業内容</th>
			</tr>
			<tr>
				<td><input type="text" name="contents"
					value="${editWork.contents}" /></td>
			</tr>
			<tr>
				<th>備考</th>
			</tr>
			<tr>
				<td><input type="text" name="note" value="${editWork.note}" /></td>
			</tr>
		</table>
		<input type="hidden" name="id" value="${editWork.id}" /><input
			type="submit" value="更新" />
	</form>
</body>
</html>