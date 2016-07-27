UPDATE 
	WorkManage
SET
	delete_flg = 1
	,status_flg = 1
WHERE
	id = ${id}
	AND user_name = ${userName}
	AND status_flg = 1