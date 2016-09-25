package jp.co.alpha.kgmwmr.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 入力チェック結果格納クラス
 * 
 * @author kigami
 *
 */
public class ValidationResult {

	/**
	 * 入力エラーメッセージのリスト
	 */
	private List<String> errorMsgList;

	/**
	 * 入力チェック結果
	 */
	private boolean checkResult;

	/**
	 * エラーメッセージ用連結文字列
	 */
	private static final String DELIMITER = "<BR />";

	/**
	 * メッセージリストの初期化
	 */
	public ValidationResult() {
		this.errorMsgList = new ArrayList<>();
	}

	/**
	 * すべてのエラーメッセージを結合して取得
	 * 
	 * @return エラーメッセージ
	 */
	public String getErrorMsgs() {

		return String.join(DELIMITER, errorMsgList);
	}

	/**
	 * リストにメッセージを追加
	 * 
	 * @param errorMsg
	 *            追加メッセージ
	 */
	public void addErrorMsg(String errorMsgCode, String... args) {
		this.errorMsgList.add(PropertyUtils.getValue(errorMsgCode, args));
	}

	/**
	 * @return the checkResult
	 */
	public boolean isCheckResult() {
		return checkResult;
	}

	/**
	 * @param checkResult
	 *            the checkResult to set
	 */
	public void setCheckResult(boolean checkResult) {
		this.checkResult = checkResult;
	}

}
