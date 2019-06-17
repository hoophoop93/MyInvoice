package com.kmichali.service;

import com.kmichali.generic.GenericService;
import com.kmichali.model.Invoice;

public interface InvoiceService extends GenericService<Invoice> {
    boolean countInvoiceNumber(String invoiceNumber,String invoiceType);
    long getLastInvoiceNumeber(String invoiceType);
    Invoice findByinvoiceNumber(String invoiceNumber);
}
