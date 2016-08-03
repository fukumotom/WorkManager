UPDATE 
	WorkManage
SET
	status_flg = 0
WHERE
	user_name = ${userName}
	AND status_flg = 1