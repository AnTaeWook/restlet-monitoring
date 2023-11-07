package org.example.metric.metricsInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class NetworkMetricsInfo {
	private Float inboundTraffic;
	private Float outboundTraffic;
}
