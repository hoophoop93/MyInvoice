package com.kmichali.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_product", updatable = false, nullable = false)
    private int id;
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy ="product",cascade=CascadeType.ALL)
    private List<ProductTransaction> productTransactionList;

    public List<ProductTransaction> getProductTransactionList() {
        return productTransactionList;
    }

    public void setProductTransactionList(List<ProductTransaction> productTransactionList) {
        this.productTransactionList = productTransactionList;
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
}
