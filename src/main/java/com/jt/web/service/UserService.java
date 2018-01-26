package com.jt.web.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.web.pojo.User;

@Service
public class UserService {
	@Autowired
	private HttpClientService httpService;
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final String SSO_URL = "http://sso.jt.com";
	public boolean doRegister(String username, String password, String phone) {
		Map<String,String> params = new HashMap<String,String>();
		params.put("username", username);
		params.put("password", password);
		params.put("phone", phone);
		//因为电话号码和邮箱是二选一，所以这里为了数据库的唯一性，将邮箱设置成用户名
		params.put("email", username);
		try {
			String jsonData = httpService.doPost(SSO_URL+"/user/register", params);
			JsonNode jsonNode = MAPPER.readTree(jsonData);
			if(jsonNode.get("status").intValue()==200){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public String doLogin(String username, String password) {
		Map<String,String> params = new HashMap<String,String>();
		params.put("u", username);
		params.put("p", password);
		try{
			String jsonData = httpService.doPost(SSO_URL+"/user/login", params);
			JsonNode jsonNode = MAPPER.readTree(jsonData);
			if(jsonNode.get("status").intValue()==200){
				return jsonNode.get("data").asText();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public User queryUserByTicket(String ticket) {
		String url = SSO_URL+"/user/query/"+ticket;
		User user = null;
		try {
			String jsonData = httpService.doGet(url,"utf-8");
			JsonNode jsonNode = MAPPER.readTree(jsonData);
			if(jsonNode.get("status").intValue()==200){
				String userData = jsonNode.get("data").asText();
				user = MAPPER.readValue(userData, User.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

}
