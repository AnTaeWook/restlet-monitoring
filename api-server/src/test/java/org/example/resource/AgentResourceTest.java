package org.example.resource;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.common.ApiResponse;
import org.example.guice.AgentTestModule;
import org.example.repository.AgentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.routing.Router;

import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class AgentResourceTest {

	private Context context;
	private Router router;
	private Gson gson;
	private final Injector injector = Guice.createInjector(new AgentTestModule());

	@BeforeEach
	void startServer() {
		context = new Context();
		context.getAttributes().put(Injector.class.getName(), injector);
		router = new Router(context);
		router.attach("", AgentResource.class);
		gson = injector.getInstance(Gson.class);
	}

	@Test
	@DisplayName("에이전트 목록 조회 API 테스트")
	void AgentResourceGet() throws IOException, URISyntaxException {
		// given
		Reference reference = new Reference(new URI(""));
		Request request = new Request(Method.GET, reference);
		Response response = new Response(request);

		// when
		router.handle(request, response);

		// then
		assertThat(response.getStatus()).isEqualTo(Status.SUCCESS_OK);

		ApiResponse apiResponse = gson.fromJson(response.getEntity().getText(), ApiResponse.class);
		assertThat(apiResponse.isSuccess()).isTrue();
		assertThat(apiResponse.getErrorMessage()).isNull();

		List<?> responseData = new ArrayList<>();
		if (apiResponse.getData() instanceof List) {
			responseData = (List<?>)apiResponse.getData();
		}
		assertThat(responseData.size()).isEqualTo(3);
	}

	@Test
	@DisplayName("에이전트 등록 해제 API 테스트")
	void AgentResourceDelete() throws URISyntaxException {
		// given
		Reference reference = new Reference(new URI(""));
		Request request = new Request(Method.DELETE, reference);

		Map<String, Object> attributes = new HashMap<>();
		attributes.put("agent_id", "1");
		request.setAttributes(attributes);
		Response response = new Response(request);

		// when
		router.handle(request, response);

		// then
		assertThat(response.getStatus()).isEqualTo(Status.SUCCESS_NO_CONTENT);
		verify(injector.getInstance(AgentRepository.class)).remove(1L);
	}

}
