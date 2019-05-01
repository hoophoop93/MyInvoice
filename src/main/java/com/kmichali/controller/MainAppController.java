package com.kmichali.controller;


import com.kmichali.model.Invoice;
import com.kmichali.model.InvoiceField;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.DoubleStringConverter;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class MainAppController implements Initializable {

    @FXML
    private ComboBox<String> taxComboBox;
    @FXML
    private Button addNewRow;
    @FXML
    private TableView<InvoiceField> productTable;
    @FXML
    private TableColumn<InvoiceField,String>  lpColumn;
    @FXML
    private TableColumn<InvoiceField, String> productNameColumn;

    @FXML
    private TableColumn<InvoiceField, String> classProductColumn;

    @FXML
    private TableColumn<InvoiceField, String> unitMeasureColumn;

    @FXML
    private TableColumn<InvoiceField, Double> amountColumn;

    @FXML
    private TableColumn<InvoiceField, Double> priceNettoColumn;

    @FXML
    private TableColumn<InvoiceField, Double> productValueColumn;

    @FXML
    private TableColumn<InvoiceField, ComboBox> taxColumn;

    @FXML
    private TableColumn<InvoiceField, Double> priceVatColumn;

    @FXML
    private TableColumn<InvoiceField, Double> priceBruttoColumn;
    @FXML
    private TextField forPrice;

    @FXML
    private TextField sumNetto;

    @FXML
    private TextField sumVat;
    @FXML
    private Button removeRow;

    private InvoiceField invoiceField;
    private double priceNetto;
    private double onlyVat;
    private double priceBrutto;
    private String selectedValueComboBox;
    private List<ComboBox> comboBoxObjectList = new ArrayList<>();


    /**
     *
     * This method will allow the user to double click on a cell and update
     * the name of product
     */
    @FXML
    public void changeNameProductCellEvent(TableColumn.CellEditEvent editedCell){
        invoiceField = productTable.getSelectionModel().getSelectedItem();
        invoiceField.setNameProduct(editedCell.getNewValue().toString());

    }
    @FXML
    public void changeProductClassCellEvent(TableColumn.CellEditEvent editedCell){
        invoiceField = productTable.getSelectionModel().getSelectedItem();
        invoiceField.setProductClass(editedCell.getNewValue().toString());
    }
    @FXML
    public void changeUnitMeasureCellEvent(TableColumn.CellEditEvent editedCell){
        invoiceField = productTable.getSelectionModel().getSelectedItem();
        invoiceField.setUnitMeasure(editedCell.getNewValue().toString());
    }
    @FXML
    public void changeAmountCellEvent(TableColumn.CellEditEvent editedCell){
        invoiceField = productTable.getSelectionModel().getSelectedItem();
        invoiceField.setAmount(Double.parseDouble(editedCell.getNewValue().toString()));
        selectedValueComboBox = taxComboBox.getSelectionModel().getSelectedItem();

        priceNetto = invoiceField.getPriceNetto()* invoiceField.getAmount();
        onlyVat = invoiceField.getPriceVat() * invoiceField.getAmount();
        priceBrutto = invoiceField.getPriceBrutto() * invoiceField.getAmount();
        fillPriceFields(priceNetto,onlyVat,priceBrutto);
        totalPrice();
    }
    @FXML
    public void changePriceNettoCellEvent(TableColumn.CellEditEvent editedCell){
        invoiceField = productTable.getSelectionModel().getSelectedItem();
        invoiceField.setPriceNetto(Double.parseDouble(editedCell.getNewValue().toString()));
        selectedValueComboBox = taxComboBox.getSelectionModel().getSelectedItem();

        priceNetto = invoiceField.getPriceNetto();
        onlyVat = priceNetto * StringToDoubleConverter(selectedValueComboBox);
        priceBrutto = priceNetto + onlyVat;
        fillPriceFields(priceNetto,onlyVat,priceBrutto);

        totalPrice();
    }

    @FXML
    public void changeProductValueCellEvent(TableColumn.CellEditEvent editedCell){
        invoiceField = productTable.getSelectionModel().getSelectedItem();
        invoiceField.setProductValue(Double.parseDouble(editedCell.getNewValue().toString()));

        selectedValueComboBox = taxComboBox.getSelectionModel().getSelectedItem();
        priceNetto = invoiceField.getProductValue();
        onlyVat = priceNetto * StringToDoubleConverter(selectedValueComboBox);
        priceBrutto = priceNetto + onlyVat;
        fillPriceFields(priceNetto,onlyVat,priceBrutto);
        totalPrice();
    }
    @FXML
    public void changePriceVatCellEvent(TableColumn.CellEditEvent editedCell){
        invoiceField = productTable.getSelectionModel().getSelectedItem();
        invoiceField.setPriceVat(Double.parseDouble(editedCell.getNewValue().toString()));
        selectedValueComboBox = taxComboBox.getSelectionModel().getSelectedItem();

        double tax = StringToDoubleConverter(selectedValueComboBox);
        onlyVat = invoiceField.getPriceVat();
        priceNetto = onlyVat * 100/ (tax*100);
        priceBrutto = priceNetto + onlyVat;
        fillPriceFields(priceNetto,onlyVat,priceBrutto);
        totalPrice();
    }
    @FXML
    public void changePriceBruttoCellEvent(TableColumn.CellEditEvent editedCell){
        invoiceField = productTable.getSelectionModel().getSelectedItem();
        invoiceField.setPriceBrutto(Double.parseDouble(editedCell.getNewValue().toString()));
        selectedValueComboBox = taxComboBox.getSelectionModel().getSelectedItem();

        priceBrutto = invoiceField.getPriceBrutto();
        priceNetto = priceBrutto *100 /(StringToDoubleConverter(selectedValueComboBox)*100 +100);
        onlyVat = priceBrutto - priceNetto;

        fillPriceFields(priceNetto,onlyVat,priceBrutto);
        totalPrice();
    }
    @FXML
    private void addNewRowAction(ActionEvent event) {
        invoiceField = new InvoiceField();
        productTable.getItems().add(new InvoiceField("","","","",1,0,
                0,taxComboBox = new ComboBox<>(fillTaxComboBox()),0,0));
        productTable.requestFocus();
        productTable.getSelectionModel().select(productTable.getItems().size()-1);
        productTable.getSelectionModel().focus(productTable.getItems().size()-1);

        comboBoxObjectList.add(taxComboBox);
        taxComboBox.setValue(fillTaxComboBox().get(0));
        invoiceField.setLp(Integer.toString((productTable.getItems().size())));
        lpColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        taxComboBox.setOnAction(this::clickComboBox);
    }
    @FXML
    private void clickComboBox(ActionEvent event)  {
        taxComboBox = comboBoxObjectList.get(productTable.getSelectionModel().getFocusedIndex());

        selectedValueComboBox = taxComboBox.getSelectionModel().getSelectedItem();
        priceNetto = invoiceField.getPriceNetto() * invoiceField.getAmount();
        onlyVat = priceNetto * StringToDoubleConverter(selectedValueComboBox) * invoiceField.getAmount();
        priceBrutto = priceNetto + onlyVat * invoiceField.getAmount();
        fillPriceFields(priceNetto,onlyVat,priceBrutto);
        totalPrice();
    }
    @FXML
    void removeRowAction(ActionEvent event) {
        InvoiceField selectedRow = productTable.getSelectionModel().getSelectedItem();
        if(productTable.getItems().size() >1)
        productTable.getItems().remove(selectedRow);
        totalPrice();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        productTable.setItems(getFirstRow());
        taxComboBox.setValue(fillTaxComboBox().get(0));
        comboBoxObjectList.add(taxComboBox);
        productTable.setEditable(true);
        lpColumn.setEditable(false);

        productNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        classProductColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        unitMeasureColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        amountColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        productValueColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        priceNettoColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        priceVatColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        priceBruttoColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));


        lpColumn.setCellValueFactory(  new PropertyValueFactory<>("lp"));
        productNameColumn.setCellValueFactory(  new PropertyValueFactory<>("nameProduct"));
        classProductColumn.setCellValueFactory(  new PropertyValueFactory<>("productClass"));
        unitMeasureColumn.setCellValueFactory(  new PropertyValueFactory<>("unitMeasure"));
        amountColumn.setCellValueFactory(  new PropertyValueFactory<>("amount"));
        priceNettoColumn.setCellValueFactory(  new PropertyValueFactory<>("priceNetto"));
        productValueColumn.setCellValueFactory(  new PropertyValueFactory<>("productValue"));
        taxColumn.setCellValueFactory(  new PropertyValueFactory<InvoiceField, ComboBox>("tax"));
        priceVatColumn.setCellValueFactory(  new PropertyValueFactory<>("priceVat"));
        priceBruttoColumn.setCellValueFactory(  new PropertyValueFactory<>("priceBrutto"));

        taxComboBox.setOnAction(this::clickComboBox);

    }
    private double StringToDoubleConverter(String value){
        if(value.length()==3)
            value = value.substring(0,value.length()-1);
        else if(value.length()==2)
            value = value.substring(0,value.length()-1);
        double valueToDouble = Double.parseDouble(value);
        valueToDouble = valueToDouble/100;

        return valueToDouble;
    }
    private ObservableList<InvoiceField> getFirstRow(){
        ObservableList<InvoiceField> selectComboBox = FXCollections.observableArrayList();
        selectComboBox.add(new InvoiceField("1","","","",1,0,0 ,
                taxComboBox = new ComboBox<>(fillTaxComboBox()),0,0));

        return selectComboBox;
    }
    private  ObservableList<String> fillTaxComboBox(){
        ObservableList<String> taxComboboxList = FXCollections.observableArrayList();
        taxComboboxList.add("23%");
        taxComboboxList.add("22%");
        taxComboboxList.add("8%");
        taxComboboxList.add("7%");
        taxComboboxList.add("5%");
        taxComboboxList.add("3%");
        taxComboboxList.add("0%");
        taxComboboxList.add("zw.");
        taxComboboxList.add("np.");
        taxComboboxList.add("o.o");
        return taxComboboxList;
    }

    public void fillPriceFields(double priceNetto, double onlyVat,double priceBrutto){
        if(invoiceField.getAmount()<2){
            invoiceField.setPriceNetto(priceNetto);
            priceNettoColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        }

        invoiceField.setProductValue(priceNetto);
        invoiceField.setPriceVat(onlyVat);
        invoiceField.setPriceBrutto(priceBrutto);

        productValueColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        priceVatColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        priceBruttoColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
    }

    private void totalPrice(){
        double totalNetto = 0;
        double totalVat = 0;
        double totalPrice =0;

        for (InvoiceField row: productTable.getItems()) {
            totalNetto = totalNetto + row.getPriceNetto();
            totalVat = totalVat + row.getPriceVat();
            totalPrice = totalPrice + row.getPriceNetto();
        }
        sumNetto.setText(Double.toString(totalNetto));
        sumVat.setText(Double.toString(totalVat));
        forPrice.setText(Double.toString(totalPrice + totalVat));
    }

}
