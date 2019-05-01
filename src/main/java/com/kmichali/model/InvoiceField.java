package com.kmichali.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ComboBox;

public class InvoiceField {

    public SimpleStringProperty lp;
    public SimpleStringProperty nameProduct;
    public SimpleStringProperty productClass;
    public SimpleStringProperty unitMeasure;
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

    public InvoiceField(String lp, String nameProduct, String productClass, String unitMeasure, double amount, double priceNetto,
                        double productValue, ComboBox<String> tax, double priceVat, double priceBrutto) {
        this.lp = new SimpleStringProperty(lp);
        this.nameProduct = new SimpleStringProperty(nameProduct);
        this.productClass = new SimpleStringProperty(productClass);
        this.unitMeasure = new SimpleStringProperty(unitMeasure);
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

    public String getNameProduct() {
        return nameProduct.get();
    }

    public SimpleStringProperty nameProductProperty() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = new SimpleStringProperty(nameProduct);
    }

    public String getProductClass() {
        return productClass.get();
    }

    public SimpleStringProperty productClassProperty() {
        return productClass;
    }

    public void setProductClass(String productClass) {
        this.productClass = new SimpleStringProperty(productClass);
    }

    public String getUnitMeasure() {
        return unitMeasure.get();
    }

    public SimpleStringProperty unitMeasureProperty() {
        return unitMeasure;
    }

    public void setUnitMeasure(String unitMeasure) {
        this.unitMeasure = new SimpleStringProperty(unitMeasure);
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
