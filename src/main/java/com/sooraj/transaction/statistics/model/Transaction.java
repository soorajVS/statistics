package com.sooraj.transaction.statistics.model;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Transaction {
	
	@NotNull
	private final Double amount;
	
	@NotNull
	private final Long timestamp;

}
