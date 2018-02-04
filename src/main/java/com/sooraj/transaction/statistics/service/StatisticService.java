package com.sooraj.transaction.statistics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sooraj.transaction.statistics.model.Statistic;
import com.sooraj.transaction.statistics.model.Transaction;
import com.sooraj.transaction.statistics.repository.StatisticRepository;
import com.sooraj.transaction.statistics.util.StatisticUtil;

@Component
public class StatisticService {
	
	@Autowired
	StatisticRepository statisticRepo;
	
	
	public boolean updateStatisticForTransaction(Transaction transaction) {
		
		if(StatisticUtil.isWithinOneMinute(transaction.getTimestamp())){
			statisticRepo.update(transaction);
			return true;
		}else {
			return false;
		}
		
	}
	
	
	public Statistic getStatistics() {
		return statisticRepo.getStatistics();
	}
}
