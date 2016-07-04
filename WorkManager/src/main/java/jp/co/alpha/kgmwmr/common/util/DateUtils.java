package jp.co.alpha.kgmwmr.common.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * 日時操作ユーティリティ
 * 
 * @author kigami
 *
 */
public class DateUtils {

	/**
	 * 日付フォーマット
	 */
	private static final DateTimeFormatter dateFormatter = DateTimeFormatter
			.ofPattern("yyyy/M/d");

	/**
	 * 時間フォーマット
	 */
	private static final DateTimeFormatter timeFormatter = DateTimeFormatter
			.ofPattern("HH:mm");

	/**
	 * CSVファイル名用日付フォーマット
	 */
	private static final DateTimeFormatter csvFileNameFormatter = DateTimeFormatter
			.ofPattern("yyyyMMdd");

	/**
	 * プライベートコンストラクタ
	 */
	private DateUtils() {
	}

	/**
	 * 現在日付を取得
	 * 
	 * @return
	 */
	public static String getTodayStr() {
		return LocalDate.now().format(dateFormatter);
	}

	/**
	 * 現在時間を文字列で取得
	 * 
	 * @return
	 */
	public static String getNowTimeStr() {
		return LocalTime.now().truncatedTo(ChronoUnit.SECONDS)
				.format(timeFormatter);
	}

	/**
	 * 現在時間を秒切り捨てで取得
	 * 
	 * @return
	 */
	public static LocalTime getNowTime() {
		String now = LocalTime.now().format(timeFormatter);
		return LocalTime.parse(now).truncatedTo(ChronoUnit.SECONDS);
	}

	/**
	 * 時間を秒切り捨てで取得
	 * 
	 * @param time
	 * @return
	 */
	public static LocalTime getParseTime(LocalTime time) {

		String timeStr = time.format(timeFormatter);
		return LocalTime.parse(timeStr).truncatedTo(ChronoUnit.SECONDS);
	}

	/**
	 * 文字列をLocalTime型に変換
	 * 
	 * @param time
	 * @return
	 */
	public static LocalTime formatLocalTime(String time) {

		return LocalTime.parse(time, timeFormatter);
	}

	/**
	 * 日付フォーマット
	 * 
	 * @param localDate
	 * @return
	 */
	public static String formatDate(LocalDate localDate) {

		return localDate.format(dateFormatter);
	}

	/**
	 * csvファイル名用時間フォーマット
	 * 
	 * @param time
	 * @return
	 */
	public static String csvFormatDate(LocalDate date) {

		return date.format(csvFileNameFormatter);
	}

	/**
	 * 時間をフォーマットして文字列で返却
	 * 
	 * @param time
	 * @return
	 */
	public static String formatTime(LocalTime time) {

		return time.format(timeFormatter);
	}
}
