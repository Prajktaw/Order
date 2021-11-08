package com.FA.Team23.OrderMs.repository;

import org.springframework.data.repository.CrudRepository;

import com.FA.Team23.OrderMs.entity.ProductsOrdered;
import com.FA.Team23.OrderMs.utility.PrimaryKey;

public interface ProductsOrderedRepo extends CrudRepository<ProductsOrdered, PrimaryKey>{

}
