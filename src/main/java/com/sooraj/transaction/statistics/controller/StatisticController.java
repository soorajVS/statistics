package com.sooraj.transaction.statistics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sooraj.transaction.statistics.model.Statistic;
import com.sooraj.transaction.statistics.service.StatisticService;

@RestController
public class StatisticController {
	
	@Autowired
	StatisticService statisticService;
	
	@GetMapping(path = "/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Statistic> getStatistics() {
		return new ResponseEntity<Statistic>(statisticService.getStatistics(), HttpStatus.OK);
	}

}
