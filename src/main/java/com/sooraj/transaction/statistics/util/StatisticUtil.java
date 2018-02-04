package com.sooraj.transaction.statistics.util;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class StatisticUtil {

	public static boolean isWithinOneMinute(long currentTimeMilliseconds, long timeInMilliseconds) {
		/*return (currentTimeMilliseconds - timeInMilliseconds) / 1000 <= 60;*/
		
		Duration age = Duration.of(60, ChronoUnit.SECONDS);
		return Math.abs(currentTimeMilliseconds - timeInMilliseconds) <= age.toMillis();
	}

	public static boolean isWithinOneMinute(long timeInMilliseconds) {
		return isWithinOneMinute(Instant.now().toEpochMilli(), timeInMilliseconds);
	}
}
