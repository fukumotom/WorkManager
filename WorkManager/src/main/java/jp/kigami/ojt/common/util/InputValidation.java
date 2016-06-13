package jp.kigami.ojt.common.util;

import java.time.DateTimeException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputValidation {

	private static final Logger logger = LoggerFactory
			.getLogger(InputValidation.class);

	private InputValidation() {
	}

	/**
	 * データサイズチェック
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
	 * パスワード入力確認
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
			result.setErrorMsg(
					PropertyUtils.getValue(MsgCodeDef.CONFIRM_ERROR));
		}
		return result;
	}

	/**
	 * 数字判定
	 * 
	 * @param id
	 */
	public static boolean isNumber(String id) {

		String regex = "^\\d+$";
		Pattern ptn = Pattern.compile(regex);
		Matcher matcher = ptn.matcher(id);
		boolean result = matcher.matches();

		return result;

	}

	/**
	 * 時間（HH:mm）判定
	 * 
	 * @param startTime
	 * @return
	 */
	public static ValidationResult isTime(String target) {

		ValidationResult result = new ValidationResult();
		try {
			DateUtils.getFomatTime(target);
			result.setCheckResult(true);
		} catch (DateTimeException e) {
			result.setCheckResult(false);
			result.setErrorMsg("フォーマットが違います。");
		}

		return result;
	}

	/**
	 * 作業idチェック
	 * 
	 * @param id
	 * @return
	 */
	public static boolean idCheck(String id) {

		boolean result = true;

		if (id == null) {
			result = false;
		} else if (!id.isEmpty() & id != null) {

			if (!InputValidation.isNumber(id)) {
				result = false;
			}
		}
		return result;
	}

	/**
	 * 作業中フラグチェック
	 * 
	 * @param workingFlgStr
	 * @return
	 */
	public static boolean flgCheck(String workingFlgStr) {

		return Boolean.parseBoolean(workingFlgStr);
	}
}
