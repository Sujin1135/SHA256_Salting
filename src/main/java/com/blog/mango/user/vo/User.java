package com.blog.mango.user.vo;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component("user")
public class User {
	private int userId; // 유저 Primary Key
	private String email; // 이메일
	private String name; // 이름
	private String password; // 비밀번호
	private String pwdCheck; // 비밀번호 확인
	private int gender; // 성별
	private String imgPath; // 프로필 이미지경로
	private String comment; // 간단한 자기소개
	private String salt; // 솔트
	private boolean keepLogin; // 로그인 유지 여부
	private String sessionId; // 세션의 ID
	private Date sessionLimit; // 세션 유지기간
	
	public User () {}
	
	public User (User user) {
		this.email = user.email;
		this.name = user.name;
		this.password = user.password;
		this.gender = user.gender;
		this.imgPath = user.imgPath;
		this.comment = user.comment;
	}
	public User (int userId, String email, String name, String password, 
			int gender, String imgPath, String comment) {
		this.userId = userId;
		this.email = email;
		this.name = name;
		this.password = password;
		this.gender = gender;
		this.imgPath = imgPath;
		this.comment = comment;
	}
	public User (String email, String sessionId, Date sessionLimit) {
		this.email = email;
		this.sessionId = sessionId;
		this.sessionLimit = sessionLimit;
	}
	
	public int getUserId() {
		return userId;
	}

	public User setUserId(int userId) {
		this.userId = userId;
		return this;
	}

	public String getEmail() {
		return email;
	}
	public User setEmail(String email) {
		this.email = email;
		return this;
	}
	public String getName() {
		return name;
	}
	public User setName(String name) {
		this.name = name;
		return this;
	}
	public String getPassword() {
		return password;
	}
	public User setPassword(String password) {
		this.password = password;
		return this;
	}
	public String getPwdCheck() {
		return pwdCheck;
	}
	public User setPwdCheck(String pwdCheck) {
		this.pwdCheck = pwdCheck;
		return this;
	}
	public int getGender() {
		return gender;
	}
	public User setGender(int gender) {
		this.gender = gender;
		return this;
	}
	public String getImgPath() {
		return imgPath;
	}
	public User setImgPath(String imgPath) {
		this.imgPath = imgPath;
		return this;
	}
	public String getComment() {
		return comment;
	}
	public User setComment(String comment) {
		this.comment = comment;
		return this;
	}

	public String getSalt() {
		return salt;
	}

	public User setSalt(String salt) {
		this.salt = salt;
		return this;
	}

	public boolean getKeepLogin() {
		return keepLogin;
	}

	public User setKeepLogin(boolean keepLogin) {
		this.keepLogin = keepLogin;
		return this;
	}

	public String getSessionId() {
		return sessionId;
	}

	public User setSessionId(String sessionId) {
		this.sessionId = sessionId;
		return this;
	}

	public Date getSessionLimit() {
		return sessionLimit;
	}

	public User setSessionLimit(Date sessionLimit) {
		this.sessionLimit = sessionLimit;
		return this;
	}
	
	
}
