package jp.co.ojt.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(SystemException.class);

	public SystemException(Exception e) {
		logger.error("システムエラー", e);
	}

	public SystemException(String string) {
		logger.error("システムエラー");
	}

}
