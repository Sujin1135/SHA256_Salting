package com.blog.mango.support;

import org.springframework.core.convert.converter.Converter;

import com.blog.mango.user.vo.User;

public class StringToUserConverter implements Converter<String, User> {

	@Override
	public User convert(String source) {
		return new User().setEmail(source);
	}

}
