package com.FA.Team23.OrderMs.service;

import java.util.List;

import com.FA.Team23.OrderMs.dto.CartDTO;
import com.FA.Team23.OrderMs.dto.OrderDTO;
import com.FA.Team23.OrderMs.dto.OrderPlacedDTO;
import com.FA.Team23.OrderMs.dto.ProductDTO;
import com.FA.Team23.OrderMs.exception.ExceptionOrder;

public interface OrderService {
	
	public List<OrderDTO> viewAllOrders() throws ExceptionOrder;

	public OrderPlacedDTO placeOrder(List<ProductDTO> productList, List<CartDTO> cartList, OrderDTO order) throws ExceptionOrder;

	public List<OrderDTO> viewOrdersByBuyer(String buyerId)throws ExceptionOrder;

	public OrderDTO viewOrder(String orderId) throws ExceptionOrder;

	public String reOrder(String buyerId, String orderId) throws ExceptionOrder;
	
	public void StatusOrderUpdate(String orderId, String status) throws ExceptionOrder;


}
