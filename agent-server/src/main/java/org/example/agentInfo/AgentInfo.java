package org.example.agentInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AgentInfo {
	private Long agentInfoId;
	private Long agentId;

}
