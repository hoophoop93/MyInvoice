package com.kmichali.service;

import com.kmichali.generic.GenericService;
import com.kmichali.model.Date;
import com.kmichali.model.Invoice;

public interface DateService extends GenericService<Date> {
    Date findByInvoice(Invoice invoice);
}
