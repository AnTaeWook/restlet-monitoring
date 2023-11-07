package org.example.config;

import java.io.IOException;

import org.apache.zookeeper.ZooKeeper;
import org.example.PathVariables;
import org.example.agentInfo.AgentService;
import org.example.metric.MetricJob;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DependencyManager {
	public static DatabaseManager databaseManager = new DatabaseManager();
	public static ZookeeperConnection zookeeperConnection;
	public static MetricScheduler metricScheduler = new MetricScheduler("0 * * * * ?", MetricJob.class);

	static {
		try {
			zookeeperConnection = new ZookeeperConnection(new ZooKeeper(
				PathVariables.getInstance().getString("zookeeper.address"), 5000, null), new AgentService());
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
}
