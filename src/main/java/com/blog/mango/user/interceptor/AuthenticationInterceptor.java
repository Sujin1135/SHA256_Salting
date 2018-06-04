package com.blog.mango.user.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import com.blog.mango.user.service.UserService;
import com.blog.mango.user.vo.User;

/**
 * 컨트롤러 전에 실행되는 인터셉터
 * @author user
 */
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	UserService userService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		
		Object loginSession = session.getAttribute("login"); // 세션을 가져온다
		if (loginSession == null) { // 세션이 비었을 경우
			Cookie cookie = WebUtils.getCookie(request, "loginCookie"); // loginCookie를 가져온다
			
			if (cookie != null) { // loginCookie가 비어있지 않을 경우
				User user = userService.cookieLogin(cookie.getValue()); // cookie를 이용하여 회원 정보를 받아와 Vo 객체에 담는다
				
				if (user != null) { // 정보를 성공적으로 받아왔을 경우
					session.setAttribute("login", user); // 세션에 정보를 저장한다
					return false;
				}
			}
		}
		
		return true;
	}
	
	@Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }
	
}
