package jp.co.alpha.kgmwmr.common.util;

import java.time.DateTimeException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 入力チェック
 * 
 * @author kigami
 *
 */
public class InputValidation {

	/**
	 * ロガー
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(InputValidation.class);

	/**
	 * プライベートコンストラクタ
	 */
	private InputValidation() {
	}

	/**
	 * データサイズチェック
	 * 
	 * @param target
	 *            チェック対象
	 * @param min
	 *            データ最小値
	 * @param maxdata
	 *            データ最大値
	 * @return チェック結果
	 */
	public static boolean inputSize(String target, int min, int max) {

		logger.info("チェックする文字列：{}", target);

		return (target.length() <= max) && (target.length() >= min);
	}

	/**
	 * パスワード入力確認
	 * 
	 * @param target1
	 *            パスワード
	 * @param target2
	 *            確認用パスワード
	 * @return パスワード同値確認
	 */
	public static boolean confilm(String target1, String target2) {

		return target1.equals(target2);
	}

	/**
	 * 数字判定
	 * 
	 * @param id
	 *            チェック対象
	 *
	 * @return チェック結果
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
	 * @param target
	 *            チェック対象
	 * @return チェック結果
	 */
	public static boolean isTime(String target) {

		boolean result = true;
		try {

			DateUtils.getParseTime(target);

		} catch (DateTimeException e) {
			result = false;
		}

		return result;
	}

	/**
	 * 日付（yyyy/MM/dd）判定
	 * 
	 * @param target
	 *            チェック対象
	 * @return チェック結果
	 */
	public static boolean isDate(String target) {

		boolean result = true;
		try {
			DateUtils.getParseDate(target);
		} catch (DateTimeException e) {
			result = false;
		}

		return result;
	}

	/**
	 * 作業idチェック
	 * 
	 * @param id
	 *            チェック対象
	 * @return チェック結果
	 */
	public static boolean idCheck(String id) {

		boolean result = true;

		if (id == null) {
			result = false;
		} else if (!id.isEmpty()) {

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
	 *            チェック対象
	 * @return チェック結果
	 */
	public static boolean flgCheck(String workingFlgStr) {

		return Boolean.parseBoolean(workingFlgStr);
	}
}
