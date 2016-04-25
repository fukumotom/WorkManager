package test.test.ojt.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptionUtils {

	private static final Logger logger = LoggerFactory.getLogger(EncryptionUtils.class);

	private EncryptionUtils() {
	}

	public static String getEncPassword(String plainPassword) {
		return getSHA256(plainPassword);
	}

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
