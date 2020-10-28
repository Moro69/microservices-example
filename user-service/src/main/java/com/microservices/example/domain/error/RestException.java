package com.microservices.example.domain.error;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestException extends RuntimeException {

	private static final long serialVersionUID = 3954053755630331857L;

	private int httpStatus;
	private String errorCode;

	public RestException(String message, int httpStatus, String errorCode) {
		super(message);
		this.httpStatus = httpStatus;
		this.errorCode = errorCode;
	}

	public RestException(ErrorCodes errorCode) {
		super(errorCode.getMessage());
		this.httpStatus = errorCode.getHttpStatus().value();
		this.errorCode = errorCode.getCode();
	}

}
