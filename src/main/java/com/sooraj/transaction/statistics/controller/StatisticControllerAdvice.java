package com.sooraj.transaction.statistics.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.sooraj.transaction.statistics.exception.OutOfTimeRangeException;

@ControllerAdvice
public class StatisticControllerAdvice {
	
	private static final Logger log = LoggerFactory.getLogger(StatisticControllerAdvice.class);
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(OutOfTimeRangeException.class)
    public void outOfRangeTimestampExceptionHandler() {
		log.error("Invalid Timestamp exception handler");
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public void methodValidationHandler() {
		log.error("Invlaid fields");
	}

}
