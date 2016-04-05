package jp.co.ojt.db.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonDbUtil {

	private static ClassLoader classLoader = CommonDbUtil.class.getClassLoader();

	private static final Logger logger = LoggerFactory.getLogger(CommonDbUtil.class);

	private CommonDbUtil() {
	}

	/**
	 * SQLファイル読み込み
	 * 
	 * @param sqlName
	 *            SQLファイル名
	 * @return 読みこんだSQL文
	 */
	public static String readSql(String sqlName) {

		// sqlファイル読みこみ
		InputStream iStream = classLoader.getResourceAsStream("/sql/" + sqlName);

		StringBuilder builder = new StringBuilder();
		try (InputStreamReader reader = new InputStreamReader(iStream);
				BufferedReader bufReader = new BufferedReader(reader);) {

			while (true) {
				String line = bufReader.readLine();
				if (line == null) {
					break;
				}
				builder.append(line);
			}

		} catch (IOException e) {
			logger.error("SQLファイル読み込み失敗", e);
		}
		return builder.toString();
	}

	/**
	 * @param sql
	 * @param paramList
	 */
	public static void insertDB(String sql, List<String> paramList) {

		Connection con = null;
		PreparedStatement pstm = null;
		try {
			Context context = new InitialContext();
			DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/postgres");
			con = ds.getConnection();

			pstm = con.prepareStatement(sql);

			// parameter join
			for (int index = 1; index < paramList.size() + 1; index++) {
				pstm.setString(index, paramList.get(index - 1));
			}

			int resultCnt = pstm.executeUpdate();
			logger.info("{}件登録", resultCnt);

		} catch (NamingException | SQLException e) {
			logger.error("DB接続失敗", e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.warn("コネクション削除失敗", e);
				}
			}
			if (pstm != null) {
				try {
					pstm.close();
				} catch (SQLException e) {
					logger.warn("PreparedStatement削除失敗", e);
				}
			}
		}
	}
}
