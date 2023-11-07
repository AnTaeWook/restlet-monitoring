package org.example.dto;

import java.util.List;

import org.example.entity.Agent;
import org.example.entity.Metric;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceReadResponseDto {

	private Long agentId;
	private String os;
	private String privateIP;
	private String description;
	private List<String> serviceList;

	public static ServiceReadResponseDto of(Agent agent, List<String> serviceList) {
		return new ServiceReadResponseDto(
			agent.getAgentId(),
			agent.getOs(),
			agent.getPrivateIP(),
			agent.getDescription(),
			serviceList
		);
	}

}
