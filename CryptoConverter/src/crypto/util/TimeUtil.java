package crypto.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeUtil {

	public static LocalDate timestampToDate(long epoch) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneId.systemDefault()).toLocalDate();
	}
	
	public static long dateToTimestamp(LocalDate date) {
		return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
	
	public static long getCurrentTimestamp() {
		return LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
	
}
