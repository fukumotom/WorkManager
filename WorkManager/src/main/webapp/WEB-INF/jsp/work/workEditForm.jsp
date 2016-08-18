<%@page import="jp.co.alpha.kgmwmr.common.util.ConstantDef"%>
<%@page import="jp.co.alpha.kgmwmr.form.WorkEditForm"%>
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
	<H2 id="errMsg">${editForm.errMsgs}</H2>
	<H1>作業編集フォーム</H1>
	<%
		// 作業内容リストと備考リストのコンボボックスデータ取得用
		WorkEditForm editForm = (WorkEditForm) request
				.getAttribute(ConstantDef.ATTR_EDIT_FORM);
	%>
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
					value="${editForm.contents}" list="contentsList" /> <datalist
						id="contentsList">
						<option value="${editForm.contents}" selected></option>
						<%
							for (String selectContents : editForm.getContentsList()) {
								// 表示されている値以外を選択候補に設定
								if (!selectContents.equals(editForm.getContents())) {
									out.println("<option value=\"" + selectContents
											+ "\"></option>");
								}
							}
						%>
					</datalist></td>
			</tr>
			<tr>
				<th>備考</th>
			</tr>
			<tr>
				<td><input type="text" name="note" value="${editForm.note}"
					list="noteList" /> <datalist id="noteList">
						<option value="${editForm.note}" selected></option>
						<%
							for (String selectNote : editForm.getNoteList()) {
								// 表示されている値以外を選択候補に設定
								if (!selectNote.equals(editForm.getNote())) {
									out.println(
											"<option value=\"" + selectNote + "\"></option>");
								}
							}
						%>
					</datalist></td>
			</tr>
		</table>
		<input type="hidden" name="id" value="${editForm.id}" /> <input
			type="submit" value="更新" />
	</form>
</body>
</html>