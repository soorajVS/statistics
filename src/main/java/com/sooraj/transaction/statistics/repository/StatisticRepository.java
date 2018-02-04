package com.sooraj.transaction.statistics.repository;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sooraj.transaction.statistics.exception.OutOfTimeRangeException;
import com.sooraj.transaction.statistics.model.Statistic;
import com.sooraj.transaction.statistics.model.Transaction;

import static java.time.temporal.ChronoUnit.MILLIS;

@Component
public class StatisticRepository {
	
	private static final Logger log = LoggerFactory.getLogger(StatisticRepository.class);

	private final AtomicReferenceArray<Statistic> statisticArray;
	
	public StatisticRepository(){
		statisticArray = new AtomicReferenceArray<Statistic>(60);
	}
	
	public void update(Transaction transaction) {

		int index = checkedIndexFor(transaction.getTimestamp());
		int offset = secondtoArrayIndex(index);
		statisticArray.updateAndGet(offset, value -> addToStatistic(transaction.getAmount(), index, value));
	}

	public void update(long timestamp, double amount) {

		int index = checkedIndexFor(timestamp);
		int offset = secondtoArrayIndex(index);
		statisticArray.updateAndGet(offset, value -> addToStatistic(amount, index, value));
	}
    
	public Statistic getStatistics() {

		return getCurrentTimeFrameStatStream().reduce(
				Statistic.builder().count(0).sum(0).min(0).max(0).secondIndex(0).build(), Statistic::addStatistic);
	}
    
	private int checkedIndexFor(long timestamp) {
		log.debug("checkedIndexFor :" + timestamp);
		long now = Instant.now().toEpochMilli();

		int minusOneMinuteIndex = getMinusOneMinuteIndex(now);
		int upperIndex = getcurrentIndex(now);

		int index = getcurrentIndex(timestamp);

		if (index <= minusOneMinuteIndex || index > upperIndex) {
			throw new OutOfTimeRangeException("Time not within one minute");
		}
		return index;
	}
    
	private Statistic addToStatistic(double amount, int index, Statistic value) {
		if (value == null || value.getSecondIndex() < index) {
			return Statistic.builder().count(1).sum(amount).min(amount).max(amount).secondIndex(index).build();
		} else {
			return value.addTransaction(amount);
		}
	}
	   
    private int secondtoArrayIndex(int index) {
        return index % statisticArray.length();
    }

   
    private int getcurrentIndex(long timestamp) {
        return (int) Duration.of(timestamp, MILLIS).get(ChronoUnit.SECONDS);
    }

   
    private int getMinusOneMinuteIndex(long timestamp) {
        return (int) Duration.of(timestamp, MILLIS).minus(1, ChronoUnit.MINUTES).get(ChronoUnit.SECONDS);
    }
    
    private Statistic getStatInCurrentTimeFrame(int index, Statistic stat) {
        return stat != null && stat.getSecondIndex() == index ? stat : null;
    }
    
    
    private Stream<Statistic> getCurrentTimeFrameStatStream() {

    	log.debug("getCurrentTimeFrameStream");
    	
    	long now = Instant.now().toEpochMilli();

        int firstIndex = getMinusOneMinuteIndex(now);
        int lastIndex = getcurrentIndex(now);
        

        return IntStream.rangeClosed(firstIndex, lastIndex)
                .mapToObj(index -> getStatInCurrentTimeFrame(index, statisticArray.get(secondtoArrayIndex(index))))
                .filter(Objects::nonNull);
    }

	
	
}
