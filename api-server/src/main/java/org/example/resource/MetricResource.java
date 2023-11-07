package org.example.resource;

import java.time.LocalDateTime;

import org.example.common.ApiResponse;
import org.example.dto.MetricReadRequestDto;
import org.example.service.MetricService;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.inject.Injector;

public class MetricResource extends ServerResource {

	private Gson gson;
	private MetricService metricService;

	@Override
	protected void doInit() throws ResourceException {
		Injector injector = (Injector)getContext().getAttributes().get(Injector.class.getName());
		gson = injector.getInstance(Gson.class);
		metricService = injector.getInstance(MetricService.class);
	}

	@Get
	public Representation metricList() {
		String agentIdString = (String)getRequestAttributes().getOrDefault("agent_id", null);
		Long agentId = Long.parseLong(agentIdString);
		MetricReadRequestDto condition = getConditionFromRequest(agentId);
		ApiResponse response = ApiResponse.success(metricService.getMetricList(agentId, condition));
		return new JsonRepresentation(gson.toJson(response));
	}

	private MetricReadRequestDto getConditionFromRequest(Long agentId) {
		String createdDateMinString = getParameterOrDefault("created_date_min", "0000-01-01T00:00:00");
		String createdDateMaxString = getParameterOrDefault("created_date_max", "9999-01-01T00:00:00");
		LocalDateTime createdDateMin = LocalDateTime.parse(createdDateMinString);
		LocalDateTime createdDateMax = LocalDateTime.parse(createdDateMaxString);
		Integer pageNum = Integer.parseInt(getParameterOrDefault("page_num", "1"));
		Integer pageSize = Integer.parseInt(getParameterOrDefault("page_size", "20"));
		return new MetricReadRequestDto(agentId, createdDateMin, createdDateMax, pageNum, pageSize);
	}

	private String getParameterOrDefault(String parameterKey, String defaultValue) {
		String values = getQueryValue(parameterKey);
		if (values == null) {
			return defaultValue;
		}
		return values;
	}


}
