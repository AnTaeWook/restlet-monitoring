package org.example.repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.dto.AgentMetricDateDto;
import org.example.entity.Agent;
import org.example.mapper.AgentMapper;
import org.example.mapper.AgentMetricMapper;

import com.google.inject.Inject;

public class AgentRepository {

	private final SqlSessionFactory sessionFactory;

	@Inject
	public AgentRepository(SqlSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public List<Agent> findAll() {
		try (SqlSession session = sessionFactory.openSession()) {
			AgentMapper mapper = session.getMapper(AgentMapper.class);
			return mapper.selectAll();
		}
	}

	public Optional<Agent> findById(Long agentId) {
		try (SqlSession session = sessionFactory.openSession()) {
			AgentMapper mapper = session.getMapper(AgentMapper.class);
			return Optional.ofNullable(mapper.selectById(agentId));
		}
	}

	public Agent save(Agent agent) {
		try (SqlSession session = sessionFactory.openSession(true)) {
			AgentMapper mapper = session.getMapper(AgentMapper.class);
			mapper.insert(agent);
			return agent;
		}
	}

	public void update(Agent agent, String newDescription) {
		try (SqlSession session = sessionFactory.openSession(true)) {
			AgentMapper mapper = session.getMapper(AgentMapper.class);
			agent.updateDescription(newDescription);
			mapper.updateDescriptionWithId(agent);
		}
	}

	public void remove(Long agentId) {
		try (SqlSession session = sessionFactory.openSession(true)) {
			AgentMapper mapper = session.getMapper(AgentMapper.class);
			mapper.deleteWithId(agentId);
		}
	}

	public void deleteAll() {
		try (SqlSession session = sessionFactory.openSession(true)) {
			AgentMapper mapper = session.getMapper(AgentMapper.class);
			mapper.deleteAll();
		}
	}

	public List<AgentMetricDateDto> findDateDto(List<Long> agentIdList) {
		if (agentIdList.size() == 0) {
			return Collections.emptyList();
		}
		try (SqlSession session = sessionFactory.openSession()) {
			AgentMetricMapper mapper = session.getMapper(AgentMetricMapper.class);
			return mapper.selectLatestDate(agentIdList);
		}
	}

}
