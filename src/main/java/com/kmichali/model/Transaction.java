package com.kmichali.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_transaction", updatable = false, nullable = false)
    private long id;
    @Column(name = "price_netto")
    private double priceNetto;
    @Column(name = "price_brutto")
    private double priceBrutto;
    @Column(name = "amount")
    private double amount;
    @Column(name = "tax")
    private String tax;
    @Column(name = "store_amount")
    private double storeAmount;
    @Column(name="type")
    private String type;
    @Column(name = "price_vat")
    private double priceVat;
    @Column(name = "product_value")
    private double productValue;
    @Column(name = "unit_measure")
    private String unitMeasure;


    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @OneToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @OneToMany(mappedBy ="transaction",cascade=CascadeType.ALL)
    private List<ProductTransaction> productTransactionList;

    @ManyToOne
    private Store store;

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public List<ProductTransaction> getProductTransactionList() {
        return productTransactionList;
    }

    public void setProductTransactionList(List<ProductTransaction> productTransactionList) {
        this.productTransactionList = productTransactionList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPriceNetto() {
        return priceNetto;
    }

    public void setPriceNetto(double priceNetto) {
        this.priceNetto = priceNetto;
    }

    public double getPriceBrutto() {
        return priceBrutto;
    }

    public void setPriceBrutto(double priceBrutto) {
        this.priceBrutto = priceBrutto;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public double getStoreAmount() {
        return storeAmount;
    }

    public void setStoreAmount(double storeAmount) {
        this.storeAmount = storeAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPriceVat() {
        return priceVat;
    }

    public void setPriceVat(double priceVat) {
        this.priceVat = priceVat;
    }

    public double getProductValue() {
        return productValue;
    }

    public void setProductValue(double productValue) {
        this.productValue = productValue;
    }

    public String getUnitMeasure() {
        return unitMeasure;
    }

    public void setUnitMeasure(String unitMeasure) {
        this.unitMeasure = unitMeasure;
    }
}
