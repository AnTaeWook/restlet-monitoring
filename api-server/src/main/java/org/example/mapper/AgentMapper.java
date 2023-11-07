package org.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.example.entity.Agent;

@Mapper
public interface AgentMapper {

	void insert(Agent agent);

	List<Agent> selectAll();

	Agent selectById(Long agentId);

	void updateDescriptionWithId(Agent agent);

	void deleteWithId(Long agentId);

	void deleteAll();

}
