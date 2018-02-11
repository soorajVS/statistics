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
    	
		Thread.sleep(65000);
    	
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
    
    @Test
    public void OldAndNewMultiThreadTransactionTest() throws Exception {
    	
    	final long epochCurrentMilliThread1 = Instant.now().toEpochMilli();
        Thread oneThread = new Thread(() ->  {
        	statisticRepo.update(new Transaction(20.0d,epochCurrentMilliThread1));
        	statisticRepo.update(new Transaction(15.0d,epochCurrentMilliThread1));
        	statisticRepo.update(new Transaction(34.0d,epochCurrentMilliThread1));
        });
        Thread twoThread = new Thread(() ->  {
        	statisticRepo.update(new Transaction(20.0d,epochCurrentMilliThread1));
        	statisticRepo.update(new Transaction(15.0d,epochCurrentMilliThread1));
        	statisticRepo.update(new Transaction(34.0d,epochCurrentMilliThread1));
        });
        Thread threeThread = new Thread(() ->  {
        	statisticRepo.update(new Transaction(20.0d,epochCurrentMilliThread1));
        	statisticRepo.update(new Transaction(15.0d,epochCurrentMilliThread1));
        	statisticRepo.update(new Transaction(34.0d,epochCurrentMilliThread1));
        });
        Thread fourThread = new Thread(() ->  {
        	statisticRepo.update(new Transaction(20.0d,epochCurrentMilliThread1));
        	statisticRepo.update(new Transaction(15.0d,epochCurrentMilliThread1));
        	statisticRepo.update(new Transaction(34.0d,epochCurrentMilliThread1));
        });
        
        final long epochCurrentMilliThread2 = epochCurrentMilliThread1 - 1000;
        Thread oneThreadNextSecond = new Thread(() ->  {
        	statisticRepo.update(new Transaction(11.0d,epochCurrentMilliThread2));
        	statisticRepo.update(new Transaction(8.0d,epochCurrentMilliThread2));
        	statisticRepo.update(new Transaction(6.0d,epochCurrentMilliThread2));
        });
        Thread twoThreadNextSecond = new Thread(() ->  {
        	statisticRepo.update(new Transaction(24.0d,epochCurrentMilliThread2));
        	statisticRepo.update(new Transaction(45.0d,epochCurrentMilliThread2));
        	statisticRepo.update(new Transaction(12.0d,epochCurrentMilliThread2));
        });
        Thread threeThreadNextSecond = new Thread(() ->  {
        	statisticRepo.update(new Transaction(13.0d,epochCurrentMilliThread2));
        	statisticRepo.update(new Transaction(16.0d,epochCurrentMilliThread2));
        	statisticRepo.update(new Transaction(19.0d,epochCurrentMilliThread2));
        });
        Thread fourThreadNextSecond = new Thread(() ->  {
        	statisticRepo.update(new Transaction(1.0d,epochCurrentMilliThread2));
        	statisticRepo.update(new Transaction(2.0d,epochCurrentMilliThread2));
        	statisticRepo.update(new Transaction(3.0d,epochCurrentMilliThread2));
        });
        
        oneThread.start();
        twoThread.start();
        threeThread.start();
        fourThread.start();
        oneThreadNextSecond.start();
        twoThreadNextSecond.start();
        threeThreadNextSecond.start();
        fourThreadNextSecond.start();
        
        threeThread.join();
        twoThread.join();
        oneThread.join();
        fourThread.join();
        oneThreadNextSecond.join();
        twoThreadNextSecond.join();
        threeThreadNextSecond.join();
        fourThreadNextSecond.join();
        
        Statistic stat = statisticRepo.getStatistics();

        assertThat(stat.getSum(), is(436.0d));
        assertThat(stat.getCount(), is(24L));
        assertThat(stat.getMax(), is(45.0d));
        assertThat(stat.getMin(), is(1.0d));
        assertThat(stat.getAvg(), is(18.166666666666668d));
    }

}
