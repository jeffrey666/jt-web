package com.jt.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.Cart;
import com.jt.web.pojo.Item;
import com.jt.web.pojo.User;

@Service
public class CartService {
	@Autowired
	private ItemService itemService;
	@Autowired
	private HttpClientService httpService;
//	@PropertyConfig
	private String CART_URL="http://cart.jt.com";
	private static final ObjectMapper MAPPER =new ObjectMapper();
	
	public Boolean addItemToCart(Long itemId, User user) {
		//通过商品Id查询商品信息
		Item item = itemService.queryItemById(itemId);
		//调用购物车系统的API添加商品
		String url = CART_URL+"/cart/save";
		Map<String,String> params = new HashMap<String,String>();
		params.put("userId",String.valueOf(user.getId()));
		params.put("itemId",String.valueOf(itemId));
		params.put("itemTitle",item.getTitle());
		String[] images = item.getImages();
		if(null==images){
			params.put("itemImage", "");
		}else{
			params.put("itemImage", images[0]);
		}
		params.put("itemPrice",String.valueOf(item.getPrice()));
		params.put("num","1");//默认为1
		try{
			String jsonData = httpService.doPost(url, params,"utf-8");
			JsonNode jsonNode = MAPPER.readTree(jsonData);
			Integer status = jsonNode.get("status").intValue();
			if(status==200||status ==202){
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	@SuppressWarnings("unchecked")
	public List<Cart> queryCartByUser(User user) {
		String url = CART_URL+"/cart/query/"+user.getId();
		try{
			String jsonData = httpService.doGet(url);
			SysResult result = SysResult.formatToList(jsonData, Cart.class);
			return (List<Cart>)result.getData();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public Boolean deleteCart(User user, Long itemId) {
		String url = CART_URL+"/cart/delete/"+user.getId()+"/"+itemId;
		try{
			String jsonData = httpService.doGet(url);
			JsonNode jsonNode = MAPPER.readTree(jsonData);
			Integer status = jsonNode.get("status").intValue();
			if(status==200){
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	public boolean updateCart(User user, Long itemId, Integer num) {
		String url = CART_URL+"/cart/update/num/"+user.getId()+"/"+itemId+"/"+num;
		try{
			String jsonData = httpService.doGet(url);
			JsonNode jsonNode = MAPPER.readTree(jsonData);
			Integer status = jsonNode.get("status").intValue();
			if(status==200){
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}


}
