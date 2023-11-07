package org.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.example.dto.MetricReadRequestDto;
import org.example.entity.Metric;

@Mapper
public interface MetricMapper {

	void insert(Metric metric);

	List<Metric> selectByAgentIdAndPeriod(MetricReadRequestDto dto);

	void bulkInsert(List<Metric> metricList);

	void deleteAll();

}
