SELECT
	startTime
FROM
	WorkManage w
WHERE
	w.id = ${ID}
AND
	w.user_name = &{userName}
