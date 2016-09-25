package jp.co.alpha.kgmwmr.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 暗号化クラス
 * 
 * @author kigami
 *
 */
public class EncryptionUtils {

	/**
	 * ロガー
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(EncryptionUtils.class);

	/**
	 * プライベートコンストラクタ
	 */
	private EncryptionUtils() {
	}

	/**
	 * 暗号化パスワードの取得
	 * 
	 * @param plainPassword
	 *            プレーンパスワード
	 * @return 暗号化パスワード
	 */
	public static String getEncPassword(String plainPassword) {
		String password = getSHA256(plainPassword);

		return password;
	}

	/**
	 * パスワードの暗号化
	 * 
	 * @param target
	 *            暗号化対象
	 * @return 暗号化パスワード
	 */
	private static String getSHA256(String target) {
		MessageDigest md;
		StringBuilder buf = new StringBuilder();

		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(target.getBytes());
			byte[] digest = md.digest();

			for (byte b : digest) {
				buf.append(String.format("%02x", b));
			}

		} catch (NoSuchAlgorithmException e) {
			logger.error("暗号化失敗", e);
		}
		return buf.toString();
	}
}
