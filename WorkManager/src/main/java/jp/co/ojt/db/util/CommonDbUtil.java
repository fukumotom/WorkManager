package jp.co.ojt.db.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.ojt.dao.dto.WorkDto;

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
				builder.append("\n");
			}

		} catch (IOException e) {
			logger.error("SQLファイル読み込み失敗", e);
		}
		logger.info("発行SQL：{}", builder.toString());
		return builder.toString();
	}

	/**
	 * @param sql
	 * @param paramList
	 */
	public static void insertDB(String sql, List<?> paramList) {

		DataSource ds = lookup();
		try (Connection con = ds.getConnection(); PreparedStatement pstm = con.prepareStatement(sql);) {

			// parameter join
			// bindParam(pstm, paramList);

			int resultCnt = pstm.executeUpdate();
			logger.info("{}件登録", resultCnt);

		} catch (SQLException e) {
			logger.error("DB接続失敗", e);
		}
	}

	public static ArrayList<WorkDto> findAllWork(String sql, WorkDto dto) {

		DataSource ds = lookup();
		try (Connection con = ds.getConnection(); PreparedStatement pstm = con.prepareStatement(sql);) {

			// createParam
			ArrayList<Object> paramList = new ArrayList<>();
			paramList.add(0, dto.getUserName());

		} catch (SQLException e) {
			logger.error("DB接続失敗", e);
		}

		return null;
	}

	// TODO
	// private static ArrayList<Object> bindParam(PreparedStatement pstm,
	// WorkDto dto) throws SQLException {
	//
	// int index = 1;
	// if (dto.getUserName() != null) {
	// pstm.setString(index, dto.getUserName());
	// index++;
	// } else if (dto.getId() != 0) {
	// pstm.setInt(index, dto.getId() );
	// index++;
	// } else if (dto.) {
	// pstm.setTime(index, (Date)obj);(index, (int) obj);
	// }
	// }

	private static DataSource lookup() {

		DataSource ds = null;
		try {
			Context context = new InitialContext();
			ds = (DataSource) context.lookup("java:comp/env/jdbc/postgres");
		} catch (NamingException e) {
			logger.error("JNDI接続エラー:{}", e);
		}
		return ds;
	}

}
