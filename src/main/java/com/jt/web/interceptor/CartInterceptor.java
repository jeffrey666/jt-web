package com.jt.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.jt.common.util.CookieUtils;
import com.jt.web.controller.UserController;
import com.jt.web.pojo.User;
import com.jt.web.service.UserService;
import com.jt.web.threadlocal.UserThreadLocal;

public class CartInterceptor implements HandlerInterceptor{
	@Autowired
	private UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String ticket = CookieUtils.getCookieValue(request, UserController.JT_TICKET);
		if(StringUtils.isNotEmpty(ticket)){
			User user = userService.queryUserByTicket(ticket);
			if(user!=null){
				//将对象放到本地线程中，方便后续使用
				UserThreadLocal.set(user);
			}else{
				UserThreadLocal.set(null);
			}
		}else{
			UserThreadLocal.set(null);
		}
		if(null == UserThreadLocal.getUserId()){
			response.sendRedirect("/user/login.html");
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}
	
}
