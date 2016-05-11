package test.test.ojt.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BindFormatException extends Exception {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory
			.getLogger(BindFormatException.class);

	private final String paramName;

	private final String paramValue;

	public BindFormatException(String paramName, String paramValue) {
		this.paramName = paramName;
		this.paramValue = paramValue;
		logger.warn("{}の値[{}]が不正です。", this.paramName, this.paramValue);
	}

	public BindFormatException(Exception e, String paramName,
			String paramValue) {
		this.paramName = paramName;
		this.paramValue = paramValue;
		logger.warn("バインドエラー", e);
		logger.warn("{}の値[{}]が不正です。", this.paramName, this.paramValue);
	}

	public String getErrMsg() {
		String errMsg;
		if (this.paramName != null && this.paramValue != null) {
			errMsg = this.paramName + "の値[" + this.paramValue + "]が不正です。";
		} else {
			errMsg = "入力値バインドエラーです。";
		}
		return errMsg;
	}
}