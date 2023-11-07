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
public class MetricReadResponseDto {

	private Long agentId;
	private String os;
	private String privateIP;
	private String description;
	private Integer metricCount;
	private List<Metric> metricList;
	private Integer pageNum;
	private Integer pageSize;

	public static MetricReadResponseDto of(Agent agent, List<Metric> metricList, Integer pageNum, Integer pageSize) {
		return new MetricReadResponseDto(
			agent.getAgentId(),
			agent.getOs(),
			agent.getPrivateIP(),
			agent.getDescription(),
			metricList.size(),
			metricList,
			pageNum,
			pageSize
		);
	}

}
