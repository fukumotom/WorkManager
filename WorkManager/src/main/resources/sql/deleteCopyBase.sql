DELETE FROM
	WorkManage
WHERE
	user_name = ${userName}
	AND work_date = ${workDate}
	AND status_flg = 0