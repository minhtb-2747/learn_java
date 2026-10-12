package com.socialanalytics.util;

public final class StringUtil {

	private StringUtil() {
	}

	// Chuỗi rỗng/chỉ có khoảng trắng -> null, còn lại trim
	public static String blankToNull(String value) {
		return value == null || value.isBlank() ? null : value.trim();
	}
}
