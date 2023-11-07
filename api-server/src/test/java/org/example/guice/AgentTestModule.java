package org.example.guice;

import static org.mockito.BDDMockito.*;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.entity.Agent;
import org.example.repository.AgentRepository;
import org.example.rpc.AgentRpcClient;
import org.example.zookeeper.ZooKeeperClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;

public class AgentTestModule extends AbstractModule {

	private final LocalDateTime dateTimeStandard = LocalDateTime.of(2022, 1, 1, 0, 0);

	@Override
	protected void configure() {
		bind(Gson.class).toInstance(new GsonBuilder().serializeNulls().create());
		bind(AgentRepository.class).toInstance(getAgentRepositoryMock());
		bind(ZooKeeperClient.class).toInstance(getZooKeeperClientMock());
		bind(AgentRpcClient.class).toInstance(mock(AgentRpcClient.class));
	}

	private AgentRepository getAgentRepositoryMock() {
		AgentRepository mock = mock(AgentRepository.class);
		given(mock.findAll()).willReturn(getAgentList());
		given(mock.findById(anyLong())).willReturn(
			Optional.of(new Agent("Window", "17.0.0.1", "desc 1", dateTimeStandard)));
		return mock;
	}

	private ZooKeeperClient getZooKeeperClientMock() {
		ZooKeeperClient mock = mock(ZooKeeperClient.class);
		given(mock.isActive(anyLong())).willReturn(true);
		return mock;
	}

	private List<Agent> getAgentList() {
		List<Agent> agentList = new ArrayList<>();
		agentList.add(new Agent("Window", "17.0.0.1", "desc 1", dateTimeStandard));
		agentList.add(new Agent("Linux", "17.0.0.2", "desc 2", dateTimeStandard));
		agentList.add(new Agent("Mac", "17.0.0.3", "desc 3", dateTimeStandard));
		return agentList;
	}

}
