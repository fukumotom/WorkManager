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
,NULL
,NULL
,${contents}
,${note}
,Date 'now'
,TIMESTAMP 'now'
,0
,0
,0
,0
);