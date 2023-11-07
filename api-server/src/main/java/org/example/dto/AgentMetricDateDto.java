package org.example.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AgentMetricDateDto {

	private Long agentId;
	private String privateIP;
	private LocalDateTime latestDate;

	public AgentMetricDateDto(Long agentId, String privateIP, String latestDate) {
		this.agentId = agentId;
		this.privateIP = privateIP;
		this.latestDate = LocalDateTime.parse(latestDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
	}

}
