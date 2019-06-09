package com.kmichali.service;

import com.kmichali.generic.GenericService;
import com.kmichali.model.ProductTransaction;
import com.kmichali.model.Transaction;

public interface ProductTransactionService extends GenericService<ProductTransaction> {
    ProductTransaction findByTransaction(Transaction transaction);
}
