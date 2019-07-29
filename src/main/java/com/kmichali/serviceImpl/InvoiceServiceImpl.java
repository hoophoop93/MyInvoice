package com.kmichali.serviceImpl;

import com.kmichali.model.Invoice;
import com.kmichali.repository.InvoiceRepository;
import com.kmichali.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    InvoiceRepository invoiceRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Invoice save(Invoice entity) {
        entity.setInvoiceNumber(entity.getInvoiceNumber());
        entity.setInvoiceType(entity.getInvoiceType());
        entity.setPaidType(entity.getPaidType());

        return invoiceRepository.save(entity);
    }

    @Override
    public Invoice update(Invoice entity) {
        return null;
    }

    @Override
    public void delete(Invoice entity) {
        invoiceRepository.delete(entity);
    }

    @Override
    public void delete(long id) {
        invoiceRepository.delete(id);
    }

    @Override
    public Invoice find(long id) {
        return invoiceRepository.findById(id);
    }

    @Override
    public Iterable<Invoice> findAll() {
        return null;
    }

    @Override
    public boolean countInvoiceNumber(String invoiceNumber,String invoiceType){
        String queryString="Select count(c.invoiceNumber) from Invoice c where c.invoiceNumber = :invoiceNumber and c.invoiceType = :invoiceType";
        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("invoiceNumber",invoiceNumber);
        query.setParameter("invoiceType",invoiceType);
        long result = (long) query.getSingleResult();
        return result > 0;
    }

    @Override
    public long getLastInvoiceNumeber(String invoiceType) {
            String queryString = "Select max(o.id) from Invoice o where LOWER(o.invoiceType) = :invoiceType ";

            Query query = getEntityManager().createQuery(queryString);
            query.setParameter("invoiceType", invoiceType.toLowerCase());
            long result=0;
            try {
                 result = (Long) query.getSingleResult();
            }catch(NullPointerException e){

            }
            return result;
    }

    @Override
    public Invoice findByinvoiceNumber(String invoiceNumber,String invoiceType){
        Invoice invoice = null;
        String queryString="Select c from Invoice c where c.invoiceNumber = :invoiceNumber and c.invoiceType = :invoiceType";

        Query query = getEntityManager().createQuery(queryString);
        query.setParameter("invoiceNumber",invoiceNumber);
        query.setParameter("invoiceType",invoiceType);
//       try {
//           invoice = (Invoice) query.getSingleResult();
//       }catch (NullPointerException e){
//
//       }
        List results = query.getResultList();
        if (!results.isEmpty())
            invoice = (Invoice) results.get(0);

        
        return invoice;
    }
}
