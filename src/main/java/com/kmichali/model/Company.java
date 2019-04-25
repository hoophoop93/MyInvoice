package com.kmichali.model;

import javax.persistence.*;

@Entity
@Table(name="company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idCompany", updatable = false, nullable = false)
    private int id;
    private String name;
    private String nip;
    private String regon;
    private String phoneNumber;

}
