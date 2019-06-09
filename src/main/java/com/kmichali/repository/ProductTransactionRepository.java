package com.kmichali.repository;

import com.kmichali.model.ProductTransaction;
import com.kmichali.model.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface ProductTransactionRepository extends CrudRepository<ProductTransaction,Long> {
    ProductTransaction findByTransaction(Transaction transaction);
}
