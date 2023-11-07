package org.example.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.example.ApiServerDataSource;
import org.example.dto.AgentMetricDateDto;
import org.example.entity.Agent;
import org.example.entity.Metric;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AgentRepositoryTest {

	private final AgentRepository agentRepository = new AgentRepository(ApiServerDataSource.sqlSessionFactory());
	private final MetricRepository metricRepository = new MetricRepository(ApiServerDataSource.sqlSessionFactory());
	private final LocalDateTime dateTimeStandard = LocalDateTime.of(2023, 1, 1, 0, 0);

	@BeforeEach
	void initAgentData() {
		agentRepository.save(new Agent("Window 10", "17.0.0.1", "Test VM 1", dateTimeStandard));
		agentRepository.save(new Agent("Window 8", "17.0.0.2", "Test VM 2", dateTimeStandard));
		agentRepository.save(new Agent("Linux", "17.0.0.3", "Test VM 3", dateTimeStandard));
	}

	@AfterEach
	void clear() {
		agentRepository.deleteAll();
	}

	@Test
	@DisplayName("활성화 상태인 에이전트를 전부 조회한다.")
	void findAll() {
		// given & when
		List<Agent> agents = agentRepository.findAll();

		// then
		assertThat(agents.size()).isEqualTo(3);
	}

	@Test
	@DisplayName("에이전트 ID를 통해 특정 에이전트를 조회한다.")
	void findById() {
		// given
		Agent agent = agentRepository.save(new Agent("Linux", "17.0.0.4", "special vm", dateTimeStandard));
		Long agentId = agent.getAgentId();

		// when
		Agent foundAgent = agentRepository.findById(agentId).get();

		// then
		assertThat(foundAgent.getOs()).isEqualTo("Linux");
		assertThat(foundAgent.getPrivateIP()).isEqualTo("17.0.0.4");
		assertThat(foundAgent.getDescription()).isEqualTo("special vm");
	}

	@Test
	@DisplayName("새 에이전트를 등록할 수 있다.")
	void save() {
		// given & when
		agentRepository.save(new Agent("os", "ip", "des", LocalDateTime.now()));
		List<Agent> agents = agentRepository.findAll();

		// then
		assertThat(agents.size()).isEqualTo(4);
	}

	@Test
	@DisplayName("에이전트의 설명(desc)를 수정할 수 있다.")
	void update() {
		// given
		String oldDescription = "old desc";
		Agent agent = agentRepository.save(new Agent("os", "ip", oldDescription, LocalDateTime.now()));

		// when
		String newDescription = "new desc";
		agentRepository.update(agent, newDescription);

		// then
		Agent foundAgent = agentRepository.findById(agent.getAgentId()).get();
		assertThat(foundAgent.getDescription()).isEqualTo(newDescription);
	}

	@Test
	@DisplayName("에이전트 등록 해재 시 해제 여부가 true 로 수정된다.")
	void remove() {
		// given
		Agent agent = agentRepository.save(new Agent("os", "ip", "desc", LocalDateTime.now()));

		// when
		agentRepository.remove(agent.getAgentId());

		// then
		List<Agent> agents = agentRepository.findAll();
		assertThat(agents.size()).isEqualTo(3);

		Optional<Agent> foundAgent = agentRepository.findById(agent.getAgentId());
		assertThat(foundAgent.isPresent()).isFalse();
	}

	@Test
	@DisplayName("에이전트 ID 목록을 통해 각 에이전트 별 최신 메트릭의 생성 날짜를 조회한다.")
	void findLatestDateTime() {
		// given
		List<Long> agentIdList = getAgentIdList();
		metricRepository.save(new Metric(agentIdList.get(0), 10.0f, 5.0f, 5.0f, 22.2f, 0.01f, 0.02f, dateTimeStandard));
		metricRepository.save(new Metric(agentIdList.get(1), 10.0f, 5.0f,
			5.0f, 22.2f, 0.01f, 0.02f, dateTimeStandard.plusDays(1)));

		// when
		List<AgentMetricDateDto> dateDtoList = agentRepository.findDateDto(agentIdList);

		// then
		Assertions.assertThat(dateDtoList.get(0).getLatestDate()).isEqualTo(dateTimeStandard);
		Assertions.assertThat(dateDtoList.get(1).getLatestDate()).isEqualTo(dateTimeStandard.plusDays(1));
		Assertions.assertThat(dateDtoList.get(2).getLatestDate()).isEqualTo(LocalDateTime.of(1, 1, 1, 0, 0, 0));
	}

	private List<Long> getAgentIdList() {
		List<Long> agentIdList = new ArrayList<>();
		agentIdList.add(agentRepository.save(new Agent("Window 10", "17.0.0.1", "Test VM 1", dateTimeStandard)).getAgentId());
		agentIdList.add(agentRepository.save(new Agent("Window 8", "17.0.0.2", "Test VM 2", dateTimeStandard)).getAgentId());
		agentIdList.add(agentRepository.save(new Agent("Linux", "17.0.0.3", "Test VM 3", dateTimeStandard)).getAgentId());
		return agentIdList;
	}

}
