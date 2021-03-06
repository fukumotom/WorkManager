package jp.co.alpha.kgmwmr.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.alpha.kgmwmr.common.exception.SystemException;

/**
 * プロパティファイル読み込み用クラス. アプリ起動時にリスナーより呼び出される.
 *
 * @author kigami
 *
 */
public class PropertyUtils {

	/**
	 * ロガー
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(PropertyUtils.class);

	/**
	 * プロパティUtils（シングルトン）
	 */
	private static PropertyUtils propertyUtils = null;

	/**
	 * プロパティ（シングルトン）
	 */
	private Properties prop = new Properties();

	/**
	 * プライベートコンストラクタ
	 */
	private PropertyUtils() {
	}

	/**
	 * メッセージプロパティファイルをエンコードして読み込む
	 */
	private void loadMsgProperty() {

		try (InputStream iStream = PropertyUtils.class.getClassLoader()
				.getResourceAsStream("messages.properties")) {
			prop.load(new InputStreamReader(iStream, StandardCharsets.UTF_8));

		} catch (IOException e) {
			// プロパティが使用できないため、ハードコーディング
			logger.error("プロパティファイル読み込み失敗", e);
		}
	}

	/**
	 * DBプロパティファイルをエンコードして読み込む
	 */
	private void loadDbProperty() {

		try (InputStream iStream = PropertyUtils.class.getClassLoader()
				.getResourceAsStream("db.properties")) {
			prop.load(new InputStreamReader(iStream, StandardCharsets.UTF_8));

		} catch (IOException e) {
			// プロパティが使用できないため、ハードコーディング
			logger.error("プロパティファイル読み込み失敗", e);
		}
	}

	/**
	 * プロパティファイルの読み込み
	 */
	public static void load() {

		if (propertyUtils == null) {
			propertyUtils = new PropertyUtils();
			propertyUtils.loadMsgProperty();
			propertyUtils.loadDbProperty();
		}

	}

	/**
	 * propertiesファイルから値を取得
	 * 
	 * @param key
	 * @param args
	 * @return
	 */
	private String getProperty(String key, Object[] args) {

		// メッセージプロパティの値と引数をバインド
		return MessageFormat.format(prop.getProperty(key), args);
	}

	/**
	 * 独自プロパティからの値取得
	 * 
	 * @param key
	 *            取得する値のkey
	 * @param args
	 *            メッセージ引数
	 * @return 取得する値
	 */
	public static String getValue(String key, String... args) {

		if (propertyUtils == null) {
			throw new SystemException(MsgCodeDef.PROP_LOAD_ERROR);
		}

		return propertyUtils.getProperty(key, args);
	}

}
