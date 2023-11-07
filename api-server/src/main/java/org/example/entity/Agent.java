package org.example.entity;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Agent {

	private Long agentId;
	private String os;
	private String privateIP;
	private String description;
	private boolean isDelete;
	private LocalDateTime registeredAt;

	public Agent(String os, String privateIP, String description, LocalDateTime registeredAt) {
		this.os = os;
		this.privateIP = privateIP;
		this.description = description;
		this.registeredAt = registeredAt;
	}

	public void updateDescription(String description) {
		this.description = description;
	}

}
