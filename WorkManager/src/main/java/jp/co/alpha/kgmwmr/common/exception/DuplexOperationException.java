package jp.co.alpha.kgmwmr.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 二重登録時の業務例外クラス
 *
 */
public class DuplexOperationException extends RuntimeException {

	/**
	 * シリアルバージョン
	 */
	private static final long serialVersionUID = -3587438319496882905L;

	/**
	 * ロガー
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(DuplexOperationException.class);

	public DuplexOperationException(String message) {
		super(message, null);
		logger.warn(message);
	}

	public DuplexOperationException(Throwable cause) {
		super("二重登録:{}", cause);
		logger.warn("二重登録:{}", cause);
	}

	public DuplexOperationException(String message, Throwable cause) {
		super(message, cause);
		logger.warn(message);
	}

}
