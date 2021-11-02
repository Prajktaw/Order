package com.FA.Team23.orderMs.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.FA.Team23.orderMs.dto.CartDTOClass;
import com.FA.Team23.orderMs.dto.OrderDTOClass;
import com.FA.Team23.orderMs.dto.PlacedOrderDTOClass;
import com.FA.Team23.orderMs.dto.ProductDTOClass;
import com.FA.Team23.orderMs.service.OrderServiceClass;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class OrderControllerClass {
	
	

	
	@Value("${product.uri}")
	String myProductUri;
	
	@Autowired
	private OrderServiceClass orderMyService;
	
	@Value("${user.uri}")
	String myUserUri;
	
	
	
	//view all orders of the user
	@RequestMapping(value = "/orderMS/viewAll", method = RequestMethod.GET)
	public ResponseEntity<List<OrderDTOClass>> viewAllMyOrder(){		
		try {
			List<OrderDTOClass> allOrd = orderMyService.viewAllMyOrders();
			return new ResponseEntity<>(allOrd,HttpStatus.OK);
		}
		catch(Exception e)
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
			
		}		
	}
	
	//view orders of specific buyer according given Id
	@RequestMapping(value = "/orderMS/viewOrders/{buyerId}", method = RequestMethod.GET)
	public ResponseEntity<List<OrderDTOClass>> viewsMyOrdersByBuyerId(@PathVariable String buyerId){		
		try {
			List<OrderDTOClass> allOrd = orderMyService.viewOrdersByBuyer(buyerId);
			return new ResponseEntity<>(allOrd,HttpStatus.OK);
		}
		catch(Exception e)
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		}		
	}
	
	//view orders by its order_id	
	@RequestMapping(value = "/orderMS/viewOrder/{orderId}", method = RequestMethod.GET)
	public ResponseEntity<OrderDTOClass> viewsMyOrderByOrderId(@PathVariable String orderId){		
		try {
			OrderDTOClass allOrd = orderMyService.viewMyorder(orderId);
			return new ResponseEntity<>(allOrd,HttpStatus.OK);
		}
		catch(Exception e)
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		}		
	}
	
	//to re-order an order order_id by its buyer buyer_id
	@RequestMapping(value = "/orderMS/reOrder/{buyerId}/{orderId}", method = RequestMethod.POST)
	public ResponseEntity<String> reMyOrder(@PathVariable String buyerId, @PathVariable String orderId){
		
		try {
			
			String Myid = orderMyService.reMyOrder(buyerId,orderId);
			return new ResponseEntity<>("Order ID: "+Myid,HttpStatus.ACCEPTED);
		}
		catch(Exception e)
		{
			return new ResponseEntity<>(e.getMessage(),HttpStatus.UNAUTHORIZED);
		}		
	}
	
	//to add product product_id in cart of buyer buyer_id
	@RequestMapping(value = "/orderMS/addToCart/{buyerId}/{prodId}/{quantity}", method = RequestMethod.POST)
	public ResponseEntity<String> addToMyCart(@PathVariable String buyerId, @PathVariable String prodId,@PathVariable Integer quantity){
		
		try {
			
			
			String Msg = new RestTemplate().postForObject(myUserUri+"/userMS/buyer/cart/add/"+buyerId+"/"+prodId+"/"+quantity, null, String.class);

			return new ResponseEntity<>(Msg,HttpStatus.ACCEPTED);
		}
		catch(Exception e)
		{
			String newMsg = "There was some error";
			if(e.getMessage().equals("404 null"))
			{
				newMsg = "There are no PRODUCTS for the given product ID";
			}
			return new ResponseEntity<>(newMsg,HttpStatus.UNAUTHORIZED);
		}		
	}
	
	//to remove product product id from cart of buyer buyer id
	@RequestMapping(value = "/orderMS/removeFromCart/{buyerId}/{prodId}", method = RequestMethod.POST)
	public ResponseEntity<String> removeFromTheCart(@PathVariable String buyerId, @PathVariable String prodId){
		
		try {
			
			String Msg = new RestTemplate().postForObject(myUserUri+"/userMS/buyer/cart/remove/"+buyerId+"/"+prodId, null, String.class);
			
			return new ResponseEntity<>(Msg,HttpStatus.ACCEPTED);
		}
		catch(Exception e)
		{
			String newMsg = "There was some error";
			if(e.getMessage().equals("404 null"))
			{
				newMsg = "There are no PRODUCTS for the given product ID";
			}
			return new ResponseEntity<>(newMsg,HttpStatus.UNAUTHORIZED);
		}		
	}
	
	//placing order by buyer(buyer id)
		@RequestMapping(value = "/orderMS/placeOrder/{buyerId}", method = RequestMethod.POST)
		public ResponseEntity<String> placeMyOrder(@PathVariable String buyerId, @RequestBody OrderDTOClass order){
			
			try {
				
				ObjectMapper map = new ObjectMapper();
				List<ProductDTOClass> product = new ArrayList<>();
				List<CartDTOClass> cartList = map.convertValue(
						new RestTemplate().getForObject(myUserUri+"/userMS/buyer/cart/get/" + buyerId, List.class), 
					    new TypeReference<List<CartDTOClass>>(){}
					);
				
				cartList.forEach(item ->{
					ProductDTOClass prod = new RestTemplate().getForObject(myProductUri+"/prodMS/getById/" +item.getProdId(),ProductDTOClass.class) ; //getByProdId/{productId}
					System.out.println(prod.getDescription());
					product.add(prod);
				});
				
				PlacedOrderDTOClass orderPlaced = orderMyService.placeMyOrder(product,cartList,order);
				cartList.forEach(item->{
					new RestTemplate().getForObject(myProductUri+"/prodMS/updateStock/" +item.getProdId()+"/"+item.getQuantity(), boolean.class) ;
					new RestTemplate().postForObject(myUserUri+"/userMS/buyer/cart/remove/"+buyerId+"/"+item.getProdId(),null, String.class);
				});			
				
				new RestTemplate().getForObject(myUserUri+"/userMS/updateRewardPoints/"+buyerId+"/"+orderPlaced.getRewardPoints() , String.class);
				
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
		
	
	

}
