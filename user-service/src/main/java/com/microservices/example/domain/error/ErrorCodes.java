package com.microservices.example.domain.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCodes {

	EXCEPTION("000000", "Unhandled exception", HttpStatus.INTERNAL_SERVER_ERROR),

	USER_NOT_FOUND("001001", "USER_NOT_FOUND", HttpStatus.BAD_REQUEST),
	EMAIL_IS_TAKEN("001002", "EMAIL_IS_TAKEN", HttpStatus.BAD_REQUEST),
	USERNAME_IS_TAKEN("001003", "USERNAME_IS_TAKEN", HttpStatus.BAD_REQUEST);

    private final String code;
	private final String message;
	private final HttpStatus httpStatus;

}
