package com.sooraj.transaction.statistics.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sooraj.transaction.statistics.model.Transaction;
import com.sooraj.transaction.statistics.service.StatisticService;

@RestController
public class TransactionController {
	
	@Autowired
	StatisticService statisticService;

	@PostMapping(path="/transactions",consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> saveTransaction(@Valid @RequestBody Transaction transaction) {
		
		if(statisticService.updateStatisticForTransaction(transaction)) {
		return new ResponseEntity<>(HttpStatus.CREATED);
		}else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}
}
