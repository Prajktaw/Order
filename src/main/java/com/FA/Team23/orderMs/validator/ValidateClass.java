package com.FA.Team23.orderMs.validator;

import com.FA.Team23.orderMs.dto.CartDTOClass;
import com.FA.Team23.orderMs.dto.OrderDTOClass;
import com.FA.Team23.orderMs.dto.ProductDTOClass;
import com.FA.Team23.orderMs.exception.OrderExceptionClass;

public class ValidateClass {
	
	public static void validateMyOrder(OrderDTOClass order) throws OrderExceptionClass {
		
		//Address must be within 100 characters
		if(!validateMyAddress(order.getAddress()))
			throw new OrderExceptionClass("Invalid number of address characters.");		
		
	}
	
	public static void validateMyStock(CartDTOClass cart, ProductDTOClass product) throws OrderExceptionClass {
				
		//Check if the required quantity of product is available in the stock
		if(!validateMyStock(product.getStock(),cart.getQuantity()))
			throw new OrderExceptionClass("Insufficient stock");	
	}
	
	
	private static boolean validateMyAddress(String address) {		
		return (address.length()>0 &&address.length()<100);		
	}
	
	private static boolean validateMyStock(Integer stock, Integer quantity) {		
		return stock>=quantity;		
	}
}
