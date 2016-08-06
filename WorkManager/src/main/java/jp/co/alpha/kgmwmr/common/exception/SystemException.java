package jp.co.alpha.kgmwmr.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * システム例外クラス
 * 
 * @author kigami
 *
 */
public class SystemException extends RuntimeException {

	/**
	 * シリアルバージョン
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ロガー
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(SystemException.class);

	/**
	 * システム例外
	 * 
	 * @param e
	 *            例外情報
	 */
	public SystemException(Throwable e) {
		this("システムエラー", e);
	}

	/**
	 * システム例外
	 * 
	 * @param message
	 *            エラーメッセージ
	 */
	public SystemException(String message) {
		this(message, null);
	}

	/**
	 * システム例外
	 * 
	 * @param message
	 *            エラーメッセージ
	 * @param cause
	 *            例外情報
	 */
	public SystemException(String message, Throwable cause) {
		super(message, cause);
		logger.error(message + ":{}", cause);
	}
}
