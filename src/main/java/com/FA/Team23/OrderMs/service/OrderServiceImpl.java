package com.FA.Team23.OrderMs.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.FA.Team23.OrderMs.dto.CartDTO;
import com.FA.Team23.OrderMs.dto.OrderDTO;
import com.FA.Team23.OrderMs.dto.OrderPlacedDTO;
import com.FA.Team23.OrderMs.dto.ProductDTO;
import com.FA.Team23.OrderMs.entity.Order;
import com.FA.Team23.OrderMs.entity.ProductsOrdered;
import com.FA.Team23.OrderMs.exception.ExceptionOrder;
import com.FA.Team23.OrderMs.repository.OrderRepo;
import com.FA.Team23.OrderMs.repository.ProductsOrderedRepo;
import com.FA.Team23.OrderMs.utility.PrimaryKey;
import com.FA.Team23.OrderMs.utility.Status;
import com.FA.Team23.OrderMs.validator.Validator;

@Service(value = "orderService")
@Transactional
public class OrderServiceImpl implements OrderService {
	
	private static int o;
	
	@Autowired
	private OrderRepo orderRepository;
	
	@Autowired
	private ProductsOrderedRepo prodOrderedRepository;
	
	static {
		o=100;
	}

	@Override
	public List<OrderDTO> viewAllOrders() throws ExceptionOrder {
		Iterable<Order> orders = orderRepository.findAll();
		List<OrderDTO> dtoList = new ArrayList<>();
		orders.forEach(order -> {
			OrderDTO odto = new OrderDTO();
			odto.setOrderId(order.getOrderId());
			odto.setBuyerId(order.getBuyerId());
			odto.setAmount(order.getAmount());
			odto.setAddress(order.getAddress());
			odto.setDate(order.getDate());
			odto.setStatus(order.getStatus());
			dtoList.add(odto);			
		});
		if(dtoList.isEmpty()) throw new ExceptionOrder("No orders available");
		return dtoList;
	}

	@Override
	public OrderPlacedDTO placeOrder(List<ProductDTO> productList, List<CartDTO> cartList, OrderDTO orderDTO)
			throws ExceptionOrder {
		Order order = new Order(); //Entity 
		Validator.validateOrder(orderDTO);
		String id = "O" + o++;
		order.setOrderId(id);
		order.setAddress(orderDTO.getAddress());
		order.setBuyerId(cartList.get(0).getBuyerId());
		order.setDate(LocalDate.now());
		order.setStatus(Status.ORDER_PLACED.toString());	
		order.setAmount(0f);
		List<ProductsOrdered> productsOrdered = new ArrayList<>();
		for(int i = 0; i<cartList.size();i++) {
			Validator.validateStock(cartList.get(i), productList.get(i));			
			order.setAmount(order.getAmount()+(cartList.get(i).getQuantity()*productList.get(i).getPrice()));
			
			ProductsOrdered prodO = new ProductsOrdered();
			prodO.setSellerId(productList.get(i).getSellerId());
			prodO.setPrimaryKeys(new PrimaryKey(cartList.get(i).getBuyerId(),productList.get(i).getProdId()));
			prodO.setQuantity(cartList.get(i).getQuantity());
			productsOrdered.add(prodO);				
		}		
		prodOrderedRepository.saveAll(productsOrdered);  // using to store the history data
		orderRepository.save(order); // using to store in date 
		OrderPlacedDTO orderPlaced = new OrderPlacedDTO();
		orderPlaced.setBuyerId(order.getBuyerId());
		orderPlaced.setOrderId(order.getOrderId());
		Integer rewardPts = (int) (order.getAmount()/100);		
		orderPlaced.setRewardPoints(rewardPts);
		
		
		return orderPlaced;
	}

	@Override
	public List<OrderDTO> viewOrdersByBuyer(String buyerId) throws ExceptionOrder {
		List<Order> orders = orderRepository.findByBuyerId(buyerId);
		if(orders.isEmpty()) throw new ExceptionOrder("No orders available for given BuyerID");
		List<OrderDTO> dtoList = new ArrayList<>();
		orders.forEach(order->{
			OrderDTO odto = new OrderDTO();
			odto.setOrderId(order.getOrderId());
			odto.setBuyerId(order.getBuyerId());
			odto.setAmount(order.getAmount());
			odto.setAddress(order.getAddress());
			odto.setDate(order.getDate());
			odto.setStatus(order.getStatus());
			dtoList.add(odto);
		});
		return dtoList;
	}

	@Override
	public OrderDTO viewOrder(String orderId) throws ExceptionOrder {
		Optional<Order> optional = orderRepository.findByOrderId(orderId);
		Order order = optional.orElseThrow(()->new ExceptionOrder("Order does not exist"));
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setOrderId(order.getOrderId());
		orderDTO.setBuyerId(order.getBuyerId());
		orderDTO.setAmount(order.getAmount());
		orderDTO.setAddress(order.getAddress());
		orderDTO.setDate(order.getDate());
		orderDTO.setStatus(order.getStatus());		
		return orderDTO;
	}

	@Override
	public String reOrder(String buyerId, String orderId) throws ExceptionOrder {
		Optional<Order> optional = orderRepository.findByOrderId(orderId);
		Order order = optional.orElseThrow(()->new ExceptionOrder("Order does not exist for the given buyer"));
		Order reorder = new Order();
		String id = "O" + o++;
		reorder.setOrderId(id);
		reorder.setBuyerId(order.getBuyerId());
		reorder.setAmount(order.getAmount());
		reorder.setAddress(order.getAddress());
		reorder.setDate(LocalDate.now());
		reorder.setStatus(order.getStatus());
		
		orderRepository.save(reorder);		
		return reorder.getOrderId();
	}

	
	@Override
	public void StatusOrderUpdate(String orderId, String status) throws ExceptionOrder {
		Optional<Order> order = orderRepository.findById(orderId);
		if (order.isPresent() == true) {
			order.get().setStatus(status);
			orderRepository.save(order.get());
		} else
			throw new ExceptionOrder("Order not found!!");

	}
}
