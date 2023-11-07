package org.example.config;

import java.text.ParseException;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MetricScheduler {
	private String METRIC_CRON_CONDITION;
	private Scheduler scheduler;
	private JobDetail jobDetail;

	public MetricScheduler(String cron, Class<? extends Job> jobClass) {
		this.METRIC_CRON_CONDITION = cron;
		this.jobDetail = registerJobClass(jobClass);
	}

	public void start() {
		try {
			initScheduler();
			scheduler.clear();
			initJob();
			scheduler.start();
			log.info("Metric Scheduler Start");
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
	}

	public void pause(){
		try {
			scheduler.interrupt(jobDetail.getKey());
			scheduler.shutdown(false);
			log.info("Metric Scheduler Stop");
		}catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
	}

	private JobDetail registerJobClass(Class<? extends Job> jobClass){
		return JobBuilder.newJob(jobClass)
			.withIdentity("system_metric_collector", "job_group")
			.build();
	}

	private void initScheduler() throws SchedulerException {
		scheduler = new StdSchedulerFactory().getScheduler();
	}

	private void initJob(){
		try {
			// CronTrigger (Job이 수행되는 조건)
			CronScheduleBuilder cronSch = CronScheduleBuilder.cronSchedule(new CronExpression(METRIC_CRON_CONDITION));
			CronTrigger cronTrigger = TriggerBuilder.newTrigger()
				.withIdentity("performed every 60 sec", "cron_trigger_group")
				.withSchedule(cronSch)
				.forJob(jobDetail)
				.build();

			scheduler.scheduleJob(jobDetail, cronTrigger);
		} catch (SchedulerException | ParseException e) {
			log.error(e.getMessage(), e);
		}
	}

}
