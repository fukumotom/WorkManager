SELECT
	id
	,user_name
	,start_time
	,end_time
	,working_time
	,contents
	,note
FROM
	WorkManage w
WHERE
	w.user_name = ${userName}
	And w.work_date = ${workDate}
	AND w.delete_flg = 0
ORDER BY
	w.start_time, w.end_time
