UPDATE 
	WorkManage
SET
	endT_time = ${endTime}
	, working_time = ${workingTime}
WHERE
	id = ${id}
	AND user_name = ${userName}
	AND deleteFlg = 0
	AND endTime is NULL