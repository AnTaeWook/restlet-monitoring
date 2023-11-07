package org.example.metric;

import java.time.LocalDateTime;

import org.example.config.DependencyManager;
import org.example.metric.metricsInfo.NetworkMetricsInfo;
import org.example.metric.metricsInfo.SystemMetricsInfo;
import org.example.metric.utils.systemCommand.OsCommandUtil;
import org.example.metric.utils.systemCommand.OsCommandUtilFactory;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;

public class MetricJob implements InterruptableJob {
	private Thread currentThread = null;
	MetricDao metricDao = new MetricDao(DependencyManager.databaseManager.getDataSource());

	@Override
	public void interrupt() {
		this.currentThread.interrupt();
	}

	@Override
	public void execute(JobExecutionContext context) {
		this.currentThread = Thread.currentThread();

		// metric collect
		Metric metric = getMetrics();

		// insert
		metricDao.save(metric);
	}

	private Metric getMetrics(){
		SystemMetricsInfo systemMetricsInfo = getSystemMetricsInfo();
		NetworkMetricsInfo networkMetricsInfo = getNetworkMetricsInfo();
		return Metric.builder()
			.cpuRate(systemMetricsInfo.getCpuRate())
			.inboundTraffic(networkMetricsInfo.getInboundTraffic())
			.outboundTraffic(networkMetricsInfo.getOutboundTraffic())
			.totalMemory(systemMetricsInfo.getTotalMemory())
			.freeMemory(systemMetricsInfo.getFreeMemory())
			.usedMemory(systemMetricsInfo.getUsedMemory())
			.createdAt(getCurrentTime())
			.build();
	}

	private LocalDateTime getCurrentTime(){
		return LocalDateTime.now();
	}

	private NetworkMetricsInfo getNetworkMetricsInfo(){
		OsCommandUtil osCommandUtil = OsCommandUtilFactory.getOsNetworkMetricsImpl();
		return osCommandUtil.getNetworkMetrics();
	}

	private SystemMetricsInfo getSystemMetricsInfo(){
		return new SystemMetricsInfo();
	}
}
