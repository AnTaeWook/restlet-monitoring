package org.example;

import org.restlet.Component;
import org.restlet.Context;
import org.restlet.data.Protocol;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiServer {

	public static void main(String[] args) throws Exception {
		int port = PathVariables.getInstance().getInt("server.port");
		Component component = new Component();
		component.getServers().add(Protocol.HTTP, port);
		component.getDefaultHost().attach(new RestletApplication(Context.getCurrent()));
		component.start();
		log.info("Server Start port : {}", port);
	}

}
