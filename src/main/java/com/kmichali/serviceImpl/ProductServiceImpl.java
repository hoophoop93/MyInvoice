package com.kmichali.serviceImpl;

import com.kmichali.model.Product;
import com.kmichali.repository.ProductRepository;
import com.kmichali.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

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

    @Override
    public boolean checkIfExist(String name) {
        String queryString = "Select count(p.name) from Product p where LOWER(p.name) = :name";
        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("name",name.toLowerCase());

        long result = (long) query.getSingleResult();

        return result > 0;
    }
}
