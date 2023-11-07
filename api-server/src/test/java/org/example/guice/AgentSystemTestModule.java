package org.example.guice;

import static org.mockito.BDDMockito.*;
import static org.mockito.Matchers.any;

import java.time.LocalDateTime;
import java.util.Optional;

import org.example.entity.Agent;
import org.example.repository.AgentRepository;
import org.example.rpc.AgentRpcClient;
import org.example.zookeeper.ZooKeeperClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;

public class AgentSystemTestModule extends AbstractModule {

	private final LocalDateTime dateTimeStandard = LocalDateTime.of(2022, 1, 1, 0, 0);

	@Override
	protected void configure() {
		bind(Gson.class).toInstance(new GsonBuilder().serializeNulls().create());
		bind(AgentRepository.class).toInstance(getAgentRepositoryMock());
		bind(ZooKeeperClient.class).toInstance(mock(ZooKeeperClient.class));
		bind(AgentRpcClient.class).toInstance(mock(AgentRpcClient.class));
	}

	private AgentRepository getAgentRepositoryMock() {
		AgentRepository mock = mock(AgentRepository.class);
		given(mock.findById(any(Long.class))).willReturn(
			Optional.of(new Agent("os", "1.1.1.1", "desc", dateTimeStandard)));
		return mock;
	}


}
