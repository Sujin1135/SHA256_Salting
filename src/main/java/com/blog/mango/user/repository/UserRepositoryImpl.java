package com.blog.mango.user.repository;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;

import com.blog.mango.user.dao.UserDao;

//import org.springframework.stereotype.Component;

import com.blog.mango.user.vo.User;

@Named
public class UserRepositoryImpl implements UserRepository {
	
	@Autowired
	UserDao userDao;

	@Override
	public int save(User user) {
		int result = userDao.insertUser(user);
		return result;
	}

	@Override
	public int countByUsername(String username) {
		return 0;
	}

}
