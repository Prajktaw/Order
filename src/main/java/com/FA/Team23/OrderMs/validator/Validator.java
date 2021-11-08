package com.FA.Team23.OrderMs.validator;

import com.FA.Team23.OrderMs.dto.CartDTO;
import com.FA.Team23.OrderMs.dto.OrderDTO;
import com.FA.Team23.OrderMs.dto.ProductDTO;
import com.FA.Team23.OrderMs.exception.ExceptionOrder;

public class Validator {
	
	public static void validateOrder(OrderDTO order) throws ExceptionOrder {
		
		//Address must be within 100 characters
		if(!validateAddress(order.getAddress()))
			throw new ExceptionOrder("Invalid number of address characters.");		
		
	}
	
	public static void validateStock(CartDTO cart, ProductDTO product) throws ExceptionOrder {
				
		//Check if the required quantity of product is available in the stock
		if(!validateStock(product.getStock(),cart.getQuantity()))
			throw new ExceptionOrder("Insufficient stock");	
	}
	
	
	private static boolean validateAddress(String address) {		
		return (address.length()>0 &&address.length()<100);		
	}
	
	private static boolean validateStock(Integer stock, Integer quantity) {		
		return stock>=quantity;		
	}
}
