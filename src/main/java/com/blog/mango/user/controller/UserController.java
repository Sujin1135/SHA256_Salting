package com.blog.mango.user.controller;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.CookieGenerator;

import com.blog.mango.module.PrintWriterUtil;
import com.blog.mango.user.annotation.MangoService;
import com.blog.mango.user.exception.UserAlreadyRegisteredException;
import com.blog.mango.user.exception.UserFailLoginException;
import com.blog.mango.user.service.UserService;
import com.blog.mango.user.validator.UserLoginValidate;
import com.blog.mango.user.validator.UserJoinValidate;
import com.blog.mango.user.vo.User;

@Controller
public class UserController {
	
	@Autowired
	@MangoService
	private UserService userService;
	
	@Autowired
	private UserJoinValidate userValidate;
	
	@Autowired
	private UserLoginValidate userLoginValidate;
	
	// 2개의 InitBinder 생성한다 하나는 회원가입용 다른 하나는 로그인용
	@InitBinder("join")
	public void joinBinder(WebDataBinder binder) {
		binder.addValidators(userValidate);
	}
	
	@InitBinder("login")
	public void loginBinder(WebDataBinder binder) {
		binder.addValidators(userLoginValidate);
	}
	
	// 회원가입 GET
	@RequestMapping(value="/user/join", method=RequestMethod.GET)
	public String joinView(Model model) throws Exception {
		return "user/joinView";
	}
	// 회원가입 POST
	@ResponseBody
	@RequestMapping(value="/user/join", method=RequestMethod.POST)
	public HashMap<String, Object> joinCheck(@ModelAttribute("join") @Valid User user,
			BindingResult result) throws Exception {
		String resultMessage = ""; // 결과 메세지를 담을 객체
		HashMap<String, Object> resultMap = new HashMap<String, Object>(); // 리턴 해쉬맵
		
		if(result.hasErrors()) { // 벨리데이션 체크에서 걸렸을 경우 해당 에러 메세지를 반환한다
			resultMessage = result.getFieldError().getDefaultMessage();
			resultMap.put("fail", resultMessage);
			return resultMap;
		} else { // 벨리데이션 체크에서 이상 없을경우
			resultMessage = "회원가입이 완료되었습니다.";
		}
		
		try {
			userService.register(user); // 회원 등록
			resultMap.put("success", resultMessage);
		} catch (UserAlreadyRegisteredException e) { // 회원 아이디가 이미 존재할 경우
			resultMap.put("fail", e.getMessage());
		}
		
		return resultMap; // 성공 혹은 실패 여부 메세지 반환
	}
	
	// 로그인 GET
	@RequestMapping(value="/user/login", method=RequestMethod.GET)
	public String loginView(Model model) throws Exception {
		return "user/loginView";
	}
	
	// 로그인 POST
	@RequestMapping(value="/user/login", method=RequestMethod.POST)
	public void loginView(HttpSession session, HttpServletResponse response, HttpServletRequest request, 
			ModelAndView view, @ModelAttribute("login") @Valid User user, BindingResult result) throws Exception {
		PrintWriter out = response.getWriter();
		String resultMessage = ""; // 결과 메세지를 담을 객체
		
		if (result.hasErrors()) { // 벨리데이션 체크에서 걸렸을 경우 해당 에러 메세지를 반환한다
			resultMessage = result.getFieldError().getDefaultMessage();
		} else { // 벨리데이션 체크에서 이상없을 경우
			resultMessage = "로그인 하였습니다.";
		}
		
		try {
			User loginUser = userService.login(user); // 로그인 시도, 성공하면 회원 정보를 loginUser에 담는다
			session.setAttribute("login", loginUser);
			
			// 자동 로그인이 true면 sessionId와 로그인 유지기간을 지정해준다
			if (user.getKeepLogin()) {
				int amount = 60 * 60 * 24 * 7; // 7일
				String sessionId = session.getId(); // 세션 고유id
				User keepLoginUser =
						new User(user.getEmail(), sessionId, new Date(System.currentTimeMillis() + (1000 * amount)));
				
				Cookie cookie = new Cookie("loginCookie", sessionId); // 쿠키 생성
				cookie.setPath("/"); // 쿠키가 적용되는 path 설정
				cookie.setMaxAge(amount); // 쿠키 유효기간 설정
				response.addCookie(cookie); // 브라우저에 쿠키 저장
				
				userService.keepLogin(keepLoginUser);
			}
			
			out.println(PrintWriterUtil.goScript(resultMessage, request.getContextPath()+ "/index"));
			
		} catch (UserFailLoginException e) { // userService.login 중 아이디나 비밀번호가 틀릴 경우
			resultMessage = e.getMessage(); // 예외 메세지를 resultMessage 에 담는다
			out.println(PrintWriterUtil.goScript(resultMessage, request.getContextPath()+ "/user/login")); // 예외 메세지와 이동할 경로를 스크립트로 view에 보낸다
		} finally {
			out.flush();
			out.close();
		}
		
	}

}
