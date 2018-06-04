package com.blog.mango.user.service;

import com.blog.mango.user.exception.UserAlreadyRegisteredException;
import com.blog.mango.user.exception.UserFailLoginException;
import com.blog.mango.user.vo.User;

public interface UserService {
	public void register(User user, String rawPassword) throws UserAlreadyRegisteredException;
	public User login(User user) throws UserFailLoginException;
	public int keepLogin(User user);
	public User cookieLogin(String sessionId);
}
