package org.example.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.example.ApiServerDataSource;
import org.example.dto.MetricReadRequestDto;
import org.example.entity.Metric;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MetricRepositoryTest {

	private final MetricRepository metricRepository = new MetricRepository(ApiServerDataSource.sqlSessionFactory());
	private final LocalDateTime dateTimeStandard = LocalDateTime.of(2023, 1, 1, 0, 0);
	private final LocalDateTime lowDateTimeStandard = LocalDateTime.of(0, 1, 1, 0, 0);
	private final LocalDateTime highDateTimeStandard = LocalDateTime.of(9999, 1, 1, 0, 0);

	@BeforeEach
	void initAgentData() {
		metricRepository.save(new Metric(1L, 10.0f, 5.0f, 5.0f, 22.2f, 0.01f, 0.02f, dateTimeStandard));
		metricRepository.save(new Metric(1L, 11.0f, 6.0f, 5.0f, 23.2f, 0.02f, 0.03f, dateTimeStandard));
		metricRepository.save(new Metric(1L, 12.0f, 6.0f, 6.0f, 24.2f, 0.03f, 0.04f, dateTimeStandard));
	}

	@AfterEach
	void clear() {
		metricRepository.deleteAll();
	}

	@Test
	@DisplayName("에이전트ID를 통해 해당 에이전트의 메트릭을 조회한다.")
	void findByAgentId() {
		// given & when
		List<Metric> metrics = metricRepository.findByAgentId(new MetricReadRequestDto(
			1L, lowDateTimeStandard, highDateTimeStandard, 1, 20
		));

		// then
		assertThat(metrics.get(0).getAgentId()).isEqualTo(1L);
		assertThat(metrics.size()).isEqualTo(3);
	}

	@Test
	@DisplayName("메트릭 리스트를 전달하여 다수의 메트릭을 다량 삽입 한다.")
	void saveList() {
		// given
		List<Metric> metricList = new ArrayList<>();
		metricList.add(new Metric(1L, 15.0f, 8.0f, 7.0f, 22.1f, 0.02f, 0.03f, dateTimeStandard));
		metricList.add(new Metric(1L, 16.0f, 9.0f, 7.0f, 22.1f, 0.02f, 0.03f, dateTimeStandard));
		metricList.add(new Metric(1L, 17.0f, 10.0f, 7.0f, 22.1f, 0.02f, 0.03f, dateTimeStandard));

		// when
		metricRepository.saveList(metricList);

		// then
		List<Metric> metrics = metricRepository.findByAgentId(new MetricReadRequestDto(
			1L, lowDateTimeStandard, highDateTimeStandard, 1, 20
		));
		assertThat(metrics.size()).isEqualTo(6);
	}

}
