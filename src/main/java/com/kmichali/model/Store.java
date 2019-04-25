package com.kmichali;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="store")
public class Store {

    private int idStore;
    private String name;
    private double amount;

}
