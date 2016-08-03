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
	<form method="post" action="/WorkManager/WorkEdit">
		<table>
			<tr>
				<th>開始時間</th>
			</tr>
			<tr>
				<td><input type="text" name="startTime"
					value="${editForm.startTime}" /></td>
			</tr>
			<tr>
				<th>終了時間</th>
			</tr>
			<tr>
				<td><input type="text" name="endTime"
					value="${editForm.endTime}" /></td>
			</tr>
			<tr>
				<th>作業内容</th>
			</tr>
			<tr>
				<td><input type="text" name="contents"
					value="${editForm.contents}" /></td>
			</tr>
			<tr>
				<th>備考</th>
			</tr>
			<tr>
				<td><input type="text" name="note" value="${editForm.note}" /></td>
			</tr>
		</table>
		<input type="hidden" name="id" value="${editForm.id}" /><input
			type="submit" value="更新" />
	</form>
</body>
</html>