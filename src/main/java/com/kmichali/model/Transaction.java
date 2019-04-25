package com.kmichali.model;

import javax.persistence.*;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idTransaction", updatable = false, nullable = false)
    private int id;
    private double priceNetto;
    private double priceBrutto;
    private double amount;
}
