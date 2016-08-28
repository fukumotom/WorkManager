package jp.co.alpha.kgmwmr.common.util;

/**
 * メッセージ定義クラス
 * 
 * @author kigami
 *
 */
public class MsgCodeDef {

	/**
	 * プライベートコンストラクタ
	 */
	private MsgCodeDef() {
	}

	/**
	 * 入力チェック（パスワード不一致エラー）
	 */
	public static final String CONFIRM_ERROR = "w.validation.001";

	/**
	 * 入力チェック（サイズエラー）
	 */
	public static final String SIZE_ERROR = "w.validation.002";

	/**
	 * 入力チェック（未入力エラー）
	 */
	public static final String EMPTY_INPUT = "w.validation.003";

	/**
	 * 入力チェック（フォーマットエラー）
	 */
	public static final String INPUT_FORMAT_ERROR = "w.validation.004";

	/**
	 * 入力チェック（登録ユーザが存在）
	 */
	public static final String EXIT_USER = "w.validation.005";

	/**
	 * 既に作業中の作業が存在
	 */
	public static final String ALREADY_EXIT_WORKING = "w.validation.006";

	/**
	 * 開始時間と終了時間の差分エラー
	 */
	public static final String START_END_ERROR = "w.validation.007";	
	/**
	 * 入力チェック（不正な入力）
	 */
	public static final String BAD_INPUT = "e.validation.001";

	/**
	 * 入力チェック（不正な操作）
	 */
	public static final String BAD_OPERATION = "e.validation.002";
	/**
	 * 仕掛作業取得時エラー
	 */
	public static final String MULTI_DATE_EXIT = "w.db.001";

	/**
	 * 作業完了時警告
	 */
	public static final String ALREADY_FINISHED = "w.db.002";

}
