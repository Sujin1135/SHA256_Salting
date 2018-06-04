package com.blog.mango.user.encoder;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import com.blog.mango.module.SHA256Util;
import com.blog.mango.user.vo.User;

@Component(value="lightweight")
public class PasswordEncoderImpl implements PasswordEncoder {

	@Override
	public User encode(User user) {
		String salt = SHA256Util.generateSalt();
		String newPassword = SHA256Util.getEncrypt(user.getPassword(), salt);
		user.setPassword(newPassword);
		user.setSalt(salt);
		return user;
	}

}
