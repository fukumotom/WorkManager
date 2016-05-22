UPDATE 
	WorkManage
SET
	status_flg = 0
WHERE
	id = ${id}
	AND user_name = ${userName}
	AND status_flg = 1