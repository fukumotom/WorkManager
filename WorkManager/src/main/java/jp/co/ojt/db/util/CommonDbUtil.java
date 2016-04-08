package jp.co.ojt.db.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	public static StringBuilder readSql(String sqlName) {

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
		logger.info("読み込みSQL：{}", builder.toString());
		return builder;
	}

	/**
	 * sql文からパラメータ用Map作成
	 * 
	 * @param sql
	 * @return
	 */
	public static HashMap<String, Integer> createSqlMap(StringBuilder sql) {
		// sql文の動的パラメータとパラメータの順番のMapを作成
		String regex = "\\$\\{([a-zA-Z\\d]*)\\}";
		Pattern ptm = Pattern.compile(regex);

		// SQL文からパラメータ代入箇所を取得
		HashMap<String, Integer> sqlParamMap = new HashMap<>();
		Matcher mat = ptm.matcher(sql);
		int index = 0;
		while (mat.find()) {
			index++;
			String sqlParam = mat.group(1);

			sqlParamMap.put(sqlParam, index);
		}
		String convertQuery = mat.replaceAll("?");
		// クエリに置換
		sql.replace(0, sql.length(), convertQuery);

		for (String key : sqlParamMap.keySet()) {
			logger.info("Map内容[{}]:{}", key, sqlParamMap.get(key));
		}
		return sqlParamMap;
	}

	/**
	 * @param sql
	 * @param paramMap
	 */
	public static void insertUsers(String sql, HashMap<Integer, Object> paramMap) {

		DataSource ds = lookup();
		try (Connection con = ds.getConnection(); PreparedStatement pstm = con.prepareStatement(sql);) {

			logger.info("発行SQL:{}", sql);

			// parameter join
			bindParam(pstm, paramMap);

			int resultCnt = pstm.executeUpdate();
			logger.info("{}件登録", resultCnt);

		} catch (SQLException e) {
			logger.error("DB接続失敗", e);
		}
	}

	public static ArrayList<WorkDto> findAllWork(String sql, HashMap<Integer, Object> paramMap) {

		ArrayList<WorkDto> dtoList = new ArrayList<>();

		DataSource ds = lookup();
		try (Connection con = ds.getConnection(); PreparedStatement pstm = con.prepareStatement(sql);) {

			logger.info("発行SQL:{}", sql);

			// bindParam
			bindParam(pstm, paramMap);

			ResultSet result = pstm.executeQuery();

			while (result.next()) {
				dtoList.add(getWorkresult(result));
			}

		} catch (SQLException e) {
			logger.error("DB接続失敗", e);
		}

		return dtoList;
	}

	private static void bindParam(PreparedStatement pstm, HashMap<Integer, Object> paramMap) throws SQLException {

		for (Integer parameterIndex : paramMap.keySet()) {

			Object value = paramMap.get(parameterIndex);

			if (value instanceof String) {
				pstm.setString(parameterIndex, (String) value);
			} else if (value instanceof Integer) {
				pstm.setInt(parameterIndex, ((Integer) value).intValue());
			} else if (value instanceof Date) {
				pstm.setTime(parameterIndex, (Time) value);
			} else {
				pstm.setObject(parameterIndex, value.toString());
			}
		}
	}

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

	private static WorkDto getWorkresult(ResultSet result) throws SQLException {

		WorkDto dto = new WorkDto();
		dto.setId((Integer) (result.getObject("id")));
		dto.setStartTime(result.getTime("startTime"));
		dto.setEndTime(result.getTime("endTime"));
		dto.setWorkingTime(result.getTime("workingTime"));
		dto.setContents(result.getString("contents"));
		dto.setNote(result.getString("note"));
		return dto;
	}
}
