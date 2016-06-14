package jp.kigami.ojt.common.util;

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
	 * メッセージリストの初期化
	 */
	public ValidationResult() {
		this.errorMsgList = new ArrayList<>();
	}

	/**
	 * すべてのエラーメッセージを結合して取得
	 * 
	 * @return the errorMsg
	 */
	public String getErrorMsgs() {

		StringBuilder sb = new StringBuilder();
		for (String errorMsg : errorMsgList) {
			sb.append(errorMsg);
			sb.append("<BR />");
		}

		return sb.toString();
	}

	/**
	 * リストにメッセージを追加
	 * 
	 * @param errorMsg
	 *            the errorMsg to add
	 */
	public void addErrorMsg(String errorMsg) {
		this.errorMsgList.add(errorMsg);
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
