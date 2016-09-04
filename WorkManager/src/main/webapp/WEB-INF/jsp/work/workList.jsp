<?xml version="1.0" encoding="UTF-8"?>
<%@page import="jp.co.alpha.kgmwmr.form.WorkListForm"%>
<%@page import="jp.co.alpha.kgmwmr.model.Work"%>
<%@page import="jp.co.alpha.kgmwmr.common.util.ConstantDef"%>
<%@page import="jp.co.alpha.kgmwmr.form.WorkListViewForm"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/app/css/styles.css">
<title>作業リスト画面</title>
</head>
<body>
	<%
		WorkListViewForm form = (WorkListViewForm) request
				.getAttribute(ConstantDef.ATTR_FORM);

		WorkListForm sessionForm = (WorkListForm) session
				.getAttribute(ConstantDef.CRITERIA);
		boolean delFlg = ConstantDef.DELETE_CHECK_ON
				.equals(sessionForm.getDeleteCechk());
		if (delFlg) {
			pageContext.setAttribute("delCheck", "(削除済み)");
		}
		pageContext.setAttribute("delFlg", delFlg);
	%>
	<jsp:include page="/WEB-INF/jsp/header.jsp" />
	<H2 id="errMsg">${form.errMsgs}</H2>
	<H1>${form.listDate}の作業リスト(${delCheck}</H1>
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
				List<Work> workList = form.getWorkList();
				for (int index = 0; index < workList.size(); index++) {
					pageContext.setAttribute("index", index);
			%>
			<tr>
				<td><input type="radio" name="id"
					value="${form.workList[index].id}"
					<%= index == 0?"checked=checked":"" %> /></td>
				<td>${form.workList[index].startTime}</td>
				<td>${form.workList[index].endTime}</td>
				<td>${form.workList[index].workingTime}</td>
				<td>${form.workList[index].contents}</td>
				<td>${form.workList[index].note}</td>
			</tr>
			<%
				}
			%>
		</table>
		<br />${(empty form.workList)?"データがありません。":""}<br />
		<table>
			<tr>
				<td><input type="submit" value="挿入" name="insertBtn"
					style=${(empty form.workList) || delFlg?'display:none':''} /></td>
				<td><input type="submit" value="追加" name="addBtn"
					style=${(empty form.workList) || delFlg?'display:none':''} /></td>
				<td><input type="submit" value="編集" name="editBtn"
					style=${(empty form.workList) || delFlg?'display:none':''} /></td>
				<td><input type="submit" value="削除" name="deleteBtn"
					style=${(empty form.workList) || delFlg?'display:none':''} /></td>
				<td><input type="submit" value="保存" name="saveBtn"
					style=${(empty form.workList) || delFlg?'display:none':''} /></td>
				<td><input type="submit" value="CSVダウンロード"
					name="csvDownloadBtn"
					style=${(empty form.workList) || delFlg?'display:none':''} /></td>
				<td><input type="checkbox" name="deleteFlg" /> 削除を含む <input
					type="text" name="workDate" /> (yyyy/MM/dd) <input type="hidden"
					name="listDate" value="${form.listDate}" /> <input type="submit"
					value="履歴" name="historyBtn" /></td>


			</tr>
		</table>
	</form>
</body>
</html>