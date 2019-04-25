package com.kmichali.model;

import javax.persistence.*;

@Entity
@Table(name = "invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idInvoice", updatable = false, nullable = false)
    private int id;
    private String invoiceNumber;
    private String invoiceType;
    private String paidType;
    private String tax;

}
