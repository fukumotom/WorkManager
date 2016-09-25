package jp.co.alpha.kgmwmr.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.alpha.kgmwmr.common.util.PropertyUtils;

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
	 * @param messageCode
	 *            メッセージコード
	 * @param args
	 *            メッセージ生成引数
	 */
	public IllegalOperationException(String messageCode, String... args) {
		this(null, messageCode, args);
	}

	/**
	 * 例外情報をログ出力
	 * 
	 * @param cause
	 *            例外情報
	 */
	public IllegalOperationException(Throwable cause) {
		super(null, cause);
	}

	/**
	 * エラーメッセージをログ出力
	 * 
	 * @param cause
	 *            例外情報
	 * @param messageCode
	 *            メッセージコード
	 * @param args
	 *            メッセージ生成引数
	 */
	public IllegalOperationException(Throwable cause, String messageCode,
			String... args) {
		logger.warn(PropertyUtils.getValue(messageCode, args), cause);
	}

}
