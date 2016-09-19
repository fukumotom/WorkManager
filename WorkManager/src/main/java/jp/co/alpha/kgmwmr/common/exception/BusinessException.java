package jp.co.alpha.kgmwmr.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.alpha.kgmwmr.common.util.PropertyUtils;

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
	 * @param messageCode
	 *            メッセージコード
	 * @param args
	 *            メッセージ生成引数
	 */
	public BusinessException(String messageCode, String... args) {
		this(null, messageCode, args);
	}

	/**
	 * 例外情報をログ出力
	 * 
	 * @param cause
	 *            例外
	 */
	public BusinessException(Throwable cause) {
		this(cause, null);
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
	public BusinessException(Throwable cause, String messageCode,
			String... args) {
		super(PropertyUtils.getValue(messageCode, args), cause);
		logger.warn(PropertyUtils.getValue(messageCode, args), cause);
	}

}
