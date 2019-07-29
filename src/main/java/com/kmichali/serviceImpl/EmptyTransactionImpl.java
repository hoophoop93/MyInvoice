package com.kmichali.serviceImpl;

import com.kmichali.model.EmptyTransaction;
import com.kmichali.model.ProductRaport;
import com.kmichali.repository.EmptyTransactionRepository;
import com.kmichali.service.EmptyTransactionService;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Service
public class EmptyTransactionImpl implements EmptyTransactionService {



    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Autowired
    EmptyTransactionRepository emptyTransactionRepository;

    @Override
    public EmptyTransaction save(EmptyTransaction entity) {
        return emptyTransactionRepository.save(entity);
    }

    @Override
    public EmptyTransaction update(EmptyTransaction entity) {
        return null;
    }

    @Override
    public void delete(EmptyTransaction entity) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public EmptyTransaction find(long id) {
        return null;
    }

    @Override
    public Iterable<EmptyTransaction> findAll() {
        return null;
    }
    public List<ProductRaport> findEmptyTransactionByProduct(String productName) {
        List<ProductRaport> transactionList = entityManager.createQuery("Select " +
                "invoiceNumber as invoiceNumber, transactionAmount as transactionAmount" +
                ",customerName as name, amount as wholeAmount " +
                ",date as date, type as type,price as price " +
                "from EmptyTransaction where productName= :productName").setParameter("productName",productName).
                unwrap( org.hibernate.query.Query.class )
                .setResultTransformer( Transformers.aliasToBean( ProductRaport.class ) )
                .getResultList();
        return transactionList;
    }
}
