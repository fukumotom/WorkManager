package test.test.ojt.db.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.test.ojt.common.exception.BusinessException;
import test.test.ojt.common.exception.SystemException;
import test.test.ojt.dao.dto.WorkDto;

public class CommonDbUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(CommonDbUtil.class);

	private static ClassLoader classLoader = CommonDbUtil.class
			.getClassLoader();

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
		InputStream iStream = classLoader
				.getResourceAsStream("/sql/" + sqlName);

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
	public static Map<Integer, String> createSqlMap(StringBuilder sql) {
		// sql文の動的パラメータとパラメータの順番のMapを作成
		String regex = "\\$\\{([a-zA-Z\\d]*)\\}";
		Pattern ptm = Pattern.compile(regex);

		// SQL文からパラメータ代入箇所を取得
		HashMap<Integer, String> sqlParamMap = new HashMap<>();
		Matcher mat = ptm.matcher(sql);
		int index = 0;
		while (mat.find()) {
			index++;
			String sqlParam = mat.group(1);

			sqlParamMap.put(index, sqlParam);
		}
		String convertQuery = mat.replaceAll("?");
		// クエリに置換
		sql.replace(0, sql.length(), convertQuery);

		for (Entry<Integer, String> entry : sqlParamMap.entrySet()) {
			logger.info("sqlMap内容[{}]:{}", entry.getKey(), entry.getValue());
		}
		return sqlParamMap;
	}

	/**
	 * @param sql
	 * @param paramMap
	 */
	public static void insertUsers(String sql,
			HashMap<Integer, Object> paramMap) {

		DataSource ds = lookup();
		try (Connection con = ds.getConnection();
				PreparedStatement pstm = con.prepareStatement(sql);) {

			logger.info("発行SQL:{}", sql);

			// parameter join
			bindParam(pstm, paramMap);

			int resultCnt = pstm.executeUpdate();
			logger.info("{}件登録", resultCnt);

		} catch (SQLException e) {
			logger.error("DB接続失敗", e);
			throw new SystemException(e);
		}
	}

	// TODO 作業登録処理用
	public static WorkDto findOne(String sql, ArrayList<String> paramsList) {

		WorkDto work = new WorkDto();
		DataSource ds = lookup();
		try (Connection con = ds.getConnection();
				PreparedStatement pstm = con.prepareStatement(sql);) {

			// 引数バインド
			pstm.setString(1, paramsList.get(0));
			ResultSet result = pstm.executeQuery();

			// マッピング
			String contents = null;
			String note = null;
			int resultcnt = 0;
			while (result.next()) {
				resultcnt++;
				contents = result.getString("contents");
				note = result.getString("note");
			}
			if (resultcnt == 0) {
				logger.info("仕掛処理なし");
				work = null;
			} else if (resultcnt > 1) {
				throw new SystemException("仕掛作業が複数あります");
			} else {
				work.setContents(contents);
				work.setNote(note);
				logger.info("取得した内容:{} 備考:{}", contents, note);
			}

		} catch (SQLException e) {

			logger.error("DB接続失敗", e);
			throw new SystemException(e);
		}

		return work;
	}

	public static List<WorkDto> findAllWork(String sql,
			HashMap<Integer, Object> paramMap) throws SystemException {

		DataSource ds = lookup();
		ArrayList<WorkDto> dtoList;
		try (Connection con = ds.getConnection();
				PreparedStatement pstm = con.prepareStatement(sql);) {

			logger.info("発行SQL:{}", sql);

			// bindParam
			bindParam(pstm, paramMap);

			ResultSet result = pstm.executeQuery();

			dtoList = resultSetToWorkDtoList(result);

		} catch (SQLException e) {
			logger.error("DB接続失敗", e);
			throw new SystemException(e);
		}

		return dtoList;
	}

	public static void insertWork(String sql,
			HashMap<Integer, Object> paramMap) {

		DataSource ds = lookup();

		try (Connection con = ds.getConnection();
				PreparedStatement pstm = con.prepareStatement(sql);) {

			bindParam(pstm, paramMap);

			int resultCnt = pstm.executeUpdate();
			logger.info("{}件挿入", resultCnt);

		} catch (SQLException e) {
			logger.error("DB接続失敗", e);
			throw new SystemException(e);
		}

	}

	public static LocalTime findTime(String sql,
			HashMap<Integer, Object> paramMap, String column)
			throws BusinessException, SystemException {

		LocalTime time = null;
		DataSource ds = lookup();
		try (Connection con = ds.getConnection();
				PreparedStatement pstm = con.prepareStatement(sql);) {

			logger.info("発行SQL:{}", sql);

			// bindParam;
			bindParam(pstm, paramMap);

			// DB検索
			ResultSet result = pstm.executeQuery();

			int resultcnt = 0;
			while (result.next()) {
				resultcnt++;
				if ("end_time".equals(column)
						&& result.getTime(column) == null) {
					throw new BusinessException("作業中の下に追加はできません。");
				}
				time = result.getTime(column).toLocalTime();
			}
			if (resultcnt != 1) {
				throw new BusinessException("選択した作業の開始または終了時間を取得できませんでした。");
			}

		} catch (SQLException e) {
			logger.error("DB更新失敗", e);
			throw new SystemException(e);
		}

		return time;
	}

	public static void deleteWork(String sql, HashMap<Integer, Object> paramMap)
			throws BusinessException {

		DataSource ds = lookup();
		try (Connection con = ds.getConnection();
				PreparedStatement pstm = con.prepareStatement(sql);) {

			logger.info("発行SQL:{}", sql);

			// bindParam;
			bindParam(pstm, paramMap);

			int resultCnt = pstm.executeUpdate();

			logger.info("{}件削除フラグ更新", resultCnt);

			if (resultCnt == 0) {
				throw new BusinessException("データは削除されています。");
			} else if (resultCnt > 1) {
				throw new SystemException("削除が正常に行われませんでした。");
			}

		} catch (SQLException e) {
			logger.error("DB更新失敗", e);
			throw new SystemException(e);
		}

	}

	/**
	 * dtoに変換したパラメータをSQL文に補完する。
	 * 
	 * @param pstm
	 *            クエリーSQL文
	 * @param paramMap
	 *            補完用パラメータ
	 * @throws SQLException
	 */
	private static void bindParam(PreparedStatement pstm,
			HashMap<Integer, Object> paramMap) throws SQLException {

		for (Entry<Integer, Object> entry : paramMap.entrySet()) {

			Object value = entry.getValue();

			if (value instanceof String) {
				pstm.setString(entry.getKey(), (String) entry.getValue());
			} else if (value instanceof Integer) {
				pstm.setInt(entry.getKey(),
						((Integer) entry.getValue()).intValue());
			} else if (value instanceof Time) {
				pstm.setTime(entry.getKey(), (Time) entry.getValue());
			} else if (value instanceof Date) {
				pstm.setDate(entry.getKey(), (Date) entry.getValue());
			} else {
				// 想定外の型は一律String型に置換
				pstm.setString(entry.getKey(), entry.getValue().toString());
			}
		}
	}

	/**
	 * JNDIによりデータソースを取得
	 * 
	 * @return
	 */
	private static DataSource lookup() {

		DataSource ds = null;
		try {
			Context context = new InitialContext();
			ds = (DataSource) context.lookup("java:comp/env/jdbc/postgres");
		} catch (NamingException e) {
			logger.error("JNDI接続エラー:{}", e);
			throw new SystemException(e);
		}
		return ds;
	}

	/**
	 * SQL実行結果をDtoに詰め替える
	 * 
	 * @param result
	 * @return
	 * @throws SQLException
	 */
	private static ArrayList<WorkDto> resultSetToWorkDtoList(ResultSet result)
			throws SQLException {

		ArrayList<WorkDto> dtoList = new ArrayList<>();
		while (result.next()) {
			WorkDto dto = new WorkDto();
			dto.setId((Integer) (result.getObject("id")));
			dto.setStartTime(result.getTime("start_time"));
			dto.setEndTime(result.getTime("end_time"));
			dto.setWorkingTime(result.getTime("working_time"));
			dto.setContents(result.getString("contents"));
			dto.setNote(result.getString("note"));
			dtoList.add(dto);
		}
		return dtoList;
	}

}
