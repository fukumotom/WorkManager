package jp.co.ojt.common.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {

	private static final DateTimeFormatter dateFormatter = DateTimeFormatter
			.ofPattern("yyyy/MM/dd");

	public static String getTodayStr() {
		return LocalDate.now().format(dateFormatter);
	}

}
