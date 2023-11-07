package org.example.zookeeper;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.zookeeper.AddWatchMode;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.example.PathVariables;
import org.example.error.ErrorCode;
import org.example.error.RestApiException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZooKeeperClient implements Watcher {

	private static final String ZOOKEEPER_HOST = PathVariables.getInstance().getString("zookeeper.host");
	private static final String ZOOKEEPER_PORT = PathVariables.getInstance().getString("zookeeper.port");
	private static final String DISCOVERY_PATH = "/discovery";
	private static final String HEALTH_PATH = "/health";
	private static final String LOCK_PATH = "/lock";
	private final ZooKeeper zooKeeper;
	private List<String> aliveAgentInfoList;

	public ZooKeeperClient() {
		try {
			zooKeeper = new ZooKeeper(getConnectString(), 5000, event -> {});
			initParentNodes();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RestApiException(ErrorCode.ZOOKEEPER_ERROR, e);
		}
	}

	private void initParentNodes() {
		try {
			create(DISCOVERY_PATH, CreateMode.PERSISTENT);
			create(HEALTH_PATH, CreateMode.PERSISTENT);
			zooKeeper.addWatch(HEALTH_PATH, this, AddWatchMode.PERSISTENT);
			aliveAgentInfoList = zooKeeper.getChildren(HEALTH_PATH, null);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RestApiException(ErrorCode.ZOOKEEPER_ERROR, e);
		}
	}

	public void registerAgentDiscovery(Long agentId) {
		try {
			create(DISCOVERY_PATH + "/" + agentId, CreateMode.PERSISTENT);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RestApiException(ErrorCode.ZOOKEEPER_ERROR, e);
		}
	}

	public void terminateAgentDiscovery(Long agentId) {
		try {
			delete(DISCOVERY_PATH + "/" + agentId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RestApiException(ErrorCode.ZOOKEEPER_ERROR, e);
		}
	}

	public void terminateAgentHealth(Long agentId) {
		try {
			delete(HEALTH_PATH + "/" + agentId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RestApiException(ErrorCode.ZOOKEEPER_ERROR, e);
		}
	}

	public boolean isActive(Long agentId) {
		return aliveAgentInfoList.contains(agentId.toString());
	}

	public List<Long> getAliveAgentInfoList() {
		return aliveAgentInfoList.stream().map(Long::parseLong).collect(Collectors.toList());
	}

	public boolean getLock() {
		try {
			zooKeeper.create(LOCK_PATH, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public void unlock() {
		try {
			Thread.sleep(1000);
			zooKeeper.delete(LOCK_PATH, zooKeeper.exists(LOCK_PATH, true).getVersion());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private void create(String path, CreateMode createMode) throws InterruptedException, KeeperException {
		if (zooKeeper.exists(path, false) == null) {
			zooKeeper.create(path, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
		}
	}

	private void delete(String path) throws InterruptedException, KeeperException {
		if (zooKeeper.exists(path, false) != null) {
			zooKeeper.delete(path, zooKeeper.exists(path, true).getVersion());
		}
	}

	private String getConnectString() {
		return ZOOKEEPER_HOST + ":" + ZOOKEEPER_PORT;
	}

	@Override
	public void process(WatchedEvent event) {
		if (event.getPath().equals(HEALTH_PATH) && event.getType() == Event.EventType.NodeChildrenChanged) {
			try {
				aliveAgentInfoList = zooKeeper.getChildren(HEALTH_PATH, null);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				throw new RestApiException(ErrorCode.ZOOKEEPER_ERROR, e);
			}
		}
	}

}
