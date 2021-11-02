package com.FA.Team23.orderMs.repository;

import org.springframework.data.repository.CrudRepository;

import com.FA.Team23.orderMs.entity.OrderedProductsClass;
import com.FA.Team23.orderMs.utility.PrimaryKeyClass;


//This class Extends all the properties of CrudeRepositry
public interface OrderedProductsRepositoryClass extends CrudRepository<OrderedProductsClass, PrimaryKeyClass>{

}

