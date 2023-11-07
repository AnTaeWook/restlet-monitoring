package org.example.error;

import org.example.common.ApiResponse;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.service.StatusService;

import com.google.gson.Gson;
import com.google.inject.Inject;

public class GlobalExceptionHandler extends StatusService {

	private final Gson gson;

	@Inject
	public GlobalExceptionHandler(Gson gson) {
		super();
		this.gson = gson;
	}

	@Override
	public Representation toRepresentation(Status status, Request request, Response response) {
		return new JsonRepresentation(gson.toJson(ApiResponse.fail(status.getThrowable().getCause().getMessage())));
	}

	@Override
	public Status toStatus(Throwable throwable, Request request, Response response) {
		Throwable exception = throwable.getCause();
		if ( exception instanceof RestApiException) {
			return new Status(((RestApiException)exception).getErrorCode().getStatus(), throwable);
		}
		return new Status(Status.SERVER_ERROR_INTERNAL, throwable);
	}

}
