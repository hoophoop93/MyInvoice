package com.kmichali.serviceImpl;

import com.kmichali.model.ProductTransaction;
import com.kmichali.model.Transaction;
import com.kmichali.repository.ProductTransactionRepository;
import com.kmichali.service.ProductService;
import com.kmichali.service.ProductTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductTransactionImpl implements ProductTransactionService {

    @Autowired
    ProductTransactionRepository productTransactionRepository;

    @Override
    public ProductTransaction save(ProductTransaction entity) {
        return productTransactionRepository.save(entity);
    }

    @Override
    public ProductTransaction update(ProductTransaction entity) {
        return null;
    }

    @Override
    public void delete(ProductTransaction entity) {
        productTransactionRepository.delete(entity);
    }

    @Override
    public void delete(long id) {
        productTransactionRepository.delete(id);
    }

    @Override
    public ProductTransaction find(long id) {
        return null;
    }

    @Override
    public Iterable<ProductTransaction> findAll() {
        return null;
    }

    @Override
    public ProductTransaction findByTransaction(Transaction transaction) {
        return productTransactionRepository.findByTransaction(transaction);
    }
}
