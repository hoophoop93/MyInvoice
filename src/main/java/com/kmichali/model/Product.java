package com.kmichali;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="product")
public class Product {

    private int idProduct;
    private String name;

}
