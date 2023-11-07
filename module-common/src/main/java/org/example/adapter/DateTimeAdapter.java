package org.example.adapter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.example.util.ProcedureUtil;

public class DateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

	private final DateTimeFormatter dateTimeFormatter = ProcedureUtil.getDateTimeFormatter();

	@Override
	public LocalDateTime unmarshal(String v) {
		return LocalDateTime.parse(v, dateTimeFormatter);
	}

	@Override
	public String marshal(LocalDateTime v) {
		return v.format(dateTimeFormatter);
	}

}
