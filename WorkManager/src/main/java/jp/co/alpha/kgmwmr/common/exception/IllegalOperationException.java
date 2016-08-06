package jp.co.alpha.kgmwmr.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 想定外の操作時の業務例外クラス<br>
 * 二重登録時に発生
 *
 */
public class IllegalOperationException extends RuntimeException {

	/**
	 * シリアルバージョン
	 */
	private static final long serialVersionUID = -3587438319496882905L;

	/**
	 * ロガー
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(IllegalOperationException.class);

	/**
	 * エラーメッセージをログ出力
	 * 
	 * @param message
	 *            エラーメッセージ
	 */
	public IllegalOperationException(String message) {
		super(message, null);
		logger.warn(message);
	}

	/**
	 * 例外情報をログ出力
	 * 
	 * @param cause
	 *            例外情報
	 */
	public IllegalOperationException(Throwable cause) {
		super("二重登録:{}", cause);
		logger.warn("二重登録:{}", cause);
	}

	/**
	 * エラーメッセージをログ出力
	 * 
	 * @param message
	 *            エラーメッセージ
	 * @param cause
	 *            例外情報
	 */
	public IllegalOperationException(String message, Throwable cause) {
		super(message, cause);
		logger.warn(message);
	}

}
