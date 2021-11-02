package com.FA.Team23.orderMs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.FA.Team23.orderMs.entity.OrderClass;

//This class Extends all the properties of CrudeRepositry
public interface OrderRepositoryClass extends CrudRepository<OrderClass, String>{

	public List<OrderClass> findByBuyerId(String buyerId); // find buyer by its id
	public Optional<OrderClass> findByOrderId(String orderId); // find the order by its order id

}
