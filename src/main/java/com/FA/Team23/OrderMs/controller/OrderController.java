package com.FA.Team23.OrderMs.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.FA.Team23.OrderMs.dto.CartDTO;
import com.FA.Team23.OrderMs.dto.OrderDTO;
import com.FA.Team23.OrderMs.dto.OrderPlacedDTO;
import com.FA.Team23.OrderMs.dto.ProductDTO;
import com.FA.Team23.OrderMs.exception.ExceptionOrder;
import com.FA.Team23.OrderMs.service.OrderService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
//	@Autowired
//	DiscoveryClient client;
	
	@Value("${user.uri}")
	String userUri;
	
	@Value("${product.uri}")
	String productUri;
	
	@PostMapping(value = "/orderMS/placeOrder/{buyerId}")
	public ResponseEntity<String> placeOrder(@PathVariable String buyerId, @RequestBody OrderDTO order){
		
		try {
//			
			ObjectMapper mapper = new ObjectMapper();  
			//Object obj = new Object();
			//obj.
			List<ProductDTO> productList = new ArrayList<>();
			List<CartDTO> cartList = mapper.convertValue(
					new RestTemplate().getForObject(userUri+"/userMS/buyer/cart/get/" + buyerId, List.class), 
				    new TypeReference<List<CartDTO>>(){}
				);
			
//			
			cartList.forEach(item ->{
				ProductDTO prod = new RestTemplate().getForObject(productUri+"/prodMS/getById/" +item.getProdId(),ProductDTO.class) ; //getByProdId/{productId}
				//System.out.println(prod);
				productList.add(prod);
			});
			//System.out.println(productList);
			//System.out.println(cartList);
			//System.out.println(order);
			OrderPlacedDTO orderPlaced = orderService.placeOrder(productList,cartList,order);
			//System.out.println("after");
			cartList.forEach(item->{
				//System.out.println("after111");
				
				new RestTemplate().put(productUri+"/prodMS/updateStock/" +item.getProdId()+"/"+item.getQuantity(),null) ;
				//System.out.println("after1112222");
				new RestTemplate().delete(userUri+"/userMS/buyer/cart/remove/"+buyerId+"/"+item.getProdId());
				//new RestTemplate().postForObject(userUri+"/userMS/buyer/cart/remove/"+buyerId+"/"+item.getProdId(),null, String.class);
			});			
			//System.out.println("before111");
			new RestTemplate().postForObject(userUri+"/userMS/updateRewardPoints/"+buyerId+"/"+orderPlaced.getRewardPoints() ,null, String.class);
			//System.out.println("before");
			return new ResponseEntity<>(orderPlaced.getOrderId(),HttpStatus.ACCEPTED);
		}
		catch(Exception e)
		{
			String newMsg = "There was some error";
			if(e.getMessage().equals("404 null"))
			{
				newMsg = "Error while placing the order";
			}
			return new ResponseEntity<>(newMsg,HttpStatus.UNAUTHORIZED);
		}		
		
	}
	
	@GetMapping(value = "/orderMS/viewAll")
	public ResponseEntity<List<OrderDTO>> viewAllOrder(){		
		try {
			List<OrderDTO> allOrders = orderService.viewAllOrders();
			return new ResponseEntity<>(allOrders,HttpStatus.OK);
		}
		catch(Exception e)
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		}		
	}
	
	@GetMapping(value = "/orderMS/viewOrders/{buyerId}")
	public ResponseEntity<List<OrderDTO>> viewsOrdersByBuyerId(@PathVariable String buyerId){		
		try {
			List<OrderDTO> allOrders = orderService.viewOrdersByBuyer(buyerId);
			return new ResponseEntity<>(allOrders,HttpStatus.OK);
		}
		catch(Exception e)
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		}		
	}
	
	@GetMapping(value = "/orderMS/viewOrder/{orderId}")
	public ResponseEntity<OrderDTO> viewsOrderByOrderId(@PathVariable String orderId){		
		try {
			OrderDTO allOrders = orderService.viewOrder(orderId);
			return new ResponseEntity<>(allOrders,HttpStatus.OK);
		}
		catch(Exception e)
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		}		
	}
	
	
	@PostMapping(value = "/orderMS/reOrder/{buyerId}/{orderId}")
	public ResponseEntity<String> reOrder(@PathVariable String buyerId, @PathVariable String orderId){
		
		try {
			
			String id = orderService.reOrder(buyerId,orderId);
			return new ResponseEntity<>("Order ID: "+id,HttpStatus.ACCEPTED);
		}
		catch(Exception e)
		{
			return new ResponseEntity<>(e.getMessage(),HttpStatus.UNAUTHORIZED);
		}		
	}
	
	@GetMapping(value = "/order/status/{orderId}/{status}")
	public ResponseEntity<String> StatusUpdate(@PathVariable String orderId, @PathVariable String status) {
		try {

			orderService.StatusOrderUpdate(orderId, status);
			return new ResponseEntity<String>("Status updated successfully!", HttpStatus.OK);
		} catch (ExceptionOrder e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		}
	}
	
	
	
	

}
