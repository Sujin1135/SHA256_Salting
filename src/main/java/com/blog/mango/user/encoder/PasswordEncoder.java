package com.blog.mango.user.encoder;

import com.blog.mango.user.vo.User;

public interface PasswordEncoder {
	public User encode(User user);
}
