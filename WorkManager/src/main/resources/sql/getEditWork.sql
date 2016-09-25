SELECT
    id
    ,start_time
    ,end_time
    ,contents
    ,note
FROM
    WorkManage w
WHERE
    w.user_name = ${userName}
    AND w.id = ${id}
	AND w.status_flg = 1