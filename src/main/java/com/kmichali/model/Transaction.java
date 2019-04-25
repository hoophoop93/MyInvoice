package com.kmichali;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "transaction")
public class Transaction {

    private int idTransaction;
    private double priceNetto;
    private double priceBrutto;
    private double amount;
}
