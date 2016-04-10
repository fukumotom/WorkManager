<?xml version="1.0" encoding="UTF-8"?>
<taglib xmlns="http://java.sun.com/xml/ns/j2ee"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee
            web-jsptaglibrary_2_0.xsd"
        version="2.0" />


<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="jp.co.ojt.model.Work, java.util.*" %>
<html>
	<head>
		<meta charset="UTF-8" >
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
%>

	<body>
		<form method="post" action="/WorkManager/WorkList">
				<table>
				<tr>
					<th></th><th>開始時間</th><th>終了時間</th><th>作業時間</th><th>作業内容</th><th>備考</th>
				</tr>
	<% 
				for(Work work : workList){
	%>
				<tr>
	<!-- 			<td><input type="radio" name="radio" value="${work.id}"></td><td>${work.startTime}</td><td>${work.endTime}</td><td>${work.workingTime}</td><td>${work.contents}</td><td>${work.note}</td> -->
				<td><input type="radio" name="radio" value="<%= work.getId() %>"</td><td><%= work.getStartTime() %></td><td><%= work.getEndTime() %></td><td><%= work.getWorkingTime() %></td><td><%= work.getContents() %></td><td><%= work.getNote() %></td>
				</tr>
	<% 
				}
	%>
				<td><input type="submit" value="追加" name="addBtn"></td>
				</table>
				
			</form>
	</body>
</html>