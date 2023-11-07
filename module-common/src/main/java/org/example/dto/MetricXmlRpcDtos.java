package org.example.dto;

import java.util.List;

import javax.xml.bind.Element;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "metrics")
@XmlAccessorType(XmlAccessType.FIELD)
public class MetricXmlRpcDtos implements Element {

	private List<MetricXmlRpcDto> metricXmlRpcDtos;

}
