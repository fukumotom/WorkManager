UPDATE 
	WorkManage
SET
	start_time = ${startTime}
	,end_time = ${endTime}
	,contents = ${contents}
	,note = ${note}
	,update_flg = 1
	,status_flg = 1
	,uptime = Timestamp 'now'
WHERE
	id = ${id}