package com.kmichali.serviceImpl;

import com.kmichali.model.Product;
import com.kmichali.repository.ProductRepository;
import com.kmichali.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public Product save(Product entity) {
        entity.setName(entity.getName());

        return productRepository.save(entity);
    }

    @Override
    public Product update(Product entity) {
        return null;
    }

    @Override
    public void delete(Product entity) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public Product find(long id) {
        return null;
    }

    @Override
    public Iterable<Product> findAll() {
        return null;
    }
}
