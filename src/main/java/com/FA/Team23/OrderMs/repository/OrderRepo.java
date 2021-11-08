package com.FA.Team23.OrderMs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.FA.Team23.OrderMs.entity.Order;

public interface OrderRepo extends CrudRepository<Order, String>{

	public List<Order> findByBuyerId(String buyerId);

	public Optional<Order> findByOrderId(String orderId);

}
