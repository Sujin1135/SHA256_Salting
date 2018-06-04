package com.blog.mango.user.exception;

@SuppressWarnings("serial")
public class UserAlreadyRegisteredException extends RuntimeException {
	public UserAlreadyRegisteredException () {
		this("사용중인 이메일입니다.");
	}
	
	public UserAlreadyRegisteredException (String message) {
		super(message);
	}
}
