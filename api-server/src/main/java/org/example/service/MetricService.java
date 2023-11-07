package org.example.service;

import org.example.dto.MetricReadRequestDto;
import org.example.dto.MetricReadResponseDto;
import org.example.entity.Agent;
import org.example.error.ErrorCode;
import org.example.error.RestApiException;
import org.example.repository.AgentRepository;
import org.example.repository.MetricRepository;

import com.google.inject.Inject;

public class MetricService {

	private final AgentRepository agentRepository;
	private final MetricRepository metricRepository;

	@Inject
	public MetricService(AgentRepository agentRepository, MetricRepository metricRepository) {
		this.agentRepository = agentRepository;
		this.metricRepository = metricRepository;
	}

	public MetricReadResponseDto getMetricList(Long agentId, MetricReadRequestDto condition) {
		Agent agent = findAgent(agentId);
		return MetricReadResponseDto.of(
			agent,
			metricRepository.findByAgentId(condition),
			condition.getPageNum(),
			condition.getPageSize()
		);
	}

	private Agent findAgent(Long agentId) {
		return agentRepository.findById(agentId).orElseThrow(
			() -> new RestApiException(ErrorCode.AGENT_NOT_EXISTS_ERROR)
		);
	}

}
