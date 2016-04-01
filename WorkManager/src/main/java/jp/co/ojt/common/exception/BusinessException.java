package jp.co.ojt.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 業務例外クラス
 * 最後、業務エラーページに遷移させる
 *
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(BusinessException.class);

	public void Businessexce(String msg) {
		logger.error(msg);
	}

	public void Businessexce(Exception e) {
		logger.error("業務エラー", e);
	}

}
