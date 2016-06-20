package jp.kigami.ojt.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.kigami.ojt.model.Work;

/**
 * 共通ロジッククラス
 * 
 * @author kigami
 *
 */
public class CommonLogic {

	/**
	 * ロガー
	 */
	private static Logger logger = LoggerFactory.getLogger(CommonLogic.class);

	/**
	 * 未保存作業削除処理
	 * 
	 * @param userName
	 */
	public void deleteUnSaveWork(String userName) {

		logger.debug("未保存作業削除処理開始");

		Work work = new Work();
		work.setUserName(userName);

		WorkLogic logic = new WorkLogic();
		logic.deleteUnSaveWork(work);
	}

}
