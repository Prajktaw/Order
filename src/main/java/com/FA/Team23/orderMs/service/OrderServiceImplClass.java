package com.FA.Team23.orderMs.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FA.Team23.orderMs.dto.CartDTOClass;
import com.FA.Team23.orderMs.dto.OrderDTOClass;
import com.FA.Team23.orderMs.dto.PlacedOrderDTOClass;
import com.FA.Team23.orderMs.dto.ProductDTOClass;
import com.FA.Team23.orderMs.entity.OrderClass;
import com.FA.Team23.orderMs.entity.OrderedProductsClass;
import com.FA.Team23.orderMs.exception.OrderExceptionClass;
import com.FA.Team23.orderMs.repository.OrderRepositoryClass;
import com.FA.Team23.orderMs.repository.OrderedProductsRepositoryClass;
import com.FA.Team23.orderMs.utility.PrimaryKeyClass;
import com.FA.Team23.orderMs.utility.StatusClass;
import com.FA.Team23.orderMs.validator.ValidateClass;

@Service(value = "orderServiceClass")
@Transactional
public class OrderServiceImplClass implements OrderServiceClass {
	
	//used to create order id adding after order "O" + index = O1, O2, O3 ...
	private static int index;
	static {
		index=100;
	}
	
	
	@Autowired
	private OrderedProductsRepositoryClass productOrderedRepository;
	
	@Autowired
	private OrderRepositoryClass orderRepository;
	
	
	//method to place order
		@Override
		public PlacedOrderDTOClass placeMyOrder(List<ProductDTOClass> productList, List<CartDTOClass> cartList, OrderDTOClass orderDTO) throws OrderExceptionClass {
			OrderClass ord = new OrderClass();
			ValidateClass.validateMyOrder(orderDTO);
			String id = "O" + index++;
			ord.setOrderId(id);
			ord.setAddress(orderDTO.getAddress());
			ord.setBuyerId(cartList.get(0).getBuyerId());
			ord.setDate(LocalDate.now());
			ord.setStatus(StatusClass.ORDER_PLACED.toString());	
			ord.setAmount(0f);
			List<OrderedProductsClass> productOrdered = new ArrayList<>();
			for(int i = 0; i<cartList.size();i++) {
				ValidateClass.validateMyStock(cartList.get(i), productList.get(i));			
				ord.setAmount(ord.getAmount()+(cartList.get(i).getQuantity()*productList.get(i).getPrice()));
				
				OrderedProductsClass prod = new OrderedProductsClass();
				prod.setSellerId(productList.get(i).getSellerId());
				prod.setPrimaryKeys(new PrimaryKeyClass(cartList.get(i).getBuyerId(),productList.get(i).getProdId()));
				prod.setQuantity(cartList.get(i).getQuantity());
				productOrdered.add(prod);				
			}		
			productOrderedRepository.saveAll(productOrdered);
			orderRepository.save(ord);
			PlacedOrderDTOClass orderPlaced = new PlacedOrderDTOClass();
			orderPlaced.setBuyerId(ord.getBuyerId());
			orderPlaced.setOrderId(ord.getOrderId());
			Integer reward = (int) (ord.getAmount()/100);		
			orderPlaced.setRewardPoints(reward);
			
			
			return orderPlaced;
		}

		
	//method to re-order 
		@Override
	public String reMyOrder(String buyerId, String orderId) throws OrderExceptionClass {
			Optional<OrderClass> opt = orderRepository.findByOrderId(orderId);
			OrderClass ord = opt.orElseThrow(()->new OrderExceptionClass("Order does not exist for the given buyer"));
			OrderClass reord = new OrderClass();
			String id = "O" + index++;
			reord.setOrderId(id);
			reord.setBuyerId(ord.getBuyerId());
			reord.setAmount(ord.getAmount());
			reord.setAddress(ord.getAddress());
			reord.setDate(LocalDate.now());
			reord.setStatus(ord.getStatus());
			
			orderRepository.save(reord);		
			return reord.getOrderId();
		}
	
	//method to store fetched orders from entity to dto
	@Override
	public List<OrderDTOClass> viewAllMyOrders() throws OrderExceptionClass {
		Iterable<OrderClass> ord = orderRepository.findAll();
		List<OrderDTOClass> dtoLst = new ArrayList<>();
		ord.forEach(order -> {
			OrderDTOClass orderDto = new OrderDTOClass();
			orderDto.setOrderId(order.getOrderId());
			orderDto.setBuyerId(order.getBuyerId());
			orderDto.setAmount(order.getAmount());
			orderDto.setAddress(order.getAddress());
			orderDto.setDate(order.getDate());
			orderDto.setStatus(order.getStatus());
			dtoLst.add(orderDto);			
		});
		if(dtoLst.isEmpty()) throw new OrderExceptionClass("No orders available");
		return dtoLst;
	}

	

	//method to view order by buyer id
	@Override
	public List<OrderDTOClass> viewOrdersByBuyer(String buyerId) throws OrderExceptionClass {
		List<OrderClass> ords = orderRepository.findByBuyerId(buyerId);
		if(ords.isEmpty()) throw new OrderExceptionClass("No orders available for given BuyerID");
		List<OrderDTOClass> dtoLst = new ArrayList<>();
		ords.forEach(ord->{
			OrderDTOClass orderDto = new OrderDTOClass();
			orderDto.setOrderId(ord.getOrderId());
			orderDto.setBuyerId(ord.getBuyerId());
			orderDto.setAmount(ord.getAmount());
			orderDto.setAddress(ord.getAddress());
			orderDto.setDate(ord.getDate());
			orderDto.setStatus(ord.getStatus());
			dtoLst.add(orderDto);
		});
		return dtoLst;
	}
	
	//method to view order by order id
	@Override
	public OrderDTOClass viewMyorder(String orderId) throws OrderExceptionClass {
		Optional<OrderClass> opt = orderRepository.findByOrderId(orderId);
		OrderClass ord = opt.orElseThrow(()->new OrderExceptionClass("Order does not exist"));
		OrderDTOClass orderDTO = new OrderDTOClass();
		orderDTO.setOrderId(ord.getOrderId());
		orderDTO.setBuyerId(ord.getBuyerId());
		orderDTO.setAmount(ord.getAmount());
		orderDTO.setAddress(ord.getAddress());
		orderDTO.setDate(ord.getDate());
		orderDTO.setStatus(ord.getStatus());		
		return orderDTO;
	}

	

}