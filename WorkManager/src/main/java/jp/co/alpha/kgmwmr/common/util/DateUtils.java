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
	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
			.ofPattern("yyyy/M/d");

	/**
	 * 時間フォーマット
	 */
	public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter
			.ofPattern("HH:mm");

	/**
	 * CSVファイル名用日付フォーマット
	 */
	private static final DateTimeFormatter CSV_FILENAME_FORMATTER = DateTimeFormatter
			.ofPattern("yyyyMMdd");

	/**
	 * プライベートコンストラクタ
	 */
	private DateUtils() {
	}

	/**
	 * 現在日付を取得
	 * 
	 * @return 現在の日付文字列
	 */
	public static String getTodayStr() {
		return LocalDate.now().format(DATE_FORMATTER);
	}

	/**
	 * 現在時間（文字列）取得
	 * 
	 * @return 現在時間（文字列）
	 */
	public static String getNowTimeStr() {
		return LocalTime.now().truncatedTo(ChronoUnit.SECONDS)
				.format(TIME_FORMATTER);
	}

	/**
	 * 現在時間(秒切り捨て)取得
	 * 
	 * @return 現在時間(秒切り捨て)
	 */
	public static LocalTime getNowTime() {
		return truncatedTime(LocalTime.now());
	}

	/**
	 * 時間(秒切り捨て)取得
	 * 
	 * @return HH:mm形式の時間
	 */
	public static LocalTime truncatedTime(LocalTime time) {

		String timeStr = time.format(TIME_FORMATTER);
		return LocalTime.parse(timeStr).truncatedTo(ChronoUnit.SECONDS);
	}

	/**
	 * 文字列をLocalTime型に変換
	 * 
	 * @return HH:mm形式の時間
	 */
	public static LocalTime getParseTime(String time) {

		return LocalTime.parse(time, TIME_FORMATTER);
	}

	/**
	 * 日付フォーマット
	 * 
	 * @param localDate
	 *            フォーマット対象
	 * @return yyyy/M/d形式の日付
	 */
	public static String formatDate(LocalDate localDate) {

		return localDate.format(DATE_FORMATTER);
	}

	/**
	 * 時間フォーマット
	 * 
	 * @param localTime
	 *            フォーマット対象
	 * @return HH:mm形式の時間
	 */
	public static String formatTime(LocalTime localTime) {

		return localTime.format(TIME_FORMATTER);
	}

	/**
	 * 文字列をLocalDate型に変換
	 * 
	 * @param date
	 *            日付文字列
	 * @return yyyy/M/d形式の日付
	 */
	public static LocalDate getParseDate(String date) {
		return LocalDate.parse(date, DATE_FORMATTER);
	}

	/**
	 * csvファイル名用時間フォーマット
	 * 
	 * @param time
	 *            出力する作業リストの表示時間
	 * @return CSVファイル名
	 */
	public static String csvFormatDate(LocalDate date) {

		return date.format(CSV_FILENAME_FORMATTER);
	}
}
