package com.kmichali;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="company")
public class Company {

    private int idCompany;
    private String name;
    private String nip;
    private String regon;
    private String phoneNumber;

}
