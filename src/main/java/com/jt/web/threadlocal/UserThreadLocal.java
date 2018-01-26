package com.jt.web.threadlocal;

import com.jt.web.pojo.User;

public class UserThreadLocal {
	private static final ThreadLocal<User> USER= new ThreadLocal<User>();
	
	public static void set(User user){
		USER.set(user);
	}
	
	public static User get(){
		return USER.get();
	}
	
	public static Long getUserId(){
		if(null!=USER){
			if(null!=USER.get()){
				return USER.get().getId();
			}
		}
		return null;
	}
}
