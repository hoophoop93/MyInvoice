package com.kmichali.repository;

import com.kmichali.model.ProductTransaction;
import org.springframework.data.repository.CrudRepository;

public interface ProductTransactionRepository extends CrudRepository<ProductTransaction,Long> {
}
