package com.kmichali.service;

import com.kmichali.generic.GenericService;
import com.kmichali.model.Invoice;
import com.kmichali.model.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionService extends GenericService<Transaction> {
    List findByInvoice(String invoiceNumber,long id);
}
