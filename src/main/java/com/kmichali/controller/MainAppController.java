package com.kmichali.controller;


import com.kmichali.model.*;
import com.kmichali.serviceImpl.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class MainAppController implements Initializable {

    @FXML
    private ComboBox<String> taxComboBox;
    @FXML
    private ComboBox<String> selectCustomerCB;
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
    private TextField invoiceNumberTF;
    @FXML
    private TextField sumNetto;
    @FXML
    private TextField sumVat;
    @FXML
    private Button removeRow;
    @FXML
    private ComboBox<String> invoiceTypeCB;
    @FXML
    private ComboBox<String> paidType;
    @FXML
    private DatePicker issueDate;
    @FXML
    private DatePicker sellDate;
    @FXML
    private DatePicker paymentDate;
    @FXML
    private TextArea companyNameTA;
    @FXML
    private TextField customerNameTF;
    @FXML
    private TextField streetTF;
    @FXML
    private TextField postalCodeTF;
    @FXML
    private TextField addressTF;
    @FXML
    private TextField nipTF;

    private InvoiceField invoiceField;
    private double priceNetto;
    private double onlyVat;
    private double priceBrutto;
    private String selectedValueComboBox;
    private List<ComboBox> comboBoxObjectList = new ArrayList<>();

    private Customer customer;
    private Company company;
    private Date date;
    private Invoice invoice;
    private Product product;
    private Store store;
    private Transaction transaction;

    @Autowired
    CustomerServiceImpl customerService;
    @Autowired
    CompanyServiceImpl companyService;
    @Autowired
    DateServiceImpl dateService;
    @Autowired
    InvoiceServiceImpl invoiceService;
    @Autowired
    ProductServiceImpl productService;
    @Autowired
    StoreServiceImpl storeService;
    @Autowired
    TransactionServiceImpl transactionService;

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
        fillPriceFields(round(priceNetto,2),round(onlyVat,2),round(priceBrutto,2));
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
        fillPriceFields(round(priceNetto,2),round(onlyVat,2),round(priceBrutto,2));

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
        fillPriceFields(round(priceNetto,2),round(onlyVat,2),round(priceBrutto,2));
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
        fillPriceFields(round(priceNetto,2),round(onlyVat,2),round(priceBrutto,2));
        totalPrice();
    }
    @FXML
    public void changePriceBruttoCellEvent(TableColumn.CellEditEvent editedCell){
        invoiceField = productTable.getSelectionModel().getSelectedItem();
        invoiceField.setPriceBrutto(Double.parseDouble(editedCell.getNewValue().toString()));
        selectedValueComboBox = taxComboBox.getSelectionModel().getSelectedItem();

        priceBrutto = round(invoiceField.getPriceBrutto(),2);
        priceNetto = priceBrutto *100 /(StringToDoubleConverter(selectedValueComboBox)*100 +100);
        onlyVat = priceBrutto - priceNetto;

        fillPriceFields(round(priceNetto,2),round(onlyVat,2),round(priceBrutto,2));
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

        try {
            selectedValueComboBox = taxComboBox.getSelectionModel().getSelectedItem();
            priceNetto = invoiceField.getPriceNetto() * invoiceField.getAmount();
            onlyVat = priceNetto * StringToDoubleConverter(selectedValueComboBox) * invoiceField.getAmount();
            priceBrutto = priceNetto + onlyVat * invoiceField.getAmount();
            fillPriceFields(round(priceNetto,2), round(onlyVat,2), round(priceBrutto,2));
            totalPrice();
        }catch(NullPointerException ex){

        }
    }
    @FXML
    void removeRowAction(ActionEvent event) {
        InvoiceField selectedRow = productTable.getSelectionModel().getSelectedItem();
        if(productTable.getItems().size() >1)
        productTable.getItems().remove(selectedRow);
        totalPrice();
    }
    @FXML
    void addNewCustomerAction(ActionEvent event) {
        //validateCustomerField();
        customer = new Customer();
        String nameCustomer = customerNameTF.getText();
        String [] splited = nameCustomer.split("\\s+");

        customer.setName(splited[0]);
        customer.setSurname(splited[1]);
        customer.setAddress(streetTF.getText());
        customer.setCity(addressTF.getText());
        customer.setPostalCode(postalCodeTF.getText());
        customerService.save(customer);
        Alert alert = new Alert(Alert.AlertType.NONE, "Klient został dodany do listy klientów. ", ButtonType.OK);
        alert.showAndWait();
        selectCustomerCB.setItems(fillCustomerComboBox());
    }
    @FXML
    private void selectCustomerAction(ActionEvent event) {
        int clickedOption = selectCustomerCB.getSelectionModel().getSelectedIndex();
        String getComboBoxItem = fillCustomerComboBox().get(clickedOption);
        String[] splitedItem = getComboBoxItem.split("\\s+");
        customer = customerService.findCustomer(splitedItem[0],splitedItem[1],splitedItem[2]+" "+splitedItem[3]);
        fillCustomerFiled(customer);
    }
    @FXML
    void createInvoiceAction(ActionEvent event) {
        //date
        String issueDateToString = String.valueOf(issueDate.getValue());
        String sellDateToString = String.valueOf(sellDate.getValue());
        String paymentDateToString = String.valueOf(paymentDate.getValue());
        date = new Date();
        date.setIssueDate(issueDateToString);
        date.setSellDate(sellDateToString);
        date.setPaymentDate(paymentDateToString);
        dateService.save(date);
        //custommer
        customer = new Customer();
        String nameCustomer = customerNameTF.getText();
        String [] splited = nameCustomer.split("\\s+");
        customer.setName(splited[0]);
        customer.setSurname(splited[1]);
        customer.setAddress(streetTF.getText());
        customer.setCity(addressTF.getText());
        customer.setPostalCode(postalCodeTF.getText());
        customerService.save(customer);
        //company
        company = new Company();
        company.setName(companyNameTA.getText());
        company.setNip(nipTF.getText());
        company.setCustomer(customer);
        companyService.save(company);
        //customer update
        customer.setCompany(company);
        customerService.save(customer);

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //customer = customerService.findCustomer("Marek","Uram","Wrocanka 44");
        //System.out.println(customer.getAddress());

        selectCustomerCB.setItems(fillCustomerComboBox());

        setLocalDateForDataPicker();
        setValueForCombobox();
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
    private ObservableList<String> fillInvoiceTypeComboBox(){
        ObservableList<String> invoiceTypeList = FXCollections.observableArrayList();
        invoiceTypeList.add("VAT RR");
        invoiceTypeList.add("VAT");

        return invoiceTypeList;
    }
    private ObservableList<String> fillPaidTypeComboBox(){
        ObservableList<String> paidTypeList = FXCollections.observableArrayList();
        paidTypeList.add("Gotówka");
        paidTypeList.add("Przelew");

        return paidTypeList;
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
        double allForPrice = totalPrice + totalVat;
        sumNetto.setText(Double.toString(round(totalNetto,2)));
        sumVat.setText(Double.toString(round(totalVat,2)));
        forPrice.setText(Double.toString(round(allForPrice,2)));
    }
    private void setLocalDateForDataPicker(){
        setDatePattern(issueDate);
        setDatePattern(sellDate);
        setDatePattern(paymentDate);
        issueDate.setValue(LocalDate.now());
        sellDate.setValue(LocalDate.now());
        paymentDate.setValue(LocalDate.now().plusDays(10));
    }
    private void setValueForCombobox(){
        invoiceTypeCB.getItems().setAll(fillInvoiceTypeComboBox());
        paidType.getItems().setAll(fillPaidTypeComboBox());
        invoiceTypeCB.setValue(fillInvoiceTypeComboBox().get(0));
        paidType.setValue(fillPaidTypeComboBox().get(0));
    }

    private void setDatePattern(DatePicker datePicker){
        String pattern = "dd-MM-yyyy";
        datePicker.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
            @Override
            public String toString(LocalDate localDate) {
                if (localDate != null) {
                    return dateFormatter.format(localDate);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
    }
    private double replaceCommaToDot(String stringText){
        stringText = stringText.replaceAll(",",".");
        double doubleValue = StringToDoubleConverter(stringText);
        return doubleValue;
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    public void validateCustomerField(){
        if(customerNameTF.getText().equals("") || companyNameTA.getText().equals("") || addressTF.getText().equals("") ||
                streetTF.getText().equals("") || nipTF.getText().equals("")|| postalCodeTF.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Jedno z wymaganych pól klineta jest puste.", ButtonType.OK);
            alert.setTitle("Ostrzeżenie!");
            alert.showAndWait();
        }
    }
    private ObservableList<String> fillCustomerComboBox(){
        ObservableList<String> customerList = FXCollections.observableArrayList();
        for (Customer customer: customerService.findAll()) {
            customerList.add(customer.getName()+" "+customer.getSurname()+"    "+customer.getAddress());
        }
        return customerList;
    }
    private void fillCustomerFiled(Customer customer){
        customerNameTF.setText(customer.getName()+" "+customer.getSurname());
        streetTF.setText(customer.getAddress());
        postalCodeTF.setText(customer.getPostalCode());
        addressTF.setText(customer.getCity());
    }

}
