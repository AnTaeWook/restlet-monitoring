package org.example;

import static org.mockito.BDDMockito.*;


import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.example.agentInfo.AgentInfo;
import org.example.agentInfo.AgentService;
import org.example.config.ZookeeperConnection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class ZookeeperTest {
	@InjectMocks
	ZookeeperConnection zookeeperConnection;
	@Mock
	ZooKeeper zooKeeper;
	@Mock
	AgentService agentService;

	@Test
	@DisplayName("주키퍼 정상적으로 등록되는지 테스트")
	public void zookeeperRegisterTest() throws InterruptedException, KeeperException {
		// given
		AgentInfo agentInfo = TestDataFactory.getAgentInfo();

		when(agentService.findLastAgentInfo()).thenReturn(agentInfo);
		lenient().when(zooKeeper.exists(any(), any())).thenReturn(null);
		lenient().when(zooKeeper.create(any(), any(), any(), any(), any())).thenReturn("");

		// when, then
		Assertions.assertDoesNotThrow(()->zookeeperConnection.registerZookeeper());

	}

	@Test
	@DisplayName("주키퍼 영구 노드 확인 예외 잡는지 테스트")
	public void zookeeperExistExceptionTest() throws InterruptedException, KeeperException {
		// given
		AgentInfo agentInfo = TestDataFactory.getAgentInfo();
		when(agentService.findLastAgentInfo()).thenReturn(agentInfo);
		lenient().when(zooKeeper.exists(any(), any())).thenThrow(new InterruptedException());

		// when, then
		Assertions.assertDoesNotThrow(()->zookeeperConnection.registerZookeeper());
	}

	@Test
	@DisplayName("주키퍼 생성 예외 잡는지 테스트")
	public void zookeeperCreateExceptionTest() throws InterruptedException, KeeperException {
		// given
		AgentInfo agentInfo = TestDataFactory.getAgentInfo();
		when(agentService.findLastAgentInfo()).thenReturn(agentInfo);
		lenient().when(zooKeeper.exists(any(), any())).thenReturn(null);
		lenient().when(zooKeeper.create(any(), any(), any(), any(), any())).thenThrow(new InterruptedException());

		// when, then
		Assertions.assertDoesNotThrow(()->zookeeperConnection.registerZookeeper());
	}

	@Test
	@DisplayName("영구노드에 등록되어 있지 않으면 임시노드 생성을 호출하지 않는다.")
	public void nonExistsZookeeperNotCreateTest() throws InterruptedException, KeeperException {
		// given
		AgentInfo agentInfo = TestDataFactory.getAgentInfo();
		when(agentService.findLastAgentInfo()).thenReturn(agentInfo);
		lenient().when(zooKeeper.exists(any(), any())).thenReturn(null);

		// when
		zookeeperConnection.registerZookeeper();

		// then
		verify(zooKeeper, never()).create(any(), any(), any(), any(), any());
	}


}
