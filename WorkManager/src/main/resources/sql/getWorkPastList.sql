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
<<<<<<< fa6b7c89464b5d6fdc93c391ed5264d21033cfa3
	AND w.delete_flg = 0
	AND w.status_flg = 1
=======
	AND w.delete_flg = ${deleteFlg}
>>>>>>> create csv export func #103
ORDER BY
	w.start_time, w.end_time