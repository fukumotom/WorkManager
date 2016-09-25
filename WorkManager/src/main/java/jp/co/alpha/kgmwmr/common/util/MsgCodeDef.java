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

	/**
	 * <<<<<<< 4aa0652a7bb3f2f23441d1e522345c2ec4c4a9f3 削除済みデータ削除時警告
	 */
	public static final String ALREADY_DELETE = "w.db.003";

	/**
	 * DB挿入
	 */
	public static final String DB_INSERT = "i.db.001";

	/**
	 * DB更新
	 */
	public static final String DB_UPDATE = "i.db.002";

	/**
	 * DB削除
	 */
	public static final String DB_DELETE = "i.db.003";

	/**
	 * DB挿入失敗
	 */
	public static final String MISS_DB_INSERT = "e.db.001";

	/**
	 * DB複製失敗
	 */
	public static final String MISS_DB_COPY = "e.db.002";

	/**
	 * DB削除失敗
	 */
	public static final String MISS_DB_DELETE = "e.db.003";

	/**
	 * DB取得失敗
	 */
	public static final String MISS_DB_FIND = "e.db.004";

	/**
	 * DB更新失敗
	 */
	public static final String MISS_DB_UPDATE = "e.db.005";

	/**
	 * 仕掛作業あり
	 */
	public static final String WORKING = "i.business.001";

	/**
	 * 仕掛作業なし
	 */
	public static final String NOT_WORKING = "i.business.002";

	/**
	 * 作業開始時の警告
	 */
	public static final String ALREADY_START = "w.business.001";

	/**
	 * 作業追加時の警告
	 */
	public static final String CAN_NOT_ADD = "w.business.002";

	/**
	 * 作業追加時の警告
	 */
	public static final String CONNECTION_CLOSE_ERROR = "w.business.003";

	/**
	 * 不正な入力値
	 */
	public static final String INPUT_ERROR = "w.business.004";

	/**
	 * バインドに失敗しました
	 */
	public static final String BIND_ERROR = "e.business.001";

	/**
	 * リフレクション失敗
	 */
	public static final String REFLECTION_ERROR = "e.business.002";

	/**
	 * JNDI接続エラー
	 */
	public static final String CONNECTION_ERROR = "e.business.003";

	/**
	 * SQLファイル読み込み失敗
	 */
	public static final String SQL_LOAD_ERROR = "e.business.004";

	/**
	 * 複数件存在
	 */
	public static final String ALREADY_EXIT = "e.business.005";

	/**
	 * リダイレクト失敗
	 */
	public static final String ERR_REDIRECT = "e.business.006";

	/**
	 * リダイレクト失敗
	 */
	public static final String ERR_FORWARD = "e.business.007";

	/**
	 * プロパティファイル読み込み失敗
	 */
	public static final String PROP_LOAD_ERROR = "e.business.008";

	/**
	 * クローズ失敗
	 */
	public static final String MISS_CLOSE = "e.business.009";
}
