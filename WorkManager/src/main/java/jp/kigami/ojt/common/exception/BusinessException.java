package jp.kigami.ojt.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 業務例外クラス 最後、業務エラーページに遷移させる
 *
 */
public class BusinessException extends Exception {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory
			.getLogger(BusinessException.class);

	public BusinessException(String message) {
		super(message, null);
		logger.warn(message);
	}

	public BusinessException(Throwable cause) {
		super("業務エラー:{}", cause);
		logger.warn("業務エラー:{}", cause);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
		logger.warn(message);
	}

}
