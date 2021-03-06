package com.kmichali.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ComboBox;

public class InvoiceField {

    public SimpleStringProperty lp;
    public ComboBox<String> nameProduct;
    public ComboBox<String> unitMeasure;
    public double amount;
    public double priceNetto;
    public double productValue;
    public ComboBox<String> tax;
    public double priceVat;
    public double priceBrutto;

    public InvoiceField(){
    }
    public InvoiceField(String lp){
        this.lp = new SimpleStringProperty(lp);
    }

    public InvoiceField(String lp, ComboBox<String> nameProduct, ComboBox<String> unitMeasure, double amount, double priceNetto,
                        double productValue, ComboBox<String> tax, double priceVat, double priceBrutto) {
        this.lp = new SimpleStringProperty(lp);
        this.nameProduct = nameProduct;
        this.unitMeasure = unitMeasure;
        this.amount = amount;
        this.priceNetto = priceNetto;
        this.productValue = productValue;
        this.tax = tax;
        this.priceVat = priceVat;
        this.priceBrutto = priceBrutto;
    }

    public String getLp() {
        return lp.get();
    }

    public SimpleStringProperty lpProperty() {
        return lp;
    }

    public void setLp(String lp) {
        this.lp = new SimpleStringProperty(lp);
    }

    public ComboBox<String> getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(ComboBox<String> nameProduct) {
        this.nameProduct = nameProduct;
    }

    public ComboBox<String> getUnitMeasure() {
        return unitMeasure;
    }

    public void setUnitMeasure(ComboBox<String> unitMeasure) {
        this.unitMeasure = unitMeasure;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPriceNetto() {
        return priceNetto;
    }

    public void setPriceNetto(double priceNetto) {
        this.priceNetto = priceNetto;
    }

    public double getProductValue() {
        return productValue;
    }

    public void setProductValue(double productValue) {
        this.productValue = productValue;
    }

    public ComboBox<String> getTax() {
        return tax;
    }

    public void setTax(ComboBox<String> tax) {
        this.tax = tax;
    }

    public double getPriceVat() {
        return priceVat;
    }

    public void setPriceVat(double priceVat) {
        this.priceVat = priceVat;
    }

    public double getPriceBrutto() {
        return priceBrutto;
    }

    public void setPriceBrutto(double priceBrutto) {
        this.priceBrutto = priceBrutto;
    }
}
