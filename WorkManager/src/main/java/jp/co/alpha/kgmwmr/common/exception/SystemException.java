package jp.co.alpha.kgmwmr.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * システムエラークラス
 * 
 * @author kigami
 *
 */
public class SystemException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory
			.getLogger(SystemException.class);

	public SystemException(Throwable e) {
		this("システムエラー", e);
	}

	public SystemException(String message) {
		this(message, null);
	}

	public SystemException(String message, Throwable cause) {
		super(message, cause);
		logger.error(message + ":{}", cause);
	}
}
