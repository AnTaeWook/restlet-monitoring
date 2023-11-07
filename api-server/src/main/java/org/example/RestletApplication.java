package org.example;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import org.example.auth.AuthFilter;
import org.example.auth.TokenVerifier;
import org.example.error.ErrorCode;
import org.example.error.RestApiException;
import org.example.guice.GuiceModule;
import org.example.rpc.ClientManager;
import org.example.error.GlobalExceptionHandler;
import org.example.resource.AgentResource;
import org.example.resource.AgentServiceResource;
import org.example.resource.AgentSystemResource;
import org.example.resource.MetricResource;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.engine.application.StatusFilter;
import org.restlet.routing.Router;
import org.restlet.service.StatusService;
import org.restlet.service.TaskService;

import com.google.inject.Guice;
import com.google.inject.Injector;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestletApplication extends Application {

	private final String REALM = "teamA Api Server Resources";

	public RestletApplication(Context context) {
		super(context);
	}

	@Override
	public Restlet createInboundRoot() {
		// Locate Service
		Injector injector = Guice.createInjector(new GuiceModule());
		getContext().getAttributes().put(Injector.class.getName(), injector);

		// Register Scheduling Tasks
		TaskService taskService = new TaskService();
		LocalDateTime now = LocalDateTime.now();
		long delay = ChronoUnit.MILLIS.between(now, now.plusMinutes(1).truncatedTo(ChronoUnit.MINUTES));
		taskService.scheduleAtFixedRate(injector.getInstance(ClientManager.class), delay, 60000, TimeUnit.MILLISECONDS);
		try {
			taskService.start();
			log.info("Scheduler Service Start");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RestApiException(ErrorCode.SCHEDULER_START_ERROR, e);
		}

		// Register Status Service
		StatusService statusService = injector.getInstance(GlobalExceptionHandler.class);
		setStatusService(statusService);

		// exception filter
		StatusFilter statusFilter = new StatusFilter(getContext(), statusService);
		statusFilter.setNext(createAgentRouter());

		// Routing Configuration
		Router baseRouter = new Router(getContext());
		baseRouter.attach("/api", statusFilter);
		return baseRouter;
	}

	private Router createAgentRouter() {
		//Attach Server Resources to given URL
		Router router = new Router(getContext());
		router.attach("/agents", AgentResource.class);
		router.attach("/agents/{agent_id}", new AuthFilter(getContext(), ChallengeScheme.CUSTOM, REALM, new TokenVerifier(getPsk()), AgentResource.class));
		router.attach("/agents/{agent_id}/metrics", MetricResource.class);
		router.attach("/agents/{agent_id}/system", new AuthFilter(getContext(), ChallengeScheme.CUSTOM, REALM, new TokenVerifier(getPsk()), AgentSystemResource.class));
		router.attach("/agents/{agent_id}/services", AgentServiceResource.class);
		return router;
	}

	private String getPsk(){
		return PathVariables.getInstance().getString("psk");
	}

}
