package cinema.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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
	
	public static LocalDateTime UnixTimeStampToLocalDateTime(int timeStamp) {
		Date date = new Date((long)timeStamp*1000);
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}
	
	public static int LocalDateTimeToUnixTimeStamp(LocalDateTime localDateTime) {
		ZoneId zoneId = ZoneId.systemDefault();
		long timeStamp = localDateTime.atZone(zoneId).toEpochSecond();
		return (int)timeStamp;
	}
	
	public static LocalDateTime StringToLocalDateTime(String stringDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime dateTime = LocalDateTime.parse(stringDate, formatter);
		System.out.println(dateTime);
		return dateTime;
	}
	
	public static LocalDate StringToLocalDate(String stringDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(stringDate, formatter);
		System.out.println(date);
		return date;
	}

}
