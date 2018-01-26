package com.jt.web.controller;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jt.common.vo.SysResult;
import com.jt.web.pojo.Cart;
import com.jt.web.pojo.Order;
import com.jt.web.pojo.User;
import com.jt.web.service.CartService;
import com.jt.web.service.OrderService;
import com.jt.web.threadlocal.UserThreadLocal;

@Controller
@RequestMapping("order")
public class OrderController {
	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;
	//创建订单
	@RequestMapping("create")
	public ModelAndView createOrder(){
		User user = UserThreadLocal.get();
		//查询该用户的购物车
		List<Cart> carts = this.cartService.queryCartByUser(user);
		ModelAndView mv = new ModelAndView("order-cart");
		mv.addObject("carts",carts);
		return mv;
	}
	
	//提交订单
	@RequestMapping("submit")
	@ResponseBody
	public SysResult create(Order order){
		User user = UserThreadLocal.get();
		order.setUserId(user.getId());
		order.setBuyerNick(user.getUsername());
		String orderNo = this.orderService.createOrder(order);
		return SysResult.oK(orderNo);
	}
	
	@RequestMapping("success")
	public String success(@RequestParam(value="id")String orderId,Model model){
		Order order = this.orderService.queryOrderById(orderId);
		model.addAttribute("order",order);
		//规则，预计两天后到达
		//joda-time工具组件实现
		model.addAttribute("date",new DateTime().plusDays(2).toString("MM月dd日"));
		return "success";
	}
}
