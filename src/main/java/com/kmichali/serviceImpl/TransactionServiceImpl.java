package com.kmichali.serviceImpl;

import com.kmichali.model.Invoice;
import com.kmichali.model.ProductRaport;
import com.kmichali.model.Transaction;
import com.kmichali.repository.TransactionRepository;
import com.kmichali.service.TransactionService;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

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
        transactionRepository.delete(entity);
    }

    @Override
    public void delete(long id) {
        transactionRepository.delete(id);
    }

    @Override
    public Transaction find(long id) {
        return transactionRepository.findOne(id);
    }

    @Override
    public Iterable<Transaction> findAll() {
        return null;
    }

    public List<ProductRaport> findTransactionByProduct(String name) {
        List<ProductRaport> transactionList = entityManager.createQuery("Select " +
                "i.invoiceNumber as invoiceNumber, ROUND(t.amount,2) as transactionAmount,t.customer.name || ' ' ||  t.customer.surname as name " +
                ",d.issueDate as date, ROUND(t.storeAmount,2) as wholeAmount, t.type as type, t.priceNetto as price  " +
                "from Transaction t, Store s,Invoice i, Date d where t.store.id = s.id and " +
                "t.invoice.id = i.id and d.id = i.date.id and  s.name= :name ORDER BY d.sellDate desc").setParameter("name",name).
                unwrap( org.hibernate.query.Query.class )
                .setResultTransformer( Transformers.aliasToBean( ProductRaport.class ) )
                .getResultList();
        return transactionList;
    }


    @Override
    public List findByInvoice(String invoiceNumber, long id,String invoiceType) {
        String queryString = "Select t.id from Transaction t, Invoice i where t.invoice.id = :id and i.invoiceNumber = :invoiceNumber and i.invoiceType = :invoiceType";

       Query query = getEntityManager().createQuery(queryString);
       query.setParameter("invoiceNumber", invoiceNumber);
       query.setParameter("id", id);
        query.setParameter("invoiceType",invoiceType);
       List result=null;
       try {
            result = query.getResultList();
       }catch(NullPointerException e){

       }
       return result;
    }
}
