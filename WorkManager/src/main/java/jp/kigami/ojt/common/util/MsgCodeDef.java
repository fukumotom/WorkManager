package jp.kigami.ojt.common.util;

/**
 * メッセージ定義クラス
 * 
 * @author kigami
 *
 */
public class MsgCodeDef {

	/**
	 * 入力チェック（パスワード不一致エラー）
	 */
	public static final String CONFIRM_ERROR = "e.validation.001";

	/**
	 * 入力チェック（サイズエラー）
	 */
	public static final String SIZE_ERROR = "e.validation.002";

	/**
	 * 入力チェック（未入力エラー）
	 */
	public static final String EMPTY_INPUT = "e.validation.003";

	/**
	 * 入力チェック（フォーマットエラー）
	 */
	public static final String INPUT_FORMAT_ERROR = "e.validation.004";

	/**
	 * 仕掛作業取得時エラー
	 */
	public static final String MULTI_DATE_EXIT = "w.db.001";

	/**
	 * 作業完了時警告
	 */
	public static final String ALREADY_FINISHED = "w.db.002";

	/**
	 * プライベートコンストラクタ
	 */
	private MsgCodeDef() {
	}
}
