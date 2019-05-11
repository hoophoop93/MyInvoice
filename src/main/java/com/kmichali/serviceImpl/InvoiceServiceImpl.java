package com.kmichali.serviceImpl;

import com.kmichali.model.Invoice;
import com.kmichali.repository.InvoiceRepository;
import com.kmichali.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    InvoiceRepository invoiceRepository;


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

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public Invoice find(long id) {
        return invoiceRepository.findById(id);
    }

    @Override
    public Iterable<Invoice> findAll() {
        return null;
    }
}
