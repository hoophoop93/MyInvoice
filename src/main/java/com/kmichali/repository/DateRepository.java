package com.kmichali.repository;

import com.kmichali.model.Date;
import com.kmichali.model.Invoice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DateRepository extends CrudRepository<Date,Long> {
    Date findByInvoice(Invoice invoice);
}
