package org.example.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.dto.MetricReadRequestDto;
import org.example.entity.Metric;
import org.example.mapper.MetricMapper;

import com.google.inject.Inject;

public class MetricRepository {

	private final SqlSessionFactory sessionFactory;

	@Inject
	public MetricRepository(SqlSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public List<Metric> findByAgentId(MetricReadRequestDto dto) {
		try (SqlSession session = sessionFactory.openSession()) {
			MetricMapper mapper = session.getMapper(MetricMapper.class);
			return mapper.selectByAgentIdAndPeriod(dto);
		}
	}

	public Metric save(Metric metric) {
		try (SqlSession session = sessionFactory.openSession(true)) {
			MetricMapper mapper = session.getMapper(MetricMapper.class);
			mapper.insert(metric);
		}
		return metric;
	}

	public void saveList(List<Metric> metricList) {
		try (SqlSession session = sessionFactory.openSession(true)) {
			MetricMapper mapper = session.getMapper(MetricMapper.class);
			mapper.bulkInsert(metricList);
		}
	}

	public void deleteAll() {
		try (SqlSession session = sessionFactory.openSession(true)) {
			MetricMapper mapper = session.getMapper(MetricMapper.class);
			mapper.deleteAll();
		}
	}

}
