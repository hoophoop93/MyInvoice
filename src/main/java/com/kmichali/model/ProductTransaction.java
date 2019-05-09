package com.kmichali.model;

import javax.persistence.*;

@Entity
@Table(name="product_transaction")
public class ProductTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_product_transaction", updatable = false, nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_transaction")
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "id_product")
    private Product product;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
