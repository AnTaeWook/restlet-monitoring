package org.example.config;

import static org.apache.zookeeper.ZooDefs.Ids.*;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.example.agentInfo.AgentInfo;
import org.example.agentInfo.AgentService;
import org.example.config.DependencyManager;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class ZookeeperConnection {
	private ZooKeeper zooKeeper;
	private AgentService agentService;
	private final String DISCOVERY_PATH = "/discovery";
	private final String HEALTH_PATH = "/health";

	public void registerZookeeper()  {
		try {
			AgentInfo agentInfo = agentService.findLastAgentInfo();
			if (checkRegisterPossibleNode(agentInfo.getAgentId())){
				zooKeeper.create( HEALTH_PATH + "/" + agentInfo.getAgentId(), null, OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
				DependencyManager.metricScheduler.start();
				log.info("ZooKeeper Registration Success");
			}
		}catch (InterruptedException | KeeperException e){
			log.error(e.getMessage(), e);
		}
	}

	private boolean checkRegisterPossibleNode(Long id) throws
		InterruptedException,
		KeeperException {
		return !(zooKeeper.exists(DISCOVERY_PATH + "/" + id, false) == null);
	}

}
