package org.example.auth;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.example.error.ErrorCode;
import org.example.error.RestApiException;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Filter;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.Verifier;

public class AuthFilter extends ChallengeAuthenticator {
	private List<String> authCheckList;

	public AuthFilter(Context context, ChallengeScheme challengeScheme, String realm, Verifier verifier, Class<? extends ServerResource> serverResource) {
		super(context, true, challengeScheme, realm, verifier);
		setNext(serverResource);
		authCheckList = getAuthMethod(serverResource);
	}
	@Override
	protected int beforeHandle(Request request, Response response) {
		Method method = request.getMethod();
		if (!authCheckList.isEmpty() && authCheckList.contains(method.toString()) && !authenticate(request, response)){
			response.setStatus(Status.CLIENT_ERROR_UNAUTHORIZED,
				new ResourceException(
					new RestApiException(ErrorCode.UNAUTHORIZED_USER_ERROR))
			);
			return Filter.STOP;
		}
		return Filter.CONTINUE;
	}

	private List<String> getAuthMethod(Class<? extends ServerResource> serverResource){
		return Arrays.stream(serverResource.getDeclaredMethods())
			.parallel()
			.filter(m->m.isAnnotationPresent(Auth.class))
			.map(m-> Arrays.stream(m.getDeclaredAnnotations())
				.filter(annotation -> annotation.annotationType().isAnnotationPresent(org.restlet.engine.connector.Method.class))
				.map(annotation -> annotation.annotationType().getAnnotation(org.restlet.engine.connector.Method.class).value())
				.findFirst()
				.get())
			.collect(Collectors.toList());
	}
}
