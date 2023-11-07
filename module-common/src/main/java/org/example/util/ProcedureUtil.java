package org.example.util;

import java.time.format.DateTimeFormatter;

public class ProcedureUtil {

	public static DateTimeFormatter getDateTimeFormatter() {
		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
	}

}
