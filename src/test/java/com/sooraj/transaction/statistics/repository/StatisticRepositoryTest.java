package com.sooraj.transaction.statistics.repository;

import java.time.Instant;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.sooraj.transaction.statistics.exception.OutOfTimeRangeException;
import com.sooraj.transaction.statistics.model.Statistic;
import com.sooraj.transaction.statistics.model.Transaction;

public class StatisticRepositoryTest {
	
	private StatisticRepository statisticRepo;

    @Before
    public void setUp() throws Exception {
    	statisticRepo = new StatisticRepository();
    }
    
	@Test
    public void addCurrentTimeSingleValueTest() throws Exception {
		long epochCurrentMilli = Instant.now().toEpochMilli();
		statisticRepo.update(new Transaction(101.0d,epochCurrentMilli));

        assertThat(statisticRepo.getStatistics().getSum(), is(101.0d));
    }

    @Test
    public void addCurrentTimeMultipleValuesTest() throws Exception {
    	long epochCurrentMilli = Instant.now().toEpochMilli();
    	statisticRepo.update(new Transaction(301.0d,epochCurrentMilli - 4));
    	statisticRepo.update(new Transaction(201.0d,epochCurrentMilli));
    	statisticRepo.update(new Transaction(101.0d,epochCurrentMilli));
    	
    	assertThat(statisticRepo.getStatistics().getSum(), is(603.0d));
    }

    @Test(expected = OutOfTimeRangeException.class)
    public void outOfTimeRangeTest() throws Exception {
    	long epochCurrentMilli = Instant.now().toEpochMilli();
    	statisticRepo.update(new Transaction(301.0d,epochCurrentMilli - 70000));
    }

    @Test(expected = OutOfTimeRangeException.class)
    public void outOfTimeRangeFutureTest() throws Exception {
    	long epochCurrentMilli = Instant.now().toEpochMilli();
    	statisticRepo.update(new Transaction(301.0d,epochCurrentMilli + 70000));
    }

    @Test
    public void zeroSumTest() throws Exception {
        double value = statisticRepo.getStatistics().getSum();

        assertThat(value, is(0.0d));
    }
    
    @Test
    public void zeroAvgTest() throws Exception {
        double value = statisticRepo.getStatistics().getAvg();

        assertThat(value, is(0.0d));
    }

    @Test
    public void OldAndNewTransactionTest() throws Exception {
    	
    	long epochCurrentMilli = Instant.now().toEpochMilli();
    	statisticRepo.update(new Transaction(200.0d,epochCurrentMilli));
    	statisticRepo.update(new Transaction(300.0d,epochCurrentMilli));
    	
    	Thread.currentThread().sleep(65000);
    	
    	epochCurrentMilli = Instant.now().toEpochMilli();
    	statisticRepo.update(new Transaction(20.0d,epochCurrentMilli));
    	statisticRepo.update(new Transaction(15.0d,epochCurrentMilli));
    	statisticRepo.update(new Transaction(34.0d,epochCurrentMilli));
    	
    	Statistic stat = statisticRepo.getStatistics();

        assertThat(stat.getSum(), is(69.0d));
        assertThat(stat.getCount(), is(3L));
        assertThat(stat.getMax(), is(34.0d));
        assertThat(stat.getMin(), is(15.0d));
        assertThat(stat.getAvg(), is(23.0d));
        
    }

}
