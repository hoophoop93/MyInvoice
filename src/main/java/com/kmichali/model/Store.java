package com.kmichali.model;

import javax.persistence.*;

@Entity
@Table(name="store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idStore", updatable = false, nullable = false)
    private int id;
    private String name;
    private double amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
