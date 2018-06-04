package com.blog.mango.user.exception;

@SuppressWarnings("serial")
public class UserFailLoginException extends RuntimeException {
	public UserFailLoginException () {
		this("아이디가 틀립니다.");
	}
	
	public UserFailLoginException (String message) {
		super(message);
	}
}
