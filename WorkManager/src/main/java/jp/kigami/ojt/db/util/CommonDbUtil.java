package jp.kigami.ojt.db.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
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

import jp.kigami.ojt.common.exception.BusinessException;
import jp.kigami.ojt.common.exception.SystemException;
import jp.kigami.ojt.dao.dto.WorkDto;

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
	 * dtoに変換したパラメータをSQL文に補完する。
	 * 
	 * @param pstm
	 *            クエリーSQL文
	 * @param paramMap
	 *            補完用パラメータ
	 * @throws SQLException
	 */
	private static void bindParam(PreparedStatement pstm,
			Map<Integer, Object> paramMap) throws SQLException {

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
	 * SQL実行結果をDtoに詰め替える
	 * 
	 * @param <T>
	 * 
	 * @param result
	 * @param dtoClass
	 * @return
	 * @throws SQLException
	 */
	private static <T> ArrayList<T> resultSetToWorkDtoList(ResultSet result,
			Class<T> dtoClass) throws SQLException {

		HashMap<String, String> clmNameMap = new HashMap<String, String>();
		ResultSetMetaData meta = result.getMetaData();
		for (int i = 1; i < meta.getColumnCount() + 1; i++) {

			meta.getColumnType(i);
			clmNameMap.put(meta.getColumnLabel(i), meta.getColumnClassName(i));
		}

		ArrayList<T> dtoList = new ArrayList<>();

		while (result.next()) {
			T dto;
			try {
				dto = dtoClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e1) {
				logger.error("DTOのリフレクションに失敗");
				throw new SystemException(e1);
			}
			for (Entry<String, String> entry : clmNameMap.entrySet()) {
				String label = entry.getKey();
				String typeStr = entry.getValue();
				logger.info("clmNameMap内容[ラベル]:型     [{}]:{}", label, typeStr);

				// ラベルからDB結果を取得
				Object obj;
				if (typeStr.contains("Integer")) {
					obj = result.getInt(label);
				} else if (typeStr.contains("Time")) {
					obj = result.getTime(label);
				} else {
					obj = result.getString(label);
				}

				// setterを取得
				Method setter = createSetter(label, dtoClass);

				try {
					setter.invoke(dto, obj);

				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					logger.error("DBのバインドに失敗");
					throw new SystemException(e);
				}
			}
			dtoList.add(dto);
		}
		return dtoList;
	}

	private static <T> Method createSetter(String label, Class<T> dtoClass) {

		// ラベルからsetter文字列を作成
		String[] pieces = label.split("_");
		StringBuilder setterStr = new StringBuilder();

		for (int i = 0; i < pieces.length; i++) {
			if (i == 0) {
				setterStr.append("set");
			}
			setterStr.append(pieces[i].substring(0, 1).toUpperCase());
			setterStr.append(pieces[i].substring(1));
		}

		Method[] methods = dtoClass.getDeclaredMethods();
		Method setter = null;
		for (Method tmpSetter : methods) {
			if (tmpSetter.getName().equals(setterStr.toString())) {
				setter = tmpSetter;
				break;
			}
		}
		if (setter == null) {
			logger.warn("{}のsetterが見つかりませんでした。", setterStr);
		}
		return setter;
	}

	public static <T> HashMap<String, Object> createDtoMap(T dto,
			Class<T> dtoClass) {

		// Dtoのフィールド名と値のMapを作成
		String regex = "get(([A-Z][a-zA-Z\\d]*))";
		Pattern ptm = Pattern.compile(regex);

		// Dtoのgetterからフィールド名を取得
		Method[] methods = dtoClass.getDeclaredMethods();
		HashMap<String, Object> dtoMap = new HashMap<>();

		for (Method method : methods) {
			// getterを抽出
			Matcher mat = ptm.matcher(method.getName());
			if (mat.find()) {
				String getter = method.getName();
				if (!getter.contains("Class")) {
					String fieldName = mat.group(1);
					fieldName = fieldName.substring(0, 1).toLowerCase()
							+ fieldName.substring(1);

					Object value = null;
					try {
						value = method.invoke(dto);
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						logger.info("リフレクション失敗", e);
					}
					dtoMap.put(fieldName, value);
				}
			}
		}

		for (Entry<String, Object> entry : dtoMap.entrySet()) {
			logger.info("DtoMap内容[{}]:{}", entry.getKey(), entry.getValue());
		}
		return dtoMap;
	}

	/**
	 * ユーザ登録処理実行
	 * 
	 * @param sql
	 * @param paramMap
	 */
	public static void insertUsers(String sql, Map<Integer, Object> paramMap) {

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

	// 作業登録処理用
	public static List<WorkDto> findWorking(String sql,
			Map<Integer, Object> paramMap) {

		ArrayList<WorkDto> workDtoList = new ArrayList<>();
		DataSource ds = lookup();
		try (Connection con = ds.getConnection();
				PreparedStatement pstm = con.prepareStatement(sql);) {

			logger.info("発行SQL:{}", sql);

			// parameter join
			bindParam(pstm, paramMap);
			ResultSet result = pstm.executeQuery();

			// マッピング
			while (result.next()) {
				WorkDto workDto = new WorkDto();
				workDto.setId(result.getInt("id"));
				workDto.setStartTime(result.getTime("start_time"));
				workDto.setContents(result.getString("contents"));
				workDto.setNote(result.getString("note"));
				workDtoList.add(workDto);
			}

		} catch (SQLException e) {
			logger.error("DB接続失敗", e);
			throw new SystemException(e);
		}

		return workDtoList;
	}

	/**
	 * 作業終了処理
	 * 
	 * @param string
	 * @param paramMap
	 * @return
	 * @throws BusinessException
	 */
	public static int finishWork(String sql, Map<Integer, Object> paramMap) {

		int resultCnt = 0;
		DataSource ds = lookup();
		try (Connection con = ds.getConnection();
				PreparedStatement pstm = con.prepareStatement(sql)) {

			logger.info("発行SQL:{}", sql);

			// parameter join
			bindParam(pstm, paramMap);
			resultCnt = pstm.executeUpdate();

		} catch (SQLException e) {
			logger.error("DB接続失敗");
			throw new SystemException(e);
		}

		return resultCnt;
	}

	public static int startWork(String sql, Map<Integer, Object> paramMap) {

		DataSource ds = lookup();
		int resultCnt = 0;
		try (Connection con = ds.getConnection();
				PreparedStatement pstm = con.prepareStatement(sql)) {

			bindParam(pstm, paramMap);
			resultCnt = pstm.executeUpdate();

		} catch (SQLException e) {
			logger.error("DB接続失敗", e);
			throw new SystemException(e);
		}

		return resultCnt;
	}

	public static List<WorkDto> findAllWork(String sql,
			Map<Integer, Object> paramMap) {

		DataSource ds = lookup();
		ArrayList<WorkDto> dtoList;
		try (Connection con = ds.getConnection();
				PreparedStatement pstm = con.prepareStatement(sql);) {

			logger.info("発行SQL:{}", sql);

			// bindParam
			bindParam(pstm, paramMap);

			ResultSet result = pstm.executeQuery();

			dtoList = resultSetToWorkDtoList(result, WorkDto.class);

		} catch (SQLException e) {
			logger.error("DB接続失敗", e);
			throw new SystemException(e);
		}

		return dtoList;
	}

	public static void insertWork(String sql, Map<Integer, Object> paramMap) {

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

	/**
	 * 引数で指定したカラム（開始or終了時間）を取得
	 * 
	 * @param sql
	 * @param paramMap
	 * @param column
	 * @return
	 */
	public static WorkDto findTime(String sql, Map<Integer, Object> paramMap,
			String column) {

		ArrayList<WorkDto> dtoList = null;
		DataSource ds = lookup();
		try (Connection con = ds.getConnection();
				PreparedStatement pstm = con.prepareStatement(sql);) {

			logger.info("発行SQL:{}", sql);

			// bindParam;
			bindParam(pstm, paramMap);

			// DB検索
			ResultSet result = pstm.executeQuery();
			dtoList = resultSetToWorkDtoList(result, WorkDto.class);

			if (dtoList.size() != 1) {
				throw new SystemException("選択した作業の開始または終了時間を取得できませんでした。");
			}

		} catch (SQLException e) {
			logger.error("DB更新失敗", e);
			throw new SystemException(e);
		}

		return dtoList.get(0);
	}

	public static void deleteWork(String sql, Map<Integer, Object> paramMap)
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

	public static WorkDto findEditWork(String sql,
			HashMap<Integer, Object> paramMap) {

		WorkDto dto = new WorkDto();
		DataSource ds = lookup();
		try (Connection con = ds.getConnection();
				PreparedStatement pstm = con.prepareStatement(sql)) {

			logger.info("発行SQL:{}", sql);

			bindParam(pstm, paramMap);

			ResultSet result = pstm.executeQuery();
			ArrayList<WorkDto> dtolist = resultSetToWorkDtoList(result,
					WorkDto.class);
			dto = dtolist.get(0);

		} catch (SQLException e) {
			logger.error("編集作業取得失敗", e);
			throw new SystemException(e);
		}

		return dto;
	}

	public static void updataWork(String sql,
			HashMap<Integer, Object> paramMap) {

		DataSource ds = lookup();
		try (Connection con = ds.getConnection();
				PreparedStatement pstm = con.prepareStatement(sql);) {

			logger.info("発行SQL:{}", sql);

			bindParam(pstm, paramMap);

			pstm.executeUpdate();

		} catch (SQLException e) {
			logger.error("編集作業失敗", e);
			throw new SystemException(e);
		}

	}

	public static void saveWork(String sql, HashMap<Integer, Object> paramMap) {
		DataSource ds = lookup();
		try (Connection con = ds.getConnection();
				PreparedStatement pstm = con.prepareStatement(sql);) {

			logger.info("発行SQL:{}", sql);

			bindParam(pstm, paramMap);

			pstm.executeUpdate();

		} catch (SQLException e) {
			logger.error("編集作業失敗", e);
			throw new SystemException(e);
		}
	}

	/**
	 * 未保存作業削除SQL発行
	 * 
	 * @param string
	 * @param paramMap
	 */
	public static void deleteUnSaveWork(String sql,
			HashMap<Integer, Object> paramMap) {

		DataSource ds = lookup();
		try (Connection con = ds.getConnection();
				PreparedStatement pstm = con.prepareStatement(sql)) {

			logger.info("発行SQL:{}", sql);

			bindParam(pstm, paramMap);

			pstm.executeUpdate();

		} catch (SQLException e) {
			logger.error("未保存作業削除失敗", e);
			throw new SystemException(e);
		}
	}
}
