package jp.co.alpha.kgmwmr.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * バインド例外クラス
 * 
 * @author kigami
 *
 */
public class BindFormatException extends Exception {

	/**
	 * シリアルバージョン
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ロガー
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(BindFormatException.class);

	/**
	 * パラメータ名
	 */
	private final String paramName;

	/**
	 * パラメータ値
	 */
	private final String paramValue;

	/**
	 * 入力値不正
	 * 
	 * @param paramName
	 *            入力項目名
	 * @param paramValue
	 *            入力値
	 */
	public BindFormatException(String paramName, String paramValue) {
		this.paramName = paramName;
		this.paramValue = paramValue;
		logger.warn("{}の値[{}]が不正です。", this.paramName, this.paramValue);
	}

	/**
	 * 入力値不正
	 * 
	 * @param e
	 *            例外
	 * @param paramName
	 *            入力項目名
	 * @param paramValue
	 *            入力値
	 */
	public BindFormatException(Throwable e, String paramName,
			String paramValue) {
		this.paramName = paramName;
		this.paramValue = paramValue;
		logger.warn("バインドエラー", e);
		logger.warn("{}の値[{}]が不正です。", this.paramName, this.paramValue);
	}

	/**
	 * 入力エラーメッセージ取得
	 * 
	 * @return エラーメッセージ
	 */
	public String getErrMsg() {
		String errMsg;
		if (this.paramName != null && this.paramValue != null) {
			errMsg = this.paramName + "の値[" + this.paramValue + "]が不正です。";
		} else {
			errMsg = "入力値バインドエラーです。";
		}
		return errMsg;
	}
}