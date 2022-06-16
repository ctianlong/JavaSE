package jdk8.newtimedate;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Set;

import org.junit.Test;

/**
 * 
 * @author Tianlong
 *
 */
public class TestLocalDateTime {

	@Test
	public void test8() {
		LocalDateTime time = LocalDateTime.parse("2021-12-13 11:42:38", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//		LocalDateTime time = LocalDateTime.now();
//		System.out.println(time.toEpochSecond(ZoneOffset.of("+8")));
//		System.out.println(time.toInstant(ZoneOffset.of("+8")).toEpochMilli());
		System.out.println(string2Long("2021-12-13 11:42:38", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
	}

	public static long string2Long(String time, DateTimeFormatter formatter) {
		LocalDateTime localDateTime = LocalDateTime.parse(time, formatter);
		return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
	}
	
	//6. ZoneDate  ZoneTime  ZoneDateTime  时区
	@Test
	public void test7() {
		Set<String> set = ZoneId.getAvailableZoneIds();
		set.forEach(System.out::println);
		
		LocalDateTime ldt = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
		System.out.println(ldt);
		
		LocalDateTime ldt2 = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
		ZonedDateTime zdt = ldt2.atZone(ZoneId.of("Asia/Shanghai"));
		System.out.println(zdt);
		
	}
	
	
	//5. DateTimeFormatter: 格式化时间/日期
	@Test
	public void test6 () {
		DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE;
		LocalDateTime ldt = LocalDateTime.now();
		
		String strDate = ldt.format(dtf);
		System.out.println(strDate);
		
		System.out.println("--------------------");
		
		DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");
		String strDate2 = dtf2.format(ldt);
		System.out.println(strDate2);
		
		LocalDateTime newDate = LocalDateTime.parse(strDate2, dtf2);
		System.out.println(newDate);
	}
	
	
	//4. TemporalAdjuster : 时间校正器
	@Test
	public void test5 () {
		LocalDateTime ldt = LocalDateTime.now();
		System.out.println(ldt);
		
		LocalDateTime ldt2 = ldt.withDayOfMonth(10);
		System.out.println(ldt2);
		
		LocalDateTime ldt3 = ldt.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
		System.out.println(ldt3);
		
		//自定义：下一个工作日
		LocalDateTime ldt5 = ldt.with((l) -> {
			LocalDateTime ldt4 = (LocalDateTime) l;
			DayOfWeek dow = ldt4.getDayOfWeek();
			if (dow.equals(DayOfWeek.FRIDAY)) {
				return ldt4.plusDays(3);
			} else if (dow.equals(DayOfWeek.SATURDAY)) {
				return ldt4.plusDays(2);
			} else {
				return ldt4.plusDays(1);
			}
		});
		System.out.println(ldt5);
		
	}
	
	
	
	//3. Duration:计算两个“时间”之间的间隔
	//   Period:计算两个“日期”之间的间隔
	@Test
	public void test4 () {
		LocalDate ld1 = LocalDate.of(2016, 2, 11);
		LocalDate ld2 = LocalDate.now();
		
		Period period = Period.between(ld1, ld2);
		System.out.println(period);
		System.out.println(period.getYears());
		System.out.println(period.getMonths());
		System.out.println(period.getDays());
	}
	
	@Test
	public void test3 () {
		Instant ins1 = Instant.now();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Instant ins2 = Instant.now();
		Duration duration = Duration.between(ins1, ins2);
		System.out.println(duration.toMillis());
		
		System.out.println("-----------------------");
		
		LocalTime lt1 = LocalTime.now();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		LocalTime lt2 = LocalTime.now();
		System.out.println(Duration.between(lt1, lt2).toMillis());
		
		
	}
	
	
	//2. Instant:时间戳(以Unix 元年： 1970年1月1日 00:00:00 到某个时间之间的毫秒值)
	@Test
	public void test2 () {
		Instant ins1 = Instant.now(); // 默认获取 UTC 时区，与中国差 8 个小时
		System.out.println(ins1);
		
		OffsetDateTime odt = ins1.atOffset(ZoneOffset.ofHours(8));
		System.out.println(odt);
		
		System.out.println(ins1.toEpochMilli());
		System.out.println(System.currentTimeMillis());
		
		Instant ins2 = Instant.ofEpochSecond(600);
		System.out.println(ins2);
	}
	
	
	//1. LocalDate  LocalTime LocalDateTime  方便人阅读  三者使用方式相同
	@Test
	public void test1 () {
		LocalDateTime ldt = LocalDateTime.now();
		System.out.println(ldt);
		
		LocalDateTime ldt2 = LocalDateTime.of(2017, 5, 20, 13, 45, 55);
		System.out.println(ldt2);
		
		LocalDateTime ldt3 = ldt.plusYears(2);
		System.out.println(ldt3);
		
		LocalDateTime ldt4 = ldt.minusMonths(2);
		System.out.println(ldt4);
		
		System.out.println(ldt.getYear());
		System.out.println(ldt.getMonthValue());
		System.out.println(ldt.getDayOfMonth());
		System.out.println(ldt.getHour());
		System.out.println(ldt.getMinute());
		System.out.println(ldt.getSecond());
	}
	
	
}
