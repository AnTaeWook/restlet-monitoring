package org.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.example.dto.AgentMetricDateDto;

@Mapper
public interface AgentMetricMapper {

	List<AgentMetricDateDto> selectLatestDate(List<Long> agentIdList);

}
