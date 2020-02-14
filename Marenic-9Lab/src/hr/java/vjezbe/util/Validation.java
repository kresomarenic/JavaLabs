package hr.java.vjezbe.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Validation {

	private final static Logger log = LoggerFactory.getLogger(Validation.class);

	public static boolean isNullOrEmpty(String str) {
		if (str != null && !str.trim().isEmpty())
			return false;
		return true;
	}
	
	

	public static boolean isNumeric(String str) {
		try {			
			Long.parseLong(str.trim());
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	public static boolean checkLength(String str, Integer length) {
		if (str.trim().length() != length)
			return true;
		return false;
	}
	
	
	public static boolean isInRange(String str, Integer start, Integer end) {
		Integer number = Integer.parseInt(str);
		if (number < start && number > end)
			return false;
		return true;
	}

}
