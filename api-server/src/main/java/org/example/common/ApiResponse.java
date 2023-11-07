package org.example.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse {

	private boolean success;
	private Object data;
	private String errorMessage;

	public static ApiResponse success() {
		return new ApiResponse(true, null, null);
	}

	public static ApiResponse success(Object data) {
		return new ApiResponse(true, data, null);
	}

	public static ApiResponse fail(String errorMessage) {
		return new ApiResponse(false, null, errorMessage);
	}

}
