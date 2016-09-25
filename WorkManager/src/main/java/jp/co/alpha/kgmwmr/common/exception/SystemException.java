package jp.co.alpha.kgmwmr.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.alpha.kgmwmr.common.util.PropertyUtils;

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
	 * @param cause
	 *            例外情報
	 */
	public SystemException(Throwable cause) {
		this(cause, null);
	}

	/**
	 * システム例外
	 * 
	 * @param messageCode
	 *            メッセージコード
	 * @param args
	 *            メッセージ生成引数
	 */
	public SystemException(String messageCode, String... args) {
		this(null, messageCode, args);
	}

	/**
	 * システム例外
	 * 
	 * @param cause
	 *            例外情報
	 * @param messageCode
	 *            メッセージコード
	 * @param args
	 *            メッセージ生成引数
	 */
	public SystemException(Throwable cause, String messageCode,
			String... args) {
		super(messageCode, cause);
		logger.error(PropertyUtils.getValue(messageCode), cause);
	}
}
