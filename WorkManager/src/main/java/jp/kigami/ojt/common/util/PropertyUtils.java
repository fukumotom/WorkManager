package jp.kigami.ojt.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * プロパティファイル読み込み用クラス. アプリ起動時にリスナーより呼び出される.
 *
 */
public class PropertyUtils {

	private static final Logger logger = LoggerFactory.getLogger(PropertyUtils.class);

	private static PropertyUtils propertyUtils = null;
	private Properties prop = new Properties();

	private PropertyUtils() {

		// singleton
	}

	public void loadProperty() {

		try (InputStream iStream = PropertyUtils.class.getClassLoader().getResourceAsStream("messages.properties");) {
			prop.load(iStream);
		} catch (IOException e) {
			logger.error("プロパティファイル読み込み失敗", e);
		}
	}

	/**
	 * プロパティファイルの読み込み
	 */
	public static void load() {

		if (propertyUtils == null) {
			propertyUtils = new PropertyUtils();
			propertyUtils.loadProperty();
		}

	}

	public String getProperty(String key) {
		return prop.getProperty(key);
	}

	/**
	 * 独自プロパティからの値取得
	 * 
	 * @param key
	 *            取得する値のkey
	 * @return 取得する値
	 */
	public static String getValue(String key) {

		if (propertyUtils == null) {
			return null;
		}
		return propertyUtils.getProperty(key);
	}

}
