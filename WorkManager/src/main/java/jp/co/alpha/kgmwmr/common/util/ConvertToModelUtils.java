package jp.co.alpha.kgmwmr.common.util;

/**
 * 変換Utilクラス
 * 
 * @author kigami
 *
 */
public class ConvertToModelUtils {

	/**
	 * プライベートコンストラクタ
	 */
	private ConvertToModelUtils() {
	}

	/**
	 * 文字列変換<br>
	 * 変換対象がString型でない場合、nulを返却
	 * 
	 * @param target
	 *            変換対象
	 * @return 文字列
	 */
	public static String convertStr(Object target) {

		if (target instanceof String) {
			return (String) target;
		} else {
			return null;
		}
	}

	/**
	 * 数値変換<br>
	 * 変換対象がnullの場合、nulを返却
	 * 
	 * @param target
	 *            変換対象
	 * @return 数値
	 */
	public static Integer convertInt(String target) {

		if (target != null) {
			return Integer.valueOf(target);
		} else {
			return null;
		}

	}

	/**
	 * フラグのboolean変換
	 * 
	 * @param target
	 *            変換対象
	 * @return onの場合:true それ以外の場合:false
	 */
	public static boolean convertBoolean(String target) {
		return "on".equals(target);

	}
}
