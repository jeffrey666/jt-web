package com.jt.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.web.pojo.Order;

@Service
public class OrderService {
	@Autowired
	private HttpClientService httpService;
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final String ORDER_URL = "http://order.jt.com";
	public String createOrder(Order order) {
		String url =ORDER_URL+"/order/create";
		try{
			String json = MAPPER.writeValueAsString(order);
			String jsonData = httpService.doPostJson(url, json);
			return jsonData;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public Order queryOrderById(String orderId) {
		String url =ORDER_URL+"/order/query/"+orderId;
		try{
			String jsonData = httpService.doGet(url,"utf-8");
			if(jsonData!=null){
				Order order = MAPPER.readValue(jsonData, Order.class);
				return order;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
