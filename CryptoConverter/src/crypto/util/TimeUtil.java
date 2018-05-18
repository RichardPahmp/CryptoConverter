package crypto.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeUtil {

	/**
	 * Converts the given unix timestamp to a localdate object
	 * @param epoch The timestamp
	 * @return
	 */
	public static LocalDate timestampToDate(long epoch) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneId.systemDefault()).toLocalDate();
	}
	
	/**
	 * Convert a LocalDate object to a unix timestamp
	 * @param date
	 * @return
	 */
	public static long dateToTimestamp(LocalDate date) {
		return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
	
	/**
	 * Get a timestamp for the current system time
	 * @return
	 */
	public static long getCurrentTimestamp() {
		return LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
	
}
