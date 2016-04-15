<?xml version="1.0" encoding="UTF-8"?>
<taglib 
		xmlns="http://java.sun.com/xml/ns/j2ee"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee web-jsptaglibrary_2_0.xsd"
		version="2.0"
/>


<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="jp.co.ojt.model.Work, java.util.*" %>
<html>
	<head>
		<meta charset="UTF-8" >
		<title>
			作業リスト画面
		</title>
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
	%>

	<body>
		<table>
			<tr>
				<td>
					<form method="get" action="/WorkManager/Menu">
						<input type="submit" value="メニュー">
					</form>
				</td>
				<td>
					<form method="get" action="/WorkManager/Logout">
						<input type="submit" value="ログアウト">
					</form>
				</td>			</tr>
		</table>
		<form method="post" action="/WorkManager/WorkList">
			<table>
				<tr>
					<th>
					</th>
					<th>
						開始時間
					</th>
					<th>
						終了時間
					</th>
					<th>
						作業時間
					</th>
					<th>
						作業内容
					</th>
					<th>
						備考
					</th>
				</tr>
				<%
				for (int index = 0; index < workList.size(); index++) {
					Work work = workList.get(index);
					request.setAttribute("work", work);
					%>
					<tr>
						<td>
							<input type="radio" name="radio" value="${work.id}">
						</td>
						<td>
							${work.startTime}
						</td>
						<td>
							${work.endTime}
						</td>
						<td>
							${work.workingTime}
						</td>
						<td>
							${work.contents}
						</td>
						<td>
							${work.note}
						</td>
					</tr>
					<%
				}
				%>
			</table>
			<table>
				<tr>
					<td>
						<input type="submit" value="挿入" name="insertBtn">
					</td>
					<td>
						<input type="submit" value="追加" name="addBtn">
					</td>
					<td>
						<input type="submit" value="削除" name="deleteBtn">
					</td>
					<td>
						<input type="submit" value="保存" name="saveBtn">
					</td>
					<td>
						<input type="checkbox" value="1" name="delFlg">
						削除を含む
						<input type="text" name="logDate">
						(yyyy/MM/dd)
						<input type="submit" value="履歴" name="logBtn">
					</td>
				</tr>
			</table>

		</form>
	</body>
</html>
