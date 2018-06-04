package com.blog.mango.user.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blog.mango.user.vo.User;

@Repository
public class UserDaoImpl implements UserDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	private final String path = "com.blog.mango.user.mapper.UserMapper.";

	@Override
	public int insertUser(User user) {
		return sqlSession.insert(path+ "insertUser", user);
	}

	@Override
	public User loginUser(User user) {
		return sqlSession.selectOne(path+ "loginUser", user);
	}
	
	@Override
	public User getSalt(String email) {
		return sqlSession.selectOne(path+ "getSalt", email);
	}
	
	@Override
	public int keepLogin(User user) {
		return sqlSession.update(path+ "keepLogin", user);
	}
	
	@Override
	public User cookieLogin(String sessionId) {
		return sqlSession.selectOne(path+ "cookieLogin", sessionId);
	}
}
