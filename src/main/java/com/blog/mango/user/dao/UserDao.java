package com.blog.mango.user.dao;

import java.util.List;

import com.blog.mango.user.vo.User;

public interface UserDao {
	/**
	 * 회원가입 시도
	 * @param user
	 * @return
	 */
	public int insertUser(User user);
	/**
	 * 로그인 시도
	 * @param user
	 * @return
	 */
	public User loginUser(User user);
	/**
	 * 입력받은 이메일에 해당하는 salt 반환
	 * @param email
	 * @return
	 */
	public User getSalt(String email);
	/**
	 * 자동 로그인 정보 저장
	 * sessionId와 sessionLimit을 저장한다
	 * @param user
	 * @return
	 */
	public int keepLogin(User user);
	/**
	 * 자동 로그인
	 * @param sessionId
	 * @return
	 */
	public User cookieLogin(String sessionId);
}
