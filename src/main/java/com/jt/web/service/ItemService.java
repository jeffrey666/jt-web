package com.jt.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.common.service.RedisService;
import com.jt.web.pojo.Item;
import com.jt.web.pojo.ItemDesc;

@Service
public class ItemService {
	@Autowired
	private HttpClientService httpService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private static final ObjectMapper MAPPER = new ObjectMapper();
	public Item queryItemById(Long itemId){
		//商品存入redis的key值
		String ITEM_KEY ="ITEM_"+itemId;
		String jsonData = redisService.get(ITEM_KEY);
		try {
			if(StringUtils.isNotEmpty(jsonData)){//缓存中有
				Item item = MAPPER.readValue(jsonData, Item.class);
				return item;
			}else{
				String url="http://manage.jt.com/items/"+itemId;
				//使用httpclient实现跨域请求
				jsonData = httpService.doGet(url,"utf-8");
				//存入redis缓存中，并设置过期时间7天
				redisService.set(ITEM_KEY, jsonData, 60*60*24*7);
				Item item = MAPPER.readValue(jsonData, Item.class);
				return item;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//查询商品详情
	public ItemDesc queryItemDescById(Long itemId) {
		String ITEM_DESC_KEY= "ITEM_DESC_"+itemId;
		String itemDescData = redisService.get(ITEM_DESC_KEY);
		ItemDesc itemDesc = null;
		try {
			if(StringUtils.isNotEmpty(itemDescData)){
				itemDesc = MAPPER.readValue(itemDescData, ItemDesc.class);
				return itemDesc;
			}else{
				String url="http://manage.jt.com/desc/"+itemId;
				itemDescData = httpService.doGet(url,"utf-8");
				itemDesc = MAPPER.readValue(itemDescData, ItemDesc.class);
				//后台已经存入缓存中，这里无须再次存入缓存中
				return itemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
