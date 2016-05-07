UPDATE 
	WorkManage
SET
	end_time = ${endTime}
	, working_time = ${workingTime}
WHERE
	id = ${id}
	AND user_name = ${userName}
	AND delete_flg = 0
	AND end_time is NULL