package test.test.ojt.common.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {

	private static final DateTimeFormatter dateFormatter = DateTimeFormatter
			.ofPattern("yyyy/M/d");

	public static String getTodayStr() {
		return LocalDate.now().format(dateFormatter);
	}

}
