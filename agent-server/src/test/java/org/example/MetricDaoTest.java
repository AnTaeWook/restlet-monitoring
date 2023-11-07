package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import javax.sql.DataSource;

import org.assertj.core.api.Assertions;
import org.example.config.TestDatabaseManager;
import org.example.dto.MetricXmlRpcDto;
import org.example.metric.Metric;
import org.example.metric.MetricDao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MetricDaoTest {

	private TestDatabaseManager testDatabaseManager = new TestDatabaseManager();
	private DataSource dataSource = testDatabaseManager.getDataSource();
	private MetricDao metricDao = new MetricDao(dataSource);

	@AfterEach
	public void clear(){
		metricDao.deleteAll();
	}

	@Test
	@DisplayName("데이터베이스 연결 테스트")
	public void dbConnectionTest() throws SQLException {
		// given, when
		Connection connection = dataSource.getConnection();

		// then
		Assertions.assertThat(connection).isNotNull();
	}

	@Test
	@DisplayName("메트릭 INSERT 테스트")
	public void insertMetricTest(){
		// given
		Metric metric = TestDataFactory.getMetric();

		// when
		Long metricId = metricDao.save(metric);

		// then
		Assertions.assertThat(metricId).isNotEqualTo(null);
	}

	@Test
	@DisplayName("메트릭 데이터 전체 조회 테스트")
	public void selectMetricTest(){
		// given
		int listSize = 5;
		for (int i = 0; i < listSize; i++) {
			metricDao.save(TestDataFactory.getMetric());
		}

		// when
		List<MetricXmlRpcDto> metricList = metricDao.findAll();

		// then
		Assertions.assertThat(metricList.size()).isEqualTo(listSize);
	}

	@Test
	@DisplayName("특정 날짜이전 메트릭 삭제 테스트")
	public void deleteMetricBeforeDateTest(){

		// given
		LocalDateTime beforeDate = LocalDateTime.now().minusMinutes(5);
		LocalDateTime nowDate = LocalDateTime.now();

		Metric beforeMetric = TestDataFactory.getMetric(beforeDate);
		Metric nowMetric = TestDataFactory.getMetric(nowDate);

		metricDao.save(beforeMetric);
		metricDao.save(nowMetric);

		// when
		metricDao.deleteBeforeData(beforeDate);
		List<MetricXmlRpcDto> metricList = metricDao.findAll();

		// then
		Assertions.assertThat(metricList.size()).isEqualTo(1);
	}

}
