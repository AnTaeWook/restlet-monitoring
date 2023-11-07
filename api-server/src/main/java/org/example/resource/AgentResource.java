package org.example.resource;

import org.example.auth.Auth;
import org.example.dto.AgentReadResponseDto;
import org.example.common.ApiResponse;
import org.example.dto.AgentCreateRequestDto;
import org.example.dto.AgentUpdateRequestDto;
import org.example.service.AgentService;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Patch;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.inject.Injector;

public class AgentResource extends ServerResource {

	private Gson gson;
	private AgentService agentService;

	@Override
	protected void doInit() throws ResourceException {
		Injector injector = (Injector)getContext().getAttributes().get(Injector.class.getName());
		gson = injector.getInstance(Gson.class);
		agentService = injector.getInstance(AgentService.class);
	}

	@Get
	public Representation agentDetailsOrList() {
		String agentId = (String)getRequestAttributes().getOrDefault("agent_id", null);
		if (agentId == null) {
			ApiResponse response = ApiResponse.success(agentService.getAgentDtolIst());
			return new JsonRepresentation(gson.toJson(response));
		}
		ApiResponse response = ApiResponse.success(agentService.getAgentDto(Long.parseLong(agentId)));
		return new JsonRepresentation(gson.toJson(response));
	}

	@Post("json")
	public Representation agentAdd(AgentCreateRequestDto dto) {
		ApiResponse response = ApiResponse.success(agentService.createAgent(dto));
		getResponse().setStatus(Status.SUCCESS_CREATED);
		return new JsonRepresentation(gson.toJson(response));
	}

	@Auth
	@Patch("json")
	public Representation agentModify(AgentUpdateRequestDto dto) {
		String agentId = (String)getRequestAttributes().getOrDefault("agent_id", null);
		AgentReadResponseDto response = agentService.updateAgent(Long.parseLong(agentId), dto);
		return new JsonRepresentation(gson.toJson(ApiResponse.success(response)));
	}

	@Auth
	@Delete
	public Representation agentRemove() {
		String agentIdString = (String)getRequestAttributes().getOrDefault("agent_id", null);
		agentService.removeAgent(Long.parseLong(agentIdString));
		getResponse().setStatus(Status.SUCCESS_NO_CONTENT);
		return new JsonRepresentation(gson.toJson(ApiResponse.success()));
	}

}
