package org.example.metric.utils.systemCommand;

import java.util.List;

import org.example.metric.metricsInfo.NetworkMetricsInfo;

public class CentosCommandUtilImpl extends OsTemplateCommandUtil implements OsCommandUtil {

	private final int INBOUND_TOKEN_LOCATION = START_TOKEN + 2;
	private final int OUTBOUND_TOKEN_LOCATION = START_TOKEN + 3;
	private final String NETWORK_TRAFFIC_MONITOR_COMMAND = "sar -n DEV | grep eth0 | tail -2 | head -1";
	private final String SYSTEM_SERVICE_LIST_COMMAND = "systemctl list-units --type=service | head -n -7";
	private final String SHUTDOWN_SYSTEM_AFTER_1M = "shutdown 1";
	private final String REBOOT_SYSTEM_AFTER_1M = "shutdown -r 1";
	private final String SERVICE_ENABLE = "systemctl start ";
	private final String SERVICE_DISABLE = "systemctl stop ";
	private final String SPACE_SEPARATOR = "\\s+";

	@Override
	public NetworkMetricsInfo getNetworkMetrics() {
		String[] command = {"/bin/sh" , "-c" , NETWORK_TRAFFIC_MONITOR_COMMAND};
		List<String> tokens = executeCommand(command, SPACE_SEPARATOR);
		return NetworkMetricsInfo.builder()
			.inboundTraffic(parseTokens(tokens, INBOUND_TOKEN_LOCATION))
			.outboundTraffic(parseTokens(tokens, OUTBOUND_TOKEN_LOCATION))
			.build();
	}

	@Override
	public List<String> getSystemList() {
		String[] command = {"/bin/sh" , "-c" , SYSTEM_SERVICE_LIST_COMMAND};
		return executeCommand(command, null);
	}

	@Override
	public void shutdown() {
		String[] command = {"/bin/sh" , "-c" , SHUTDOWN_SYSTEM_AFTER_1M};
		executeCommand(command, "");
	}

	@Override
	public void reboot() {
		String[] command = {"/bin/sh" , "-c" , REBOOT_SYSTEM_AFTER_1M};
		executeCommand(command, "");
	}

	@Override
	public void serviceEnable(String serviceName) {
		String[] command = {"/bin/sh", "-c", SERVICE_ENABLE + serviceName};
		executeCommand(command, "");
	}

	@Override
	public void serviceDisable(String serviceName) {
		String[] command = {"/bin/sh", "-c", SERVICE_DISABLE + serviceName};
		executeCommand(command, "");
	}

	private Float parseTokens(List<String> tokens, int tokenLocation){
		return Float.parseFloat(tokens.get(tokenLocation));
	}

}
