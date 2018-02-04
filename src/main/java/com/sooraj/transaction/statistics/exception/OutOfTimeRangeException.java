package com.sooraj.transaction.statistics.exception;

public class OutOfTimeRangeException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OutOfTimeRangeException(String message){
		super(message);
	}
	
}
