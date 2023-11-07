package org.example.resource;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.example.auth.AuthFilter;
import org.example.auth.TokenVerifier;
import org.example.guice.AgentSystemTestModule;
import org.example.rpc.AgentRpcClient;
import org.example.dto.SystemRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.routing.Router;

import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class AgentSystemResourceTest {

	private Context context;
	private Router router;
	private Gson gson;
	private final Injector injector = Guice.createInjector(new AgentSystemTestModule());

	@BeforeEach
	void startServer() {
		context = new Context();
		context.getAttributes().put(Injector.class.getName(), injector);
		router = new Router(context);
		router.attach("", AgentSystemResource.class);
		gson = injector.getInstance(Gson.class);
	}

	@Test
	@DisplayName("시스템 재부팅 API 테스트")
	void AgentSystemReboot() throws URISyntaxException {
		// given
		Reference reference = new Reference(new URI(""));
		Request request = new Request(Method.POST, reference);

		Map<String, Object> attributes = new HashMap<>();
		attributes.put("agent_id", "1");
		request.setAttributes(attributes);
		request.setEntity(new JsonRepresentation(gson.toJson(new SystemRequestDto("reboot", null))));
		Response response = new Response(request);

		// when
		router.handle(request, response);

		// then
		assertThat(response.getStatus()).isEqualTo(Status.SUCCESS_OK);
		verify(injector.getInstance(AgentRpcClient.class)).agentSystemReboot("1.1.1.1");
	}

	@Test
	@DisplayName("서비스 활성화 API 테스트")
	void AgentServiceEnable() throws URISyntaxException {
		// given
		Reference reference = new Reference(new URI(""));
		Request request = new Request(Method.POST, reference);

		Map<String, Object> attributes = new HashMap<>();
		attributes.put("agent_id", "1");
		request.setAttributes(attributes);
		request.setEntity(new JsonRepresentation(gson.toJson(new SystemRequestDto("enable", "dummy_service"))));
		Response response = new Response(request);

		// when
		router.handle(request, response);

		// then
		assertThat(response.getStatus()).isEqualTo(Status.SUCCESS_OK);
		verify(injector.getInstance(AgentRpcClient.class)).serviceEnable("1.1.1.1", "dummy_service");
	}

	@Test
	@DisplayName("서비스 제어 API 테스트 - 인증 실패")
	void authenticationFail() throws URISyntaxException {
		// given
		router.attach("/login", new AuthFilter(context, ChallengeScheme.CUSTOM, "TEST_REALM",
			new TokenVerifier("test-psk"), AgentSystemResource.class)
		);
		Reference reference = new Reference(new URI("/login"));
		Request request = new Request(Method.POST, reference);
		Response response = new Response(request);

		// when
		router.handle(request, response);

		// then
		assertThat(response.getStatus()).isEqualTo(Status.CLIENT_ERROR_UNAUTHORIZED);
	}

	@Test
	@DisplayName("서비스 제어 API 테스트 - 인증 성공")
	void authenticationSuccess() throws URISyntaxException {
		// given
		router.attach("/login", new AuthFilter(context, ChallengeScheme.CUSTOM, "TEST_REALM",
			new TokenVerifier("test-psk"), AgentSystemResource.class)
		);

		Reference reference = new Reference(new URI("/login"));
		Request request = new Request(Method.POST, reference);

		request.getHeaders().add("Authorization", "Bearer test-psk");

		Map<String, Object> attributes = new HashMap<>();
		attributes.put("agent_id", "1");
		attributes.put("org.restlet.http.headers", request.getHeaders());
		request.setAttributes(attributes);
		request.setEntity(new JsonRepresentation(gson.toJson(new SystemRequestDto("reboot", null))));
		Response response = new Response(request);

		// when
		router.handle(request, response);

		// then
		assertThat(response.getStatus()).isEqualTo(Status.SUCCESS_OK);
	}

}
