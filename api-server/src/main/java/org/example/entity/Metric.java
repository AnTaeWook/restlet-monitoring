package org.example.entity;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Metric {

	private Long metricId;
	private Long agentId;
	private Float totalMemory;
	private Float freeMemory;
	private Float usedMemory;
	private Float cpuRate;
	private Float inboundTraffic;
	private Float outboundTraffic;
	private LocalDateTime createdAt;

	public Metric(Long agentId, Float totalMemory, Float freeMemory, Float usedMemory, Float cpuRate,
		Float inboundTraffic, Float outboundTraffic, LocalDateTime createdAt) {
		this.agentId = agentId;
		this.totalMemory = totalMemory;
		this.freeMemory = freeMemory;
		this.usedMemory = usedMemory;
		this.cpuRate = cpuRate;
		this.inboundTraffic = inboundTraffic;
		this.outboundTraffic = outboundTraffic;
		this.createdAt = createdAt;
	}

}
