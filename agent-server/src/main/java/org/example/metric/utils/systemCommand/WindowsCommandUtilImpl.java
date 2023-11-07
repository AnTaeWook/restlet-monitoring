package org.example.metric.utils.systemCommand;

import java.util.List;

import org.example.metric.metricsInfo.NetworkMetricsInfo;

public class WindowsCommandUtilImpl extends OsTemplateCommandUtil implements OsCommandUtil {
	private final String SYSTEM_SERVICE_LIST_COMMAND = "sc queryex type=service state=all | find /i \"DISPLAY_NAME\"";
	private final String DISPLAY_NAME_SEPARATOR = "DISPLAY_NAME: ";
	private final String SHUTDOWN_SYSTEM_AFTER_1M = "shutdown -s -t 60";
	private final String REBOOT_SYSTEM_AFTER_1M = "shutdown -r -t 60";
	private final Float WINDOW_FAKE_FLOAT_VALUE = 0.0f;

	@Override
	public NetworkMetricsInfo getNetworkMetrics() {

		return NetworkMetricsInfo.builder()
			.inboundTraffic(WINDOW_FAKE_FLOAT_VALUE)
			.outboundTraffic(WINDOW_FAKE_FLOAT_VALUE)
			.build();
	}

	@Override
	public List<String> getSystemList() {
		String[] command = {"cmd" , "/c" , SYSTEM_SERVICE_LIST_COMMAND};
		return executeCommand(command, DISPLAY_NAME_SEPARATOR);
	}

	@Override
	public void shutdown() {
		String[] command = {"cmd", "/c", SHUTDOWN_SYSTEM_AFTER_1M};
		executeCommand(command, "");
	}

	@Override
	public void reboot() {
		String[] command = {"cmd", "/c", REBOOT_SYSTEM_AFTER_1M};
		executeCommand(command, "");
	}

	@Override
	public void serviceEnable(String serviceName) {}

	@Override
	public void serviceDisable(String serviceName) {}

}
