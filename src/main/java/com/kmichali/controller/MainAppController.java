package com.kmichali.controller;


import com.kmichali.model.InvoiceField;
import com.kmichali.service.SellerService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
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
    private TableColumn<InvoiceField, String> amountColumn;

    @FXML
    private TableColumn<InvoiceField, String> unitPriceColumn;

    @FXML
    private TableColumn<InvoiceField, String> productValueColumn;

    @FXML
    private TableColumn<InvoiceField, ComboBox> taxColumn;

    @FXML
    private TableColumn<InvoiceField, String> priceVatColumn;

    @FXML
    private TableColumn<InvoiceField, String> priceBruttoColumn;



    @FXML
    private void addNewRowAction(ActionEvent event) {

    }
    InvoiceField invoiceField;
    /**
     *
     * This method will allow the user to double click on a cell and update
     * the name of product
     */
    @FXML
    public void changeNameProductCellEvent(TableColumn.CellEditEvent editedCell){
        invoiceField = productTable.getSelectionModel().getSelectedItem();
        invoiceField.setNameProduct(editedCell.getNewValue().toString());
        productNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        invoiceField.setLp(Integer.toString(productTable.getSelectionModel().getSelectedIndex()+1));
        //lpColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLp()));
        lpColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        //dodanie nowego rowa do tabelki przyda sie
        //productTable.getItems().add(new InvoiceField(Integer.toString(productTable.getSelectionModel().getSelectedIndex()+1)));
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
        invoiceField.setAmountColumn(editedCell.getNewValue().toString());
    }
    @FXML
    public void changeUnitPriceCellEvent(TableColumn.CellEditEvent editedCell){
        invoiceField = productTable.getSelectionModel().getSelectedItem();
        invoiceField.setUnitPriceColumn(editedCell.getNewValue().toString());
    }
    @FXML
    public void changeProductValueCellEvent(TableColumn.CellEditEvent editedCell){
        invoiceField = productTable.getSelectionModel().getSelectedItem();
        invoiceField.setProductValueColumn(editedCell.getNewValue().toString());
    }
//    @FXML
//    public void changeTaxCellEvent(TableColumn.CellEditEvent editedCell){
//        invoiceField = productTable.getSelectionModel().getSelectedItem();
//        invoiceField.setTaxColumn(editedCell.getNewValue().toString());
//    }
    @FXML
    public void changePriceVatCellEvent(TableColumn.CellEditEvent editedCell){
        invoiceField = productTable.getSelectionModel().getSelectedItem();
        invoiceField.setPriceVatColumn(editedCell.getNewValue().toString());
    }
    @FXML
    public void changePriceBruttoCellEvent(TableColumn.CellEditEvent editedCell){
        invoiceField = productTable.getSelectionModel().getSelectedItem();
        invoiceField.setPriceBruttoColumn(editedCell.getNewValue().toString());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ComboBox<String> taxComboBox = new ComboBox<>();
        productTable.setItems(getFirstRow());
        productTable.setEditable(true);
        lpColumn.setEditable(false);

        productNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        classProductColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        unitMeasureColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        amountColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        productValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        unitPriceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        //taxColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        priceVatColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        priceBruttoColumn.setCellFactory(TextFieldTableCell.forTableColumn());


        lpColumn.setCellValueFactory(  new PropertyValueFactory<>("lp"));
        productNameColumn.setCellValueFactory(  new PropertyValueFactory<>("nameProduct"));
        classProductColumn.setCellValueFactory(  new PropertyValueFactory<>("productClass"));
        unitMeasureColumn.setCellValueFactory(  new PropertyValueFactory<>("unitMeasure"));
        amountColumn.setCellValueFactory(  new PropertyValueFactory<>("amount"));
        unitPriceColumn.setCellValueFactory(  new PropertyValueFactory<>("unitPrice"));
        productValueColumn.setCellValueFactory(  new PropertyValueFactory<>("productValue"));
        taxColumn.setCellValueFactory(  new PropertyValueFactory<InvoiceField, ComboBox>("tax"));
        priceVatColumn.setCellValueFactory(  new PropertyValueFactory<>("priceVat"));
        priceBruttoColumn.setCellValueFactory(  new PropertyValueFactory<>("priceBrutto"));

    }

    private ObservableList<InvoiceField> getFirstRow(){
        taxComboBox.getItems().add("23%");
        taxComboBox.getItems().add("22%");
        taxComboBox.getItems().add("8%");
        taxComboBox.getItems().add("7%");
        taxComboBox.getItems().add("5%");
        taxComboBox.getItems().add("3%");
        taxComboBox.getItems().add("0%");
        taxComboBox.getItems().add("zw.");
        taxComboBox.getItems().add("np.");
        taxComboBox.getItems().add("o.o");
        ObservableList<InvoiceField> selectComboBox = FXCollections.observableArrayList();
        selectComboBox.add(new InvoiceField("","","","","1","0,00","0,00",taxComboBox,"0,00","0,00"));

        return selectComboBox;
    }

}
