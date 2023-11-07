package org.example.metric.utils.systemCommand;

import java.util.List;

import org.example.metric.metricsInfo.NetworkMetricsInfo;

public interface OsCommandUtil {

	NetworkMetricsInfo getNetworkMetrics();
	List<String> getSystemList();

	void shutdown();

	void reboot();

	void serviceEnable(String serviceName);

	void serviceDisable(String serviceName);

}
