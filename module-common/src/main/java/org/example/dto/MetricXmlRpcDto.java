package org.example.dto;

import java.time.LocalDateTime;

import javax.xml.bind.Element;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.example.adapter.DateTimeAdapter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "metric")
@XmlAccessorType(XmlAccessType.FIELD)
public class MetricXmlRpcDto implements Element {

	private Float totalMemory;
	private Float freeMemory;
	private Float usedMemory;
	private Float cpuRate;
	private Float inboundTraffic;
	private Float outboundTraffic;
	@XmlJavaTypeAdapter(value = DateTimeAdapter.class)
	private LocalDateTime createdAt;

	public static MetricXmlRpcDto of(Float totalMemory, Float freeMemory, Float usedMemory, Float cpuRate,
		Float inboundTraffic, Float outboundTraffic, LocalDateTime createdAt) {
		return new MetricXmlRpcDto(
			totalMemory, freeMemory, usedMemory, cpuRate, inboundTraffic, outboundTraffic, createdAt
		);
	}

}
