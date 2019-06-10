package com.kmichali.service;

import com.kmichali.generic.GenericService;
import com.kmichali.model.Product;

import javax.persistence.GeneratedValue;

public interface ProductService extends GenericService<Product> {

     boolean checkIfExist(String name);
     Product findByName(String name);
}
