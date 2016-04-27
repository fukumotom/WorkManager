package test.test.ojt.servlet;

import java.time.LocalDate;
import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.db.dialect.DBUtil;
import test.test.ojt.common.exception.BusinessException;
import test.test.ojt.common.util.DateUtils;
import test.test.ojt.logic.WorkLogic;
import test.test.ojt.model.Work;

public class WorkHelper {
	private static final Logger logger = LoggerFactory
			.getLogger(WorkHelper.class);

	void idCheck(Integer id) throws BusinessException {
		if (id == null) {
			throw new BusinessException("行を選択して下さい。");
		}
	}

	void action(Work work, String actionName) throws BusinessException {

		WorkLogic logic = new WorkLogic();
		LocalTime time;
		switch (actionName) {

		case "insert":
			time = logic.getStartTime(work);
			work.setStartTime(DateUtils.getParseTime(time));
			work.setEndTime(DateUtils.getParseTime(time));

			logic.insertWork(work);

			break;

		case "add":
			time = logic.getEndTime(work);
			work.setStartTime(DateUtils.getParseTime(time));
			work.setEndTime(DateUtils.getParseTime(time));

			logic.insertWork(work);

			break;

		case "delete":
			logic.delete(work);
			break;
		}
	}

	/**
	 * 履歴取得用日付の入力チェック
	 * 
	 * @param historyDate
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	void dateCheck(Work inputWork) throws BusinessException {

		LocalDate workDate = inputWork.getWorkDate();
		logger.info("入力日付:{}", workDate);

		if (workDate == null) {
			throw new BusinessException("日付を入力してください。");
		}

		// 過去日チェック
		logger.info("今日の日付:{}", LocalDate.now());
		if (workDate.isAfter(LocalDate.now())) {
			throw new BusinessException("過去日を選択してください。");
		}
	}

}
