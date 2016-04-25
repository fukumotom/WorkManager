INSERT INTO WorkManage(
    user_name
    ,start_time
    ,end_time
    ,working_time
    ,contents
    ,note
    ,work_date
    ,uptime
    ,delete_flg
    ,insert_flg
    ,update_flg
	,status_flg
) VALUES (
${userName}
,${startTime}
,${endTime}
,'00:00:00'
,'サンプル内容X'
,'サンプルメモX'
,Date 'now'
,TIMESTAMP 'now'
,0
,1
,0
,1
);