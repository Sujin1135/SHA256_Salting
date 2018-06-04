package com.blog.mango.user.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.blog.mango.module.SHA256Util;
import com.blog.mango.user.annotation.Lightweight;
import com.blog.mango.user.dao.UserDao;
import com.blog.mango.user.encoder.PasswordEncoder;
import com.blog.mango.user.exception.UserAlreadyRegisteredException;
import com.blog.mango.user.exception.UserFailLoginException;
import com.blog.mango.user.repository.UserRepository;
import com.blog.mango.user.vo.User;

@Component("mangoService")
@Scope(proxyMode= ScopedProxyMode.INTERFACES)
public class UserServiceImpl implements UserService { // BeanPostProcessor 는 빈 초기화 전처리 & 후처리를 위해 구현하였다
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserDao userDao;
	
	@Value("${application.dateOfServiceStarting:}")
	private LocalDate dateOfServiceStarting;
	
	@Autowired // 생성자를 통하여 오토와이어링 한다
	public UserServiceImpl(UserRepository userRepository,
			@Lightweight PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public void register(User user, String rawPassword) throws UserAlreadyRegisteredException {
		// 입력된 원본 패스워드를 해시화한 후, 사용자 정보로 설정한다.
		passwordEncoder.encode(user);
		int result = userRepository.save(user);
		
		if (result == 0) { // 같은 사용자가 있을 경우 예외를 발생시킨다
			throw new UserAlreadyRegisteredException("아이디가 중복되었습니다.");
		}
	}
	
	@Override
	public User login(User user) throws UserFailLoginException {
		User resultUser; // 반환할 회원정보를 담을 객체
		User salt = userDao.getSalt(user.getEmail()); // salt가 담긴 Vo 객체
		String password = ""; // 솔팅한 비밀번호를 담을 객체
		
		try { // 아이디 확인
			password = SHA256Util.getEncrypt(user.getPassword(), salt.getSalt()); // 입력받은 비밀번호에 솔팅한 값을 담는다
			
			user.setPassword(password); // Vo 객체에 솔팅한 패스워드를 저장한다
			
		} catch (NullPointerException e) { // salt가 비어있는 객체일 경우 아이디가 존재하지 않는것이다
			throw new UserFailLoginException("존재하지 않는 아이디입니다.");
		}
		
		try {
			resultUser = userDao.loginUser(user); // 솔팅한 비밀번호를 이용하여 로그인 시도
			@SuppressWarnings("unused")
			String pwd = resultUser.getPassword(); // 비밀번호가 틀렸을 경우 NullPointerException이 발생
		} catch (NullPointerException e) {
			throw new UserFailLoginException("비밀번호가 틀립니다.");
		}
		
		return resultUser; // 정상적으로 로그인이 성공하였을 경우 회원정보를 담은 Vo 반환
	}
	
	@Override
	public int keepLogin(User user) { // 자동 로그인 체크 후 로그인 할 경우 자동 로그인 정보 저장
		return userDao.keepLogin(user);
	}
	
	@Override
	public User cookieLogin(String sessionId) { // 자동 로그인 시도
		return userDao.cookieLogin(sessionId);
	}
}
