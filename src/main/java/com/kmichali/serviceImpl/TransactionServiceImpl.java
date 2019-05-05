package com.kmichali.serviceImpl;

import com.kmichali.model.Transaction;
import com.kmichali.repository.TransactionRepository;
import com.kmichali.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public Transaction save(Transaction entity) {
        entity.setAmount(entity.getAmount());
        entity.setPriceBrutto(entity.getPriceBrutto());
        entity.setTax(entity.getTax());
        entity.setPriceNetto(entity.getPriceNetto());

        return transactionRepository.save(entity);
    }

    @Override
    public Transaction update(Transaction entity) {
        return null;
    }

    @Override
    public void delete(Transaction entity) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public Transaction find(long id) {
        return null;
    }

    @Override
    public Iterable<Transaction> findAll() {
        return null;
    }
}
