package org.example.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MetricReadRequestDto {

	private Long agentId;
	private LocalDateTime createdAtMin;
	private LocalDateTime createdAtMax;
	private Integer pageNum;
	private Integer pageSize;

}
