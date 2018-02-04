package com.sooraj.transaction.statistics.service;

import org.junit.Test;

import java.time.Instant;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.sooraj.transaction.statistics.model.Transaction;
import com.sooraj.transaction.statistics.repository.StatisticRepository;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class StatisticServiceTest {

	@Mock
	private StatisticRepository statisticRepo;
	
	@InjectMocks
	private StatisticService statisticsService = new StatisticService();

	@Test
	public void updateStatisticRepoTest() {
		long epochCurrentMilli = Instant.now().toEpochMilli();
		statisticsService.updateStatisticForTransaction(new Transaction(100.0d, epochCurrentMilli));
		verify(statisticRepo).update(new Transaction(100.0d, epochCurrentMilli));
	}
	
}
