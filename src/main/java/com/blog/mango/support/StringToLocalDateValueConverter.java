package com.blog.mango.support;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.core.convert.converter.Converter;

public class StringToLocalDateValueConverter implements Converter<String, LocalDate> {
	public LocalDate convert(String source) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("uuuu-MM-dd");
		
		LocalDate date = LocalDate.parse(source, format);
		return date;
	}
}