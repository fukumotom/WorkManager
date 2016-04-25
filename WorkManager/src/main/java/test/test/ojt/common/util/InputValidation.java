package test.test.ojt.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputValidation {

	private static final Logger logger = LoggerFactory.getLogger(InputValidation.class);

	private InputValidation() {
	}

	/**
	 * data size check
	 * 
	 * @param target
	 *            check data
	 * @param min
	 *            data's minimum size
	 * @param maxdata's
	 *            maximum size
	 * @return
	 */
	public static ValidationResult inputSize(String target, int min, int max) {

		ValidationResult result = new ValidationResult();

		logger.info("チェックする文字列：{}", target);

		if ((target.length() <= max) && (target.length() >= min)) {

			result.setCheckResult(true);
		} else {
			result.setCheckResult(false);
			result.setErrorMsg(PropertyUtils.getValue(MsgCodeDef.SIZE_ERROR));
		}

		return result;
	}

	/**
	 * password confirm
	 * 
	 * @param target1
	 *            password
	 * @param target2
	 *            confirmPassword
	 * @return checkResult
	 */
	public static ValidationResult confilm(String target1, String target2) {

		ValidationResult result = new ValidationResult();

		if (target1.equals(target2)) {
			result.setCheckResult(true);
		} else {
			result.setCheckResult(false);
			result.setErrorMsg(PropertyUtils.getValue(MsgCodeDef.CONFIRM_ERROR));
		}
		return result;
	}

}
