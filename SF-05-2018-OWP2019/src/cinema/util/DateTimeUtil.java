package cinema.util;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class DateTimeUtil {
	
	public static LocalDate UnixTimeStampToLocalDate(int timeStamp) {
		
		Date date = new Date((long)timeStamp*1000);
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
		
	}
	
	public static int LocalDateToUnixTimeStamp(LocalDate localDate) {
		
		ZoneId zoneId = ZoneId.systemDefault();
		long timeStamp = localDate.atStartOfDay(zoneId).toEpochSecond();
		return (int)timeStamp;
		
	}

}
