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

}
