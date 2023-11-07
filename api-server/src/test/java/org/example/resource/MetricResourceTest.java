package org.example.resource;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.example.common.ApiResponse;
import org.example.dto.MetricReadRequestDto;
import org.example.guice.MetricTestModule;
import org.example.repository.MetricRepository;
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
import com.google.gson.internal.LinkedTreeMap;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class MetricResourceTest {

	private Context context;
	private Router router;
	private Gson gson;
	private final Injector injector = Guice.createInjector(new MetricTestModule());

	@BeforeEach
	void startServer() {
		context = new Context();
		context.getAttributes().put(Injector.class.getName(), injector);
		router = new Router(context);
		router.attach("", MetricResource.class);
		gson = injector.getInstance(Gson.class);
	}

	@Test
	@DisplayName("메트릭 정보 조회 API 테스트")
	void MetricResourceGet() throws URISyntaxException, IOException {
		// given
		Reference reference = new Reference(new URI(""));
		Request request = new Request(Method.GET, reference);

		Map<String, Object> attributes = new HashMap<>();
		attributes.put("agent_id", "1");
		request.setAttributes(attributes);
		Response response = new Response(request);

		// when
		router.handle(request, response);

		// then
		assertThat(response.getStatus()).isEqualTo(Status.SUCCESS_OK);

		ApiResponse apiResponse = gson.fromJson(response.getEntity().getText(), ApiResponse.class);
		assertThat(apiResponse.isSuccess()).isTrue();
		assertThat(apiResponse.getErrorMessage()).isNull();

		LinkedTreeMap<?, ?> data = (LinkedTreeMap<?, ?>)apiResponse.getData();
		assertThat(data.get("pageNum")).isNotNull().isEqualTo(1.0);
		assertThat(data.get("pageSize")).isNotNull().isEqualTo(20.0);

		verify(injector.getInstance(MetricRepository.class)).findByAgentId(any(MetricReadRequestDto.class));
	}

}
