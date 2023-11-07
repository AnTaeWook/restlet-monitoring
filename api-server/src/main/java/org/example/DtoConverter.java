package org.example;

import org.example.dto.MetricXmlRpcDto;
import org.example.entity.Metric;

public class DtoConverter {

	public static Metric MetricXmlRpcDtoToMetric(MetricXmlRpcDto metricXmlRpcDto, Long agentId) {
		return new Metric(agentId,
			metricXmlRpcDto.getTotalMemory(),
			metricXmlRpcDto.getFreeMemory(),
			metricXmlRpcDto.getUsedMemory(),
			metricXmlRpcDto.getCpuRate(),
			metricXmlRpcDto.getInboundTraffic(),
			metricXmlRpcDto.getOutboundTraffic(),
			metricXmlRpcDto.getCreatedAt()
		);
	}
}
