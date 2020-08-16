package com.rate.limitter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.TOO_MANY_REQUESTS)
public class TooManyRequestException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5756280505923682032L;

	public TooManyRequestException() {
	}

}
