package jp.co.alpha.kgmwmr.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 業務例外クラス<br>
 * 最後、業務エラーページに遷移させる
 *
 */
public class BusinessException extends Exception {

	/**
	 * シリアルバージョン
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ロガー
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(BusinessException.class);

	/**
	 * エラーメッセージをログ出力
	 * 
	 * @param message
	 *            エラーメッセージ
	 */
	public BusinessException(String message) {
		super(message, null);
		logger.warn(message);
	}

	/**
	 * 例外情報をログ出力
	 * 
	 * @param cause
	 *            例外
	 */
	public BusinessException(Throwable cause) {
		super("業務エラー:{}", cause);
		logger.warn("業務エラー:{}", cause);
	}

	/**
	 * エラーメッセージをログ出力
	 * 
	 * @param message
	 *            エラーメッセージ
	 * @param cause
	 *            例外
	 */
	public BusinessException(String message, Throwable cause) {
		super(message, cause);
		logger.warn(message);
	}

}
