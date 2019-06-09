package com.kmichali.repository;

import com.kmichali.model.Invoice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends CrudRepository<Invoice,Long> {

    Invoice findById(long id);
    Invoice findByinvoiceNumber(String invoiceNumber);
}
