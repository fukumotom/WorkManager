<%@page contentType="text/html; charset=utf-8"%>
<!-- 仕掛処理 -->
<table class="centerTable" border="1">
	<tr>
		<td align="left">
			<table>
				<tr>
					<th>開始時間</th>
				</tr>
				<tr>
					<td>${form.work.startTime}</td>
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
					<td>${form.work.contents}</td>
					<td>${form.work.note}</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<form method="post" action="/WorkManager/WorkRegister">
				<input type="hidden" name="id" value="${form.work.id}" /> <input
					type="submit" name="finishBtn" value="作業終了" />
			</form>
		</td>
	</tr>
</table>