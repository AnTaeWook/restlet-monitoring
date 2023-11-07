package org.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.example.agentInfo.AgentInfo;
import org.example.metric.Metric;

public class TestDataFactory {

	private static final Float FAKE_FLOAT_VALUE = 0.0f;
	private static final LocalDateTime FAKE_DATA_VALUE = LocalDateTime.now();

	public static Metric getMetric(){
		return Metric.builder()
			.inboundTraffic(FAKE_FLOAT_VALUE)
			.outboundTraffic(FAKE_FLOAT_VALUE)
			.cpuRate(FAKE_FLOAT_VALUE)
			.totalMemory(FAKE_FLOAT_VALUE)
			.usedMemory(FAKE_FLOAT_VALUE)
			.freeMemory(FAKE_FLOAT_VALUE)
			.createdAt(FAKE_DATA_VALUE)
			.build();
	}

	public static Metric getMetric(LocalDateTime localDateTime){
		return Metric.builder()
			.inboundTraffic(FAKE_FLOAT_VALUE)
			.outboundTraffic(FAKE_FLOAT_VALUE)
			.cpuRate(FAKE_FLOAT_VALUE)
			.totalMemory(FAKE_FLOAT_VALUE)
			.usedMemory(FAKE_FLOAT_VALUE)
			.freeMemory(FAKE_FLOAT_VALUE)
			.createdAt(localDateTime)
			.build();
	}

	public static AgentInfo getAgentInfo(){
		return AgentInfo.builder()
			.agentId(1L)
			.build();
	}

	public static List<AgentInfo> getAgentInfoList(){
		List<AgentInfo> testAgentInfoList = new ArrayList<>();
		testAgentInfoList.add(getAgentInfo());
		return testAgentInfoList;
	}
}
