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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPriceNetto() {
        return priceNetto;
    }

    public void setPriceNetto(double priceNetto) {
        this.priceNetto = priceNetto;
    }

    public double getPriceBrutto() {
        return priceBrutto;
    }

    public void setPriceBrutto(double priceBrutto) {
        this.priceBrutto = priceBrutto;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
