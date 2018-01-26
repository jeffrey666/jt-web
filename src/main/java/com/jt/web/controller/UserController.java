package com.jt.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.util.CookieUtils;
import com.jt.common.vo.SysResult;
import com.jt.web.service.UserService;
import com.jt.web.threadlocal.UserThreadLocal;

@Controller
@RequestMapping("user")
public class UserController {
	public static final String JT_TICKET = "JT_TICKET";
	@Autowired
	private UserService userService;
	@RequestMapping("register")
	public String registerIndex(){
		return "register";
	}
	
	@RequestMapping("login")
	public String loginIndex(){
		return "login";
	}
	
	@RequestMapping("doRegister")
	@ResponseBody
	public SysResult doRegister(String username,String password,String phone){
		boolean boo = userService.doRegister(username,password,phone);
		if(!boo){
			return SysResult.build(201, "注册失败"); 
		}
		return SysResult.oK();
	}
	@RequestMapping("doLogin")
	@ResponseBody
	public SysResult doLogin(String username,String password,HttpServletRequest request,HttpServletResponse response){
		String ticket = userService.doLogin(username,password);
		if(ticket!=null){
			CookieUtils.setCookie(request, response,JT_TICKET, ticket,60*60*24);
			return SysResult.oK("登录成功");
		}
		return SysResult.build(201,"登录失败");
	}
	
	@RequestMapping("logout")
	public String doLogout(HttpServletRequest request,HttpServletResponse response){
		CookieUtils.deleteCookie(request, response, JT_TICKET);
		UserThreadLocal.set(null);
		return "index";
	}
}
