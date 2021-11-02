package com.FA.Team23.orderMs.service;

import java.util.List;

import com.FA.Team23.orderMs.dto.CartDTOClass;
import com.FA.Team23.orderMs.dto.OrderDTOClass;
import com.FA.Team23.orderMs.dto.PlacedOrderDTOClass;
import com.FA.Team23.orderMs.dto.ProductDTOClass;
import com.FA.Team23.orderMs.exception.OrderExceptionClass;

//implementation of service Interface
public interface OrderServiceClass {
	
	//viewing all orders
	public List<OrderDTOClass> viewAllMyOrders() throws OrderExceptionClass;

	//placing the order
	public PlacedOrderDTOClass placeMyOrder(List<ProductDTOClass> productList, List<CartDTOClass> cartList, OrderDTOClass order) throws OrderExceptionClass;

	//view order of a particular buyer according to its id
	public List<OrderDTOClass> viewOrdersByBuyer(String buyerId)throws OrderExceptionClass;

	//view order by its order_id
	public OrderDTOClass viewMyorder(String orderId) throws OrderExceptionClass;

	//re-order by its order_id and buyer_id
	public String reMyOrder(String buyerId, String orderId) throws OrderExceptionClass;


}
