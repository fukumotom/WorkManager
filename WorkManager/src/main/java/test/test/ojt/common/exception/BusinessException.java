package jp.co.ojt.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 業務例外クラス 最後、業務エラーページに遷移させる
 *
 */
public class BusinessException extends Exception {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(BusinessException.class);

	public BusinessException(String errMsg) {
		super(errMsg);
		logger.error(errMsg);
	}

	public BusinessException(Exception e) {
		super(e);
		logger.error("業務エラー", e);
	}

	public BusinessException(String errMsg, Exception e) {
		super(errMsg, e);
		logger.error(errMsg, e);
	}

}
