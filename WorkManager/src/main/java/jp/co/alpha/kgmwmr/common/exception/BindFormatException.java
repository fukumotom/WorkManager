package jp.co.alpha.kgmwmr.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.alpha.kgmwmr.common.util.MsgCodeDef;
import jp.co.alpha.kgmwmr.common.util.PropertyUtils;

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
		this(null, paramName, paramValue);
	}

	/**
	 * 入力値不正
	 * 
	 * @param cause
	 *            例外情報
	 * @param paramName
	 *            入力項目名
	 * @param paramValue
	 *            入力値
	 */
	public BindFormatException(Throwable cause, String paramName,
			String paramValue) {
		this.paramName = paramName;
		this.paramValue = paramValue;
		logger.warn(PropertyUtils.getValue(MsgCodeDef.BIND_ERROR), cause);
		logger.warn(PropertyUtils.getValue(MsgCodeDef.INPUT_ERROR,
				this.paramName, this.paramValue));
	}

	/**
	 * 入力エラーメッセージ取得
	 * 
	 * @return エラーメッセージ
	 */
	public String getErrMsg() {
		String errMsg;
		if (this.paramName != null && this.paramValue != null) {
			errMsg = PropertyUtils.getValue(MsgCodeDef.INPUT_ERROR,
					this.paramName, this.paramValue);
		} else {
			errMsg = PropertyUtils.getValue(MsgCodeDef.BIND_ERROR);
		}
		return errMsg;
	}
}