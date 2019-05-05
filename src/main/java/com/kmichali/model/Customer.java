package com.kmichali.model;

import com.sun.scenario.effect.Identity;

import javax.persistence.*;

@Entity
@Table(name="customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idCustomer", updatable = false, nullable = false)
    private int id;
    private String name;
    private String surname;
    private String address;
    private String postalCode;
    private String city;
    private String street;

    @OneToOne(cascade=CascadeType.ALL)
    private Company company;
    @OneToOne(cascade=CascadeType.ALL)
    private IdentityCard identityCard;

    @OneToOne(mappedBy = "customer",cascade=CascadeType.ALL)
    @JoinColumn(name = "idTransaction")
    private Transaction transaction;

    public IdentityCard getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(IdentityCard identityCard) {
        this.identityCard = identityCard;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
