package com.kmichali.model;

import javax.persistence.*;

@Entity
@Table(name="empty_transaction")
public class EmptyTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_empty_transaction", updatable = false, nullable = false)
    private long id;
    @Column(name = "product_name")
    String productName;
    @Column(name = "invoice_number")
    String invoiceNumber;
    @Column(name = "customer_name")
    String customerName;
    @Column(name = "date")
    String date;
    @Column(name = "amount")
    double amount;
    @Column(name = "price_netto")
    double price;
    @Column(name = "type")
    String type;
    @Column(name = "transaction_amount")
    double transactionAmount;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
