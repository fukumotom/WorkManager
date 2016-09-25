SELECT DISTINCT
	note
FROM
	WorkManage w
WHERE
	w.user_name = ${userName}
	AND w.delete_flg = 0
