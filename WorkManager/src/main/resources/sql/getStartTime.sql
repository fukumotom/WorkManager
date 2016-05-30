SELECT
	start_time
FROM
	WorkManage w
WHERE
	w.id = ${id}
AND
	w.user_name = ${userName}
