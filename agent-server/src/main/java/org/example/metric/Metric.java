package org.example.metric;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class Metric {
	private Long metricId;
	private Float cpuRate;
	private Float inboundTraffic;
	private Float outboundTraffic;
	private Float freeMemory;
	private Float usedMemory;
	private Float totalMemory;
	private LocalDateTime createdAt;

}
