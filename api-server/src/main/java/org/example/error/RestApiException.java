package org.example.error;

import lombok.Getter;

@Getter
public class RestApiException extends RuntimeException {

	private final ErrorCode errorCode;

	public RestApiException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public RestApiException(ErrorCode errorCode, Exception e) {
		super(errorCode.getMessage(), e);
		this.errorCode = errorCode;
	}

}
