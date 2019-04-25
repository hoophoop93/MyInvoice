package com.kmichali.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ComboBox;

public class InvoiceField {

    public SimpleStringProperty lp;
    public SimpleStringProperty nameProduct;
    public SimpleStringProperty productClass;
    public SimpleStringProperty unitMeasure;
    public SimpleStringProperty amount;
    public SimpleStringProperty unitPrice;
    public SimpleStringProperty productValue;
    public ComboBox<String> tax;
    public SimpleStringProperty priceVat;
    public SimpleStringProperty priceBrutto;

    public InvoiceField(){
    }
    public InvoiceField(String lp){
        this.lp = new SimpleStringProperty(lp);
    }

    public InvoiceField(String lp, String nameProduct, String productClass, String unitMeasure, String amount, String unitPrice,
                        String productValue, ComboBox<String> tax, String priceVat, String priceBrutto) {
        this.lp = new SimpleStringProperty(lp);
        this.nameProduct = new SimpleStringProperty(nameProduct);
        this.productClass = new SimpleStringProperty(productClass);
        this.unitMeasure = new SimpleStringProperty(unitMeasure);
        this.amount = new SimpleStringProperty(amount);
        this.unitPrice = new SimpleStringProperty(unitPrice);
        this.productValue = new SimpleStringProperty(productValue);
        this.tax = tax;
        this.priceVat = new SimpleStringProperty(priceVat);
        this.priceBrutto = new SimpleStringProperty(priceBrutto);
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
        this.productClass= new SimpleStringProperty(productClass);
    }

    public String getUnitMeasure() {
        return unitMeasure.get();
    }

    public SimpleStringProperty unitMeasureProperty() {
        return unitMeasure;
    }

    public void setUnitMeasure(String unitMeasure) {
        this.unitMeasure= new SimpleStringProperty(unitMeasure);
    }

    public String getAmount() {
        return amount.get();
    }

    public SimpleStringProperty amountProperty() {
        return amount;
    }

    public void setAmountColumn(String amountColumn) {
        this.amount= new SimpleStringProperty(amountColumn);
    }

    public String getUnitPrice() {
        return unitPrice.get();
    }

    public SimpleStringProperty unitPriceProperty() {
        return unitPrice;
    }

    public void setUnitPriceColumn(String unitPriceColumn) {
        this.unitPrice = new SimpleStringProperty(unitPriceColumn);
    }

    public String getProductValue() {
        return productValue.get();
    }

    public SimpleStringProperty productValueProperty() {
        return productValue;
    }

    public void setProductValueColumn(String productValueColumn) {
        this.productValue= new SimpleStringProperty(productValueColumn);
    }

    public ComboBox<String> getTax() {
        return tax;
    }

    public void setTax(ComboBox<String> tax) {
        tax = tax;
    }

    public String getPriceVat() {
        return priceVat.get();
    }

    public SimpleStringProperty priceVatProperty() {
        return priceVat;
    }

    public void setPriceVatColumn(String priceVatColumn) {
        this.priceVat= new SimpleStringProperty(priceVatColumn);
    }

    public String getPriceBrutto() {
        return priceBrutto.get();
    }

    public SimpleStringProperty priceBruttoProperty() {
        return priceBrutto;
    }

    public void setPriceBruttoColumn(String priceBruttoColumn) {
        this.priceBrutto= new SimpleStringProperty(priceBruttoColumn);
    }
}
