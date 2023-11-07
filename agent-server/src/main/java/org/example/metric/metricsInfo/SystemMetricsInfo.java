package org.example.metric.metricsInfo;

import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;

import lombok.Getter;


@Getter
public class SystemMetricsInfo {
	private Float cpuRate;
	private Float freeMemory;
	private Float usedMemory;
	private Float totalMemory;

	public SystemMetricsInfo() {
		OperatingSystemMXBean operatingSystemMXBean = getOsMxBean();
		this.cpuRate = getCpuRate(operatingSystemMXBean);
		this.totalMemory = getTotalMemory(operatingSystemMXBean);
		this.freeMemory = getFreeMemory(operatingSystemMXBean);
		this.usedMemory = getUsedMemory(totalMemory, freeMemory);
	}

	private OperatingSystemMXBean getOsMxBean(){
		return (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
	}

	private Float getCpuRate(OperatingSystemMXBean osBean){
		return Float.parseFloat(String.format("%.2f", osBean.getSystemCpuLoad() * 100));
	}

	private Float getTotalMemory(OperatingSystemMXBean osBean){
		return Float.parseFloat(memorySizeToString(osBean.getTotalPhysicalMemorySize()));
	}
	private Float getUsedMemory(Float totalMemory, Float freeMemory){
		return totalMemory - freeMemory;
	}

	private Float getFreeMemory(OperatingSystemMXBean osBean){
		return Float.parseFloat(memorySizeToString(osBean.getFreePhysicalMemorySize()));
	}
	private String memorySizeToString(Long memorySize){
		return String.format("%.2f", (double) memorySize/1024/1024/1024);
	}

}
