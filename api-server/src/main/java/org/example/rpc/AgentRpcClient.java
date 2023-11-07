package org.example.rpc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.util.ClientFactory;
import org.example.DtoConverter;
import org.example.dto.MetricXmlRpcDto;
import org.example.dto.MetricXmlRpcDtos;
import org.example.entity.Metric;
import org.example.error.ErrorCode;
import org.example.error.RestApiException;
import org.example.rpc.converter.JaxbTypeConverterFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AgentRpcClient {

	public List<Metric> collectMetrics(String agentIp, Long agentId, LocalDateTime lastDateTime) {
		ArrayList<Metric> metrics = new ArrayList<>();
		MetricXmlRpcDtos metricXmlRpcDtos;
		try {
			metricXmlRpcDtos = getAgentProcedure(agentIp).getMetrics(lastDateTime);
		} catch (Exception e) {
			log.warn("Xml-Rpc collectMetrics Method Error, agentIp : {} agentId : {} lastDataTime : {}", agentIp, agentId, lastDateTime, e);
			return metrics;
		}
		for (MetricXmlRpcDto metricXmlRpcDto : metricXmlRpcDtos.getMetricXmlRpcDtos()) {
			metrics.add(DtoConverter.MetricXmlRpcDtoToMetric(metricXmlRpcDto, agentId));
		}
		return metrics;
	}

	public String getAgentOs(String agentIp) {
		String os = "";
		try {
			os = getAgentProcedure(agentIp).getOs();
		} catch (Exception e) {
			log.warn("Xml-Rpc getAgentOs Method Error, agentIp : {}", agentIp, e);
			throw new RestApiException(ErrorCode.XML_RPC_CLIENT_ERROR, e);
		}
		return os;
	}

	public void postAgentInfo(String agentIp, Long agentId) {
		try {
			getAgentProcedure(agentIp).saveAgentInfo(agentId);
		} catch (Exception e) {
			log.warn("Xml-Rpc postAgentInfo Method Error, agentIp : {} agentId : {}", agentIp, agentId, e);
			throw new RestApiException(ErrorCode.XML_RPC_CLIENT_ERROR, e);
		}
	}

	public List<String> getAgentServices(String agentIp) {
		List<String> agentServices;
		try {
			agentServices = getAgentProcedure(agentIp).getSystemServiceList();
		} catch (Exception e) {
			log.warn("Xml-Rpc getAgentServices Method Error, agentIp : {}", agentIp, e);
			throw new RestApiException(ErrorCode.XML_RPC_CLIENT_ERROR, e);
		}
		return agentServices;
	}

	public void agentSystemShutdown(String agentIp) {
		try {
			getAgentProcedure(agentIp).systemShutdown();
		} catch (Exception e) {
			log.warn("Xml-Rpc agentSystemShutdown Method Error, agentIp : {}", agentIp, e);
			throw new RestApiException(ErrorCode.XML_RPC_CLIENT_ERROR, e);
		}
	}

	public void agentSystemReboot(String agentIp) {
		try {
			getAgentProcedure(agentIp).systemReboot();
		} catch (Exception e) {
			log.warn("Xml-Rpc agentSystemReboot Method Error, agentIp : {}", agentIp, e);
			throw new RestApiException(ErrorCode.XML_RPC_CLIENT_ERROR, e);
		}
	}

	public void serviceEnable(String agentIp, String serviceName) {
		try {
			getAgentProcedure(agentIp).serviceEnable(serviceName);
		} catch (Exception e) {
			log.warn("Xml-Rpc serviceEnable Method Error, agentIp : {}, serviceName : {}",agentIp, serviceName, e);
			throw new RestApiException(ErrorCode.XML_RPC_CLIENT_ERROR, e);
		}
	}

	public void serviceDisable(String agentIp, String serviceName) {
		try {
			getAgentProcedure(agentIp).serviceDisable(serviceName);
		} catch (Exception e) {
			log.warn("Xml-Rpc serviceDisable Method Error, agentIp : {}, serviceName : {}",agentIp, serviceName, e);
			throw new RestApiException(ErrorCode.XML_RPC_CLIENT_ERROR, e);
		}
	}

	public void terminateAgent(String agentIp) {
		try {
			getAgentProcedure(agentIp).terminateAgent();
		} catch (Exception e) {
			// Agent to be terminated is inactive
			log.warn("Xml-Rpc terminateAgent Method Error, agentIp : {}", agentIp, e);
		}
	}

	private AgentProcedure getAgentProcedure(String agentIp) {
		XmlRpcClient xmlRpcClient = XmlRpcClientGenerator.generate(agentIp);
		ClientFactory factory = new ClientFactory(xmlRpcClient, new JaxbTypeConverterFactory());
		return (AgentProcedure) factory.newInstance(AgentProcedure.class);
	}

}
