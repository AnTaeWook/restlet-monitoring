package org.example.service;

import java.util.List;
import java.util.stream.Collectors;

import org.example.dto.AgentCreateRequestDto;
import org.example.dto.AgentReadResponseDto;
import org.example.dto.AgentUpdateRequestDto;
import org.example.dto.ServiceReadResponseDto;
import org.example.dto.SystemRequestDto;
import org.example.entity.Agent;
import org.example.error.ErrorCode;
import org.example.error.RestApiException;
import org.example.repository.AgentRepository;
import org.example.rpc.AgentRpcClient;
import org.example.zookeeper.ZooKeeperClient;

import com.google.inject.Inject;

public class AgentService {

	private final AgentRepository agentRepository;
	private final ZooKeeperClient zooKeeperClient;
	private final AgentRpcClient agentRpcClient;

	@Inject
	public AgentService(AgentRepository agentRepository, ZooKeeperClient zooKeeperClient,
		AgentRpcClient agentRpcClient) {
		this.agentRepository = agentRepository;
		this.zooKeeperClient = zooKeeperClient;
		this.agentRpcClient = agentRpcClient;
	}

	public List<AgentReadResponseDto> getAgentDtolIst() {
		return agentRepository.findAll().stream().map(
			(Agent agent) -> AgentReadResponseDto.from(agent, zooKeeperClient.isActive(agent.getAgentId()))
		).collect(Collectors.toList());
	}

	public AgentReadResponseDto getAgentDto(Long agentId) {
		Agent agent = findAgent(agentId);
		return AgentReadResponseDto.from(agent, zooKeeperClient.isActive(agentId));
	}

	public AgentReadResponseDto createAgent(AgentCreateRequestDto dto) {
		String agentOs = agentRpcClient.getAgentOs(dto.getPrivateIP());
		Agent agent = agentRepository.save(dto.toEntity(agentOs));
		zooKeeperClient.registerAgentDiscovery(agent.getAgentId());
		agentRpcClient.postAgentInfo(agent.getPrivateIP(), agent.getAgentId());
		return AgentReadResponseDto.from(agent, zooKeeperClient.isActive(agent.getAgentId()));
	}

	public AgentReadResponseDto updateAgent(Long agentId, AgentUpdateRequestDto dto) {
		Agent agent = findAgent(agentId);
		agentRepository.update(agent, dto.getDescription());
		return AgentReadResponseDto.from(agent, zooKeeperClient.isActive(agent.getAgentId()));
	}

	public void removeAgent(Long agentId) {
		Agent agent = findAgent(agentId);
		terminateAgent(agent.getAgentId());
		agentRpcClient.terminateAgent(agent.getPrivateIP());
		agentRepository.remove(agentId);
	}

	public ServiceReadResponseDto getAgentServiceList(Long agentId) {
		Agent agent = findAgent(agentId);
		List<String> services = agentRpcClient.getAgentServices(agent.getPrivateIP());
		return ServiceReadResponseDto.of(agent, services);
	}

	public void controlAgent(Long agentId, SystemRequestDto dto) {
		Agent agent = findAgent(agentId);
		switch (dto.getAction()) {
			case "shutdown":
				agentRpcClient.agentSystemShutdown(agent.getPrivateIP());
				break;
			case "reboot":
				agentRpcClient.agentSystemReboot(agent.getPrivateIP());
				break;
			case "enable":
				agentRpcClient.serviceEnable(agent.getPrivateIP(), dto.getServiceName());
				break;
			case "disable":
				agentRpcClient.serviceDisable(agent.getPrivateIP(), dto.getServiceName());
				break;
			default:
				throw new RestApiException(ErrorCode.BAD_ACTION_REQUEST_ERROR);
		}
	}

	private Agent findAgent(Long agentId) {
		return agentRepository.findById(agentId).orElseThrow(
			() -> new RestApiException(ErrorCode.AGENT_NOT_EXISTS_ERROR)
		);
	}

	private void terminateAgent(Long agentId) {
		zooKeeperClient.terminateAgentDiscovery(agentId);
		zooKeeperClient.terminateAgentHealth(agentId);
	}

}
