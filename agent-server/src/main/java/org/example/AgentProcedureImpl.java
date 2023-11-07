package org.example;

import java.time.LocalDateTime;
import java.util.List;

import org.example.agentInfo.AgentInfo;
import org.example.agentInfo.AgentInfoDao;
import org.example.config.DependencyManager;
import org.example.dto.MetricXmlRpcDtos;
import org.example.metric.MetricDao;
import org.example.metric.utils.systemCommand.OsCommandUtil;
import org.example.metric.utils.systemCommand.OsCommandUtilFactory;
import org.example.rpc.AgentProcedure;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AgentProcedureImpl implements AgentProcedure {

	MetricDao metricDao = new MetricDao(DependencyManager.databaseManager.getDataSource());
	AgentInfoDao agentInfoDao = new AgentInfoDao(DependencyManager.databaseManager.getDataSource());

	@Override
	public MetricXmlRpcDtos getMetrics(LocalDateTime latestDateTime) {
		metricDao.deleteBeforeData(latestDateTime);
		return new MetricXmlRpcDtos(metricDao.findAll());
	}

	@Override
	public String getOs() {
		return OsCommandUtilFactory.os;
	}

	@Override
	public List<String> getSystemServiceList() {
		OsCommandUtil osCommandUtil = OsCommandUtilFactory.getOsNetworkMetricsImpl();
		return osCommandUtil.getSystemList();
	}

	@Override
	public void systemShutdown() {
		OsCommandUtilFactory.getOsNetworkMetricsImpl().shutdown();
	}

	@Override
	public void systemReboot() {
		OsCommandUtilFactory.getOsNetworkMetricsImpl().reboot();
	}

	@Override
	public void serviceEnable(String serviceName) {
		OsCommandUtil osCommandUtil = OsCommandUtilFactory.getOsNetworkMetricsImpl();
		osCommandUtil.serviceEnable(serviceName);
	}

	@Override
	public void serviceDisable(String serviceName) {
		OsCommandUtil osCommandUtil = OsCommandUtilFactory.getOsNetworkMetricsImpl();
		osCommandUtil.serviceDisable(serviceName);
	}

	@Override
	public Long saveAgentInfo(Long agentId) {
		AgentInfo agentInfo = AgentInfo.builder().agentId(agentId).build();
		Long agentInfoId = agentInfoDao.save(agentInfo);
		DependencyManager.zookeeperConnection.registerZookeeper();
		return agentInfoId;
	}

	@Override
	public void terminateAgent() {
		agentInfoDao.deleteAgentInfo();
		DependencyManager.metricScheduler.pause();
	}

}
