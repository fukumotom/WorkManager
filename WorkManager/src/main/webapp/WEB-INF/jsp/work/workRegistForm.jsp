<%@page import="jp.co.alpha.kgmwmr.form.WorkRegisterViewForm"%>
<%@page import="jp.co.alpha.kgmwmr.common.util.ConstantDef"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/app/css/styles.css">
<title>作業登録フォーム</title>
</head>
<body>
	<jsp:include page="/WEB-INF/jsp/header.jsp" />

	<h1>
		<label>作業登録フォーム</label>
	</h1>
	<h2 align="center">現在の状況:${(form.workingFlg)? "作業中" : "未作業中"}</h2>
	<h2 id="errMsg">${errorMsg}</h2>
	<%
		WorkRegisterViewForm form = (WorkRegisterViewForm) request
				.getAttribute(ConstantDef.ATTR_FORM);
		if (form.isWorkingFlg()) {
	%>
	<jsp:include page="unfinishedwokInclude.jsp" />
	<%
		}
	%>
	<br />
	<form method="post" action="/WorkManager/WorkRegister">
		<table class="centerTable" border="1">
			<tr>
				<td align="left">
					<table>
						<tr>
							<th>開始時間</th>
						</tr>
						<tr>
							<td><input type="time" name="startTime"
								value="${form.nowTime}" /></td>
						</tr>
					</table> <br />
				</td>
			</tr>
			<tr>
				<td align="left">
					<table>
						<tr>
							<th>作業内容</th>
							<th>備考</th>
						</tr>
						<tr>
							<td><input type="text" name="contents" list="contentsList" />
								<datalist id="contentsList">
									<option value="${form.contents}" selected></option>
									<%
										for (String selectContents : form.getContentsList()) {
											out.println(
													"<option value=\"" + selectContents + "\"></option>");
										}
									%>
								</datalist></td>
							<td><input type="text" name="note" list="noteList" /> <datalist
									id="noteList">
									<option value="${form.note}" selected></option>
									<%
										for (String selectNote : form.getNoteList()) {
											out.println("<option value=\"" + selectNote + "\"></option>");
										}
									%>
								</datalist></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center"><input type="hidden"
					name="workingFlg" value="${form.workingFlg}" /> <input
					type="hidden" name="id" value="${form.work.id}" /><input
					type="submit" name="startBtn" value="作業開始" /></td>
			</tr>
		</table>
	</form>
</body>
</html>
