package org.example.dto;

import java.time.LocalDateTime;

import org.example.entity.Agent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AgentCreateRequestDto {

	private String description;
	private String privateIP;

	public Agent toEntity(String os) {
		return new Agent(
			os,
			getPrivateIP(),
			description,
			LocalDateTime.now()
		);
	}

}
