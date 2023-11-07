package org.example.dto;

import java.time.LocalDateTime;

import org.example.entity.Agent;
import org.example.zookeeper.ZooKeeperClient;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AgentReadResponseDto {

	private Long agentId;
	private String os;
	private String privateIp;
	private String description;
	private LocalDateTime registeredAt;
	private boolean isActive;

	public static AgentReadResponseDto from(Agent agent, boolean isActive) {
		return new AgentReadResponseDto(
			agent.getAgentId(),
			agent.getOs(),
			agent.getPrivateIP(),
			agent.getDescription(),
			agent.getRegisteredAt(),
			isActive
		);
	}

}
