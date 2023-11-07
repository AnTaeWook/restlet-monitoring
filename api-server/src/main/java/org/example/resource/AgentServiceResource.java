package org.example.resource;

import org.example.common.ApiResponse;
import org.example.service.AgentService;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.inject.Injector;

public class AgentServiceResource extends ServerResource {

	private Gson gson;
	private AgentService agentService;

	@Override
	protected void doInit() throws ResourceException {
		Injector injector = (Injector)getContext().getAttributes().get(Injector.class.getName());
		gson = injector.getInstance(Gson.class);
		agentService = injector.getInstance(AgentService.class);
	}

	@Get
	public Representation agentServiceList() {
		String agentId = (String)getRequestAttributes().getOrDefault("agent_id", null);
		ApiResponse response = ApiResponse.success(agentService.getAgentServiceList(Long.parseLong(agentId)));
		return new JsonRepresentation(gson.toJson(response));
	}

}
