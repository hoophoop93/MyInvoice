package com.kmichali.model;

import javax.persistence.*;

@Entity
@Table(name="product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idProduct", updatable = false, nullable = false)
    private int id;
    private String name;

}
