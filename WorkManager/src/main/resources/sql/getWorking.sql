SELECT
    id
    ,start_time
    ,contents
    ,note
FROM
    WorkManage w
WHERE
    w.user_name = ${userName}
    AND w.end_time is NULL
    AND w.work_date = Date 'now'
    AND w.delete_flg = 0
    AND w.status_flg = 0
order by 
    w.start_time, w.end_time