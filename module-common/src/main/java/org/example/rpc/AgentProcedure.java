package org.example.rpc;

import java.time.LocalDateTime;
import java.util.List;

import org.example.dto.MetricXmlRpcDtos;

public interface AgentProcedure {

	MetricXmlRpcDtos getMetrics(LocalDateTime latestDateTime);

	String getOs();

	List<String> getSystemServiceList();

	void systemShutdown();

	void systemReboot();

	void serviceEnable(String serviceName);

	void serviceDisable(String serviceName);

	Long saveAgentInfo(Long agentId);

	void terminateAgent();

}
