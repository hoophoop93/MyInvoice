package com.kmichali.model;

import javax.persistence.*;

@Entity
@Table(name ="identitycard")
public class IdentityCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idCard", updatable = false, nullable = false)
    private long id;
    private String seria;
    private String number;
    private String releaseDate;
    private String organization;

    @OneToOne
    @JoinColumn(name = "idCustomer")
    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSeria() {
        return seria;
    }

    public void setSeria(String seria) {
        this.seria = seria;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
}
