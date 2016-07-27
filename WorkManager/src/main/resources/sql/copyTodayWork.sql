INSERT INTO
	workmanage(
		SELECT
			nextval('workmanage_id_seq') AS id
			,user_name
			,start_time
			,end_time
			,working_time
			,contents
			,note
			,work_date
			,uptime
			,insert_flg
			,update_flg
			,delete_flg
			,1 AS status_flg
		FROM
			workmanage
		WHERE
			user_name = ${userName}
			AND work_date = DATE 'now'
	);
