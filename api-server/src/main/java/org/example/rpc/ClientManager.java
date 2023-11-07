package org.example.rpc;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.example.dto.AgentMetricDateDto;
import org.example.entity.Metric;
import org.example.repository.AgentRepository;
import org.example.repository.MetricRepository;
import org.example.zookeeper.ZooKeeperClient;

import com.google.inject.Inject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientManager implements Runnable {

	private final AgentRpcClient agentRpcClient;
	private final MetricRepository metricRepository;
	private final AgentRepository agentRepository;
	private final ZooKeeperClient zooKeeperClient;
	private final ExecutorService executor = Executors.newFixedThreadPool(10);

	@Inject
	public ClientManager(
		AgentRpcClient agentRpcClient,
		MetricRepository metricRepository,
		AgentRepository agentRepository,
		ZooKeeperClient zooKeeperClient
	) {
		this.agentRpcClient = agentRpcClient;
		this.metricRepository = metricRepository;
		this.agentRepository = agentRepository;
		this.zooKeeperClient = zooKeeperClient;
	}

	@Override
	public void run() {
		try {
			doCollectionJob();
		} catch (Exception e) {
			log.warn("Scheduled Jon Throws Exception : {}", e.getMessage());
		}
	}

	private void doCollectionJob() {
		if (zooKeeperClient.getLock()) {
			List<Long> aliveAgentInfoDtoList = zooKeeperClient.getAliveAgentInfoList();
			List<AgentMetricDateDto> dateDtoList = agentRepository.findDateDto(aliveAgentInfoDtoList);
			for (AgentMetricDateDto dto : dateDtoList) {
				executor.submit(() -> addMetrics(dto));
			}
			zooKeeperClient.unlock();
		}
	}

	private void addMetrics(AgentMetricDateDto dto) {
		List<Metric> metricList = agentRpcClient.collectMetrics(
			dto.getPrivateIP(), dto.getAgentId(), dto.getLatestDate()
		);
		if (metricList.size() > 0) {
			metricRepository.saveList(metricList);
		}
	}

}
