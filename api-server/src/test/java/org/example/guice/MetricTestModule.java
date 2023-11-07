package org.example.guice;

import static org.mockito.BDDMockito.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.dto.MetricReadRequestDto;
import org.example.entity.Agent;
import org.example.entity.Metric;
import org.example.repository.AgentRepository;
import org.example.repository.MetricRepository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;

public class MetricTestModule extends AbstractModule {

	private final LocalDateTime dateTimeStandard = LocalDateTime.of(2022, 1, 1, 0, 0);

	@Override
	protected void configure() {
		bind(Gson.class).toInstance(new GsonBuilder().serializeNulls().create());
		bind(AgentRepository.class).toInstance(getAgentRepositoryMock());
		bind(MetricRepository.class).toInstance(getMetricRepositoryMock());
	}

	private MetricRepository getMetricRepositoryMock() {
		MetricRepository mock = mock(MetricRepository.class);
		given(mock.findByAgentId(any(MetricReadRequestDto.class))).willReturn(getMetricList());
		return mock;
	}

	private List<Metric> getMetricList() {
		List<Metric> metricList = new ArrayList<>();
		metricList.add(new Metric(1L, 10.0f, 5.0f, 5.0f, 22.2f, 0.01f, 0.02f, dateTimeStandard));
		metricList.add(new Metric(1L, 11.0f, 6.0f, 5.0f, 23.2f, 0.02f, 0.03f, dateTimeStandard));
		metricList.add(new Metric(1L, 12.0f, 6.0f, 6.0f, 24.2f, 0.03f, 0.04f, dateTimeStandard));
		return metricList;
	}

	private AgentRepository getAgentRepositoryMock() {
		AgentRepository mock = mock(AgentRepository.class);
		given(mock.findById(any(Long.class))).willReturn(Optional.of(new Agent("os", "1.1.1.1", "desc", dateTimeStandard)));
		return mock;
	}

}
