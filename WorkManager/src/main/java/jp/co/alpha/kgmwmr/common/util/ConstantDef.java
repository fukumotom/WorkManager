package jp.co.alpha.kgmwmr.common.util;

/**
 * 定数定義クラス
 * 
 * @author kigami
 *
 */
public class ConstantDef {

	/**
	 * プライベートコンストラクタ
	 */
	private ConstantDef() {
	}

	/**
	 * エラーメッセージ（属性）
	 */
	public static final String ERROR_MSG = "errorMsg";

	/**
	 * フォーム（属性）
	 */
	public static final String ATTR_FORM = "form";

	/**
	 * フォーム（属性）
	 */
	public static final String ATTR_EDIT_FORM = "editForm";

	/**
	 * 検索条件（属性）
	 */
	public static final String CRITERIA = "criteria";

	/**
	 * 検索するJNDI名
	 */
	public static final String DB_LOOK_UP = "db.look.up.name";

	/**
	 * 検索条件の削除checkbox(ON)
	 */
	public static final String DELETE_CHECK_ON = "on";

	/**
	 * 検索条件の削除checkbox(OFF)
	 */
	public static final String DELETE_CHECK_OFF = "off";

	/**
	 * 状態フラグ(未編集)
	 */
	public static final int STATUS_NOT_EDIT = 0;

	/**
	 * 状態フラグ(編集中)
	 */
	public static final int STATUS_EDIT = 1;
}
