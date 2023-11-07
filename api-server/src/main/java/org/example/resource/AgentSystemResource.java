package org.example.resource;

import org.example.auth.Auth;
import org.example.common.ApiResponse;
import org.example.dto.SystemRequestDto;
import org.example.service.AgentService;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.inject.Injector;

public class AgentSystemResource extends ServerResource {

	private Gson gson;
	private AgentService agentService;

	@Override
	protected void doInit() throws ResourceException {
		Injector injector = (Injector)getContext().getAttributes().get(Injector.class.getName());
		gson = injector.getInstance(Gson.class);
		agentService = injector.getInstance(AgentService.class);
	}

	@Auth
	@Post("json")
	public Representation agentSystemControl(SystemRequestDto dto) {
		String agentId = (String)getRequestAttributes().getOrDefault("agent_id", null);
		agentService.controlAgent(Long.parseLong(agentId), dto);
		return new JsonRepresentation(gson.toJson(ApiResponse.success()));
	}

}
