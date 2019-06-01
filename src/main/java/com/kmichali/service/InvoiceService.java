package com.kmichali.service;

import com.kmichali.generic.GenericService;
import com.kmichali.model.Invoice;

public interface InvoiceService extends GenericService<Invoice> {
    boolean countInvoiceNumber(String invoiceNumber);
    long getLastInvoiceNumeber(String invoiceType);
}
