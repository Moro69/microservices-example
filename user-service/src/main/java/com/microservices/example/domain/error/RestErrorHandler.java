package com.microservices.example.domain.error;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * REST error handler.
 *
 * @author Maxim Shidlovsky
 */
@Log4j2
@ControllerAdvice
@SuppressWarnings("unused")
public class RestErrorHandler {

	@ExceptionHandler(RestException.class)
	public @ResponseBody
    ErrorInfo handleRestException(HttpServletResponse response, RestException ex) {
		log.debug("RestException: message = \"{}\", errorCode = {}, httpStatus = {}",
				ex.getMessage(), ex.getErrorCode(), ex.getHttpStatus());
		log.error("handleRestException", ex);
		response.setStatus(ex.getHttpStatus());
		return new ErrorInfo(ex.getErrorCode(), ex.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public @ResponseBody
    ErrorInfo handleConstraintViolationException(HttpServletResponse response,
                                                                    MethodArgumentNotValidException ex) {
		log.error(ex);

		response.setStatus(HttpStatus.BAD_REQUEST.value());

		return new ErrorInfo(ErrorCodes.EXCEPTION.getCode(), ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public @ResponseBody
    ErrorInfo handleUncaughtException(HttpServletResponse response, Exception ex) {
		log.debug("Exception: message = \"{}\"", ex.getMessage());
		log.error("handleRestException", ex);
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ErrorInfo(ErrorCodes.EXCEPTION.getCode(), ex.getMessage());
	}
}
