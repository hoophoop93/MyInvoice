package com.kmichali;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "invoice")
public class Invoice {

    private int idInvoice;
    private String invoiceNumber;
    private String invoiceType;
    private String paidType;
    private String tax;

}
