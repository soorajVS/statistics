package com.sooraj.transaction.statistics.model;

import lombok.Getter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.EqualsAndHashCode;

@ToString
@Getter
@EqualsAndHashCode
@Builder
public class Statistic {

	private final double sum;
	private final double avg;
	private final double max;
	private final double min;
	private final long count;
	@JsonIgnore
	private final int secondIndex;
	
	
	public double getAvg() {
		return getCount() > 0 ? getSum() / getCount() : 0.0d;
	}
	
	public Statistic addTransaction(double amount) {
		
		return new StatisticBuilder()
		.count(getCount() + 1)
		.sum(getSum() + amount)
		.min(Math.min(getMin(), amount))
		.max(Math.max(getMax(), amount))
		.secondIndex(getSecondIndex()).build();
		
    }
	
	public Statistic addStatistic(Statistic otherStat) {

		if (this.getCount() == 0L) {
			return otherStat;
		}
		if (otherStat.getCount() == 0L) {
			return this;
		}

		return new StatisticBuilder().count(getCount() + otherStat.getCount()).sum(getSum() + otherStat.getSum())
				.min(Math.min(getMin(), otherStat.getMin())).max(Math.max(getMax(), otherStat.getMax()))
				.secondIndex(getSecondIndex()).build();
	}
	
}
