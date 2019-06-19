package com.kmichali.model;

public class ProductRaport {

    double transactionAmount;
    String name;
    String date;
    double wholeAmount;
    String type;
    private String unitMeasure;
    double conversionKilograms;



    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getWholeAmount() {
        return wholeAmount;
    }

    public void setWholeAmount(double wholeAmount) {
        this.wholeAmount = wholeAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnitMeasure() {
        return unitMeasure;
    }

    public void setUnitMeasure(String unitMeasure) {
        this.unitMeasure = unitMeasure;
    }

    public double getConversionKilograms() {
        return conversionKilograms;
    }

    public void setConversionKilograms(double conversionKilograms) {
        this.conversionKilograms = conversionKilograms;
    }
}
