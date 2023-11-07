package org.example;

import org.assertj.core.api.Assertions;
import org.example.config.MetricScheduler;
import org.example.config.TestJob;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;


@ExtendWith(MockitoExtension.class)
public class SchedulerTest {
	String METRIC_CRON_CONDITION = "0/1 * * * * ?";

	MetricScheduler metricScheduler = new MetricScheduler(METRIC_CRON_CONDITION, TestJob.class);



	@AfterEach
	void clear(){
		metricScheduler.pause();
	}

	@Test
	@DisplayName("스케줄러가 실행되고 실행중인 상태로 변경되는지 확인")
	public void schedulerStartStatusTest() throws SchedulerException {
		// when
		metricScheduler.start();

		// then
		Assertions.assertThat(metricScheduler.getScheduler().isStarted()).isTrue();
	}

	@Test
	@DisplayName("스케줄러가 시작되고 Job이 등록되어 있는지 확인")
	public void schedulerRegisterJobTest() throws SchedulerException {
		// given
		JobDetail jobDetail = metricScheduler.getJobDetail();

		// when
		metricScheduler.start();

		// then
		Assertions.assertThat(metricScheduler.getScheduler().checkExists(jobDetail.getKey())).isTrue();
	}

	@Test
	@DisplayName("스케줄러가 시작 후 종료했을 때, Job이 삭제되는지 확인")
	public void schedulerPauseTest() throws SchedulerException {
		// given
		JobDetail jobDetail = metricScheduler.getJobDetail();

		// when
		metricScheduler.start();
		metricScheduler.pause();

		// then
		Assertions.assertThatException().isThrownBy(
			()-> metricScheduler.getScheduler().checkExists(jobDetail.getKey())
		);

	}

	@Test
	@DisplayName("스케줄러를 재시작 하였을 때, Job이 등록 되었는지와 상태를 확인")
	public void schedulerRestartTest() throws SchedulerException {
		// given
		JobDetail jobDetail = metricScheduler.getJobDetail();

		// when
		metricScheduler.start();
		metricScheduler.pause();
		metricScheduler.start();

		// then
		Assertions.assertThat(metricScheduler.getScheduler().checkExists(jobDetail.getKey())).isTrue();
		Assertions.assertThat(metricScheduler.getScheduler().isStarted()).isTrue();
	}

}
