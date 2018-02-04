package com.sooraj.transaction.statistics.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Instant;

import org.junit.Test;

public class StatisticUtilTest {

	
	@Test
	public void isWithinOneMinuteTest() {
		long epochCurrentMilli = Instant.now().toEpochMilli();
		boolean val = StatisticUtil.isWithinOneMinute(epochCurrentMilli, epochCurrentMilli + 70000);
		assertFalse(val);
	}
	
	@Test
	public void isWithinOneMinutePastTest() {
		long epochCurrentMilli = Instant.now().toEpochMilli();
		boolean val = StatisticUtil.isWithinOneMinute(epochCurrentMilli, epochCurrentMilli - 70000);
		assertFalse(val);
	}
	
	@Test
	public void isWithinOneMinuteTrueTest() {
		long epochCurrentMilli = Instant.now().toEpochMilli();
		boolean val = StatisticUtil.isWithinOneMinute(epochCurrentMilli, epochCurrentMilli - 100);
		assertTrue(val);
	}
	
	@Test
	public void isWithinOneMinuteTrueFutureTest() {
		long epochCurrentMilli = Instant.now().toEpochMilli();
		boolean val = StatisticUtil.isWithinOneMinute(epochCurrentMilli, epochCurrentMilli + 100);
		assertTrue(val);
	}
}
