package com.kmichali.controller;


import com.itextpdf.text.DocumentException;
import com.kmichali.config.StageManager;
import com.kmichali.model.*;
import com.kmichali.repository.ProductRepository;
import com.kmichali.serviceImpl.*;
import com.kmichali.utility.AcceptOnExitTableCell;
import com.kmichali.utility.ComboBoxAutoComplete;
import com.kmichali.utility.VatInvoicePDF;
import com.kmichali.view.FxmlView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class VatInvoiceController implements Initializable {

    @FXML
    private Label label1,label2,label3,label4,label5;
    @FXML
    private ComboBox<String> taxComboBox;
    @FXML
    private ComboBox<String> unitMeasureComboBox;
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
    private TableColumn<InvoiceField, ComboBox> unitMeasureColumn;
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
    private List<ComboBox> taxComboBoxObjectList = new ArrayList<>();

    private Customer customer;
    private Company company;
    private Date date;
    private Invoice invoice;
    private Product product;
    private Store store;
    private Transaction transaction;
    private ProductTransaction productTransaction;
    private ObservableList<String> customerList;
    private static int counter=0;

    @Autowired
    ProductRepository productRepository;
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
    @Autowired
    SellerServiceImpl sellerService;
    @Autowired
    ProductTransactionImpl productTransactionService;
    @Lazy
    @Autowired
    private StageManager stageManager;

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
        productTable.getItems().add(new InvoiceField(Integer.toString((productTable.getItems().size()+1)),"","1",unitMeasureComboBox = new ComboBox<>(fillUnitMeasureComboBox())
                ,1,0, 0,taxComboBox = new ComboBox<>(fillTaxComboBox()),0,0));
        productTable.requestFocus();
        productTable.getSelectionModel().select(productTable.getItems().size()-1);
        productTable.getSelectionModel().focus(productTable.getItems().size()-1);

        taxComboBoxObjectList.add(taxComboBox);
        taxComboBox.setValue(fillTaxComboBox().get(0));
        unitMeasureComboBox.setValue(fillUnitMeasureComboBox().get(0));
        invoiceField.setLp(Integer.toString((productTable.getItems().size())));

        taxComboBox.setOnAction(this::clickComboBox);
    }
    @FXML
    private void clickComboBox(ActionEvent event)  {
        taxComboBox = taxComboBoxObjectList.get(productTable.getSelectionModel().getFocusedIndex());

        try {
            selectedValueComboBox = taxComboBox.getSelectionModel().getSelectedItem();
            if(selectedValueComboBox.equals("zw.")){
                selectedValueComboBox = "0%";
            }
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
        if(productTable.getItems().size() >1) {
            productTable.getItems().remove(selectedRow);

            int counter=1;
            for (InvoiceField row : productTable.getItems()) {
                invoiceField.setLp(String.valueOf(counter));
                lpColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                lpColumn.setCellValueFactory(  new PropertyValueFactory<>("lp"));

                counter++;
            }
        }

        productTable.refresh();
        totalPrice();
    }
    @FXML
    void addNewCustomerAction(ActionEvent event) {
        //validateCustomerField();
        customer = new Customer();
        String nameCustomer = customerNameTF.getText();
        String [] splited = nameCustomer.split("\\s+");

        if(!(customerService.countCustomerByAddress(streetTF.getText()) && customerService.countCustomerBySurname(splited[1]))) {
            customer.setName(splited[0]);
            customer.setSurname(splited[1]);
            customer.setAddress(streetTF.getText());
            customer.setCity(addressTF.getText());
            customer.setPostalCode(postalCodeTF.getText());
            customerService.save(customer);
            message("Klient został dodany do listy klientów. ",Alert.AlertType.NONE,"Sukces");
            selectCustomerCB.setItems(fillCustomerComboBox());
        }else{
            message("Taki klient jest juz w bazie!",Alert.AlertType.NONE,"Wiadomość");
        }
    }
    @FXML
    private void selectCustomerAction(Event event) {

            if(selectCustomerCB.getSelectionModel().getSelectedItem() != null) {
                String[] splitedItem = selectCustomerCB.getSelectionModel().getSelectedItem().split("\\s");
                String[] removedEmptyElemnt = Arrays.stream(splitedItem).filter(value -> value != null && value.length() > 0).toArray(size -> new String[size]);
                if (removedEmptyElemnt.length == 3) {
                    customer = customerService.findCustomer(removedEmptyElemnt[0], removedEmptyElemnt[1], removedEmptyElemnt[2]);
                } else {
                    customer = customerService.findCustomer(removedEmptyElemnt[0], removedEmptyElemnt[1], removedEmptyElemnt[2] + " " + removedEmptyElemnt[3]);
                }
                company = companyService.findByCustomer(customer);
                fillCustomerFiled(customer, company);
            }
    }
    @FXML
    void invoiceTableAction(MouseEvent event) {
    AcceptOnExitTableCell acceptOnExitTableCell = new AcceptOnExitTableCell();
    }
    @FXML
    void backButtonAction(ActionEvent event) throws UnsupportedEncodingException {
        stageManager.switchScene(FxmlView.PRIMARYSTAGE);
    }
    @FXML
    @Transactional
    void createInvoiceAction(ActionEvent event) throws DocumentException, IOException {
        Seller seller = sellerService.find(1);
        //date
//        String issueDateToString = String.valueOf(issueDate.getValue());
//        String sellDateToString = String.valueOf(sellDate.getValue());
//        String paymentDateToString = String.valueOf(paymentDate.getValue());
//        date = new Date();
//        date.setIssueDate(issueDateToString);
//        date.setSellDate(sellDateToString);
//        date.setPaymentDate(paymentDateToString);
//        dateService.save(date);
//
//        //custommer
        //String [] splited = nameCustomer.split("\\s+");
       // if(!(customerService.countCustomerByAddress(streetTF.getText()) && customerService.countCustomerBySurname(splited[1]))) {
//        customer = new Customer();
//        String nameCustomer = customerNameTF.getText();
//
//        customer.setName(splited[0]);
//        customer.setSurname(splited[1]);
//        customer.setAddress(streetTF.getText());
//        customer.setCity(addressTF.getText());
//        customer.setPostalCode(postalCodeTF.getText());
//        customerService.save(customer);
       // }
//
//        //company
//        company = new Company();
//        company.setName(companyNameTA.getText());
//        company.setNip(nipTF.getText());
//        company.setCustomer(customer);
//        companyService.save(company);
//
//        company = new Company();
//        company.setSeller(seller);
//        companyService.save(company);
//
//       // invoice
//        invoice = new Invoice();
//        invoice.setInvoiceNumber(invoiceNumberTF.getText());
//        invoice.setPaidType(paidType.getSelectionModel().getSelectedItem());
//        invoice.setInvoiceType(invoiceTypeCB.getSelectionModel().getSelectedItem());
//        invoice.setDate(date);
//        invoiceService.save(invoice);
//
//
//        //transaction
//        List<Transaction> transactionList = new ArrayList<>();
//        for (InvoiceField row: productTable.getItems()) {
//            transaction = new Transaction();
//            transaction.setTax(row.getTax().getSelectionModel().getSelectedItem());
//            transaction.setPriceNetto(row.getPriceNetto());
//            transaction.setPriceBrutto(row.getPriceBrutto());
//            transaction.setAmount(row.getAmount());
//            transaction.setInvoice(invoice);
//            transaction.setCustomer(customer);
//            transaction.setSeller(seller);
//            transactionService.save(transaction);
//
//        //product
//            product = new Product();
//            product.setName(row.getNameProduct());
//            //if(!productService.checkIfExist(row.getNameProduct())) {
//                productService.save(product);
//
//
//            //join product and transaction
//            productTransaction = new ProductTransaction();
//            productTransaction.setProduct(product);
//            productTransaction.setTransaction(transaction);
//            productTransactionService.save(productTransaction);
//        }
//        selectCustomerCB.setItems(fillCustomerComboBox());
        invoice= invoiceService.find(1);
        date = dateService.find(1);
        customer = customerService.find(1);
        Company companyCustomer=companyService.findByCustomer(customer);
        Company companySeller=companyService.findBySeller(seller);
        VatInvoicePDF pdfCreator = new VatInvoicePDF(invoice ,date,companySeller,companyCustomer,productTable, paidType);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        selectCustomerCB.setItems(fillCustomerComboBox());
        new ComboBoxAutoComplete<String>(selectCustomerCB);
        setLocalDateForDataPicker();
        generateInvoiceNumber();
        setValueForCombobox();
        productTable.setItems(getFirstRow());
        taxComboBox.setValue(fillTaxComboBox().get(0));
        unitMeasureComboBox.setValue(fillUnitMeasureComboBox().get(0));
        taxComboBoxObjectList.add(taxComboBox);
        productTable.setEditable(true);
        lpColumn.setEditable(false);

        lpColumn.setCellFactory(AcceptOnExitTableCell.forTableColumn());
        productNameColumn.setCellFactory(AcceptOnExitTableCell.forTableColumn());
        classProductColumn.setCellFactory(AcceptOnExitTableCell.forTableColumn());
        amountColumn.setCellFactory(AcceptOnExitTableCell.forTableColumn(new DoubleStringConverter()));
        productValueColumn.setCellFactory(AcceptOnExitTableCell.forTableColumn((new DoubleStringConverter())));
        priceNettoColumn.setCellFactory(AcceptOnExitTableCell.forTableColumn((new DoubleStringConverter())));
        priceVatColumn.setCellFactory(AcceptOnExitTableCell.forTableColumn((new DoubleStringConverter())));
        priceBruttoColumn.setCellFactory(AcceptOnExitTableCell.forTableColumn((new DoubleStringConverter())));


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
        selectComboBox.add(new InvoiceField("1","","1",unitMeasureComboBox = new ComboBox<>(fillUnitMeasureComboBox()),1,0,0 ,
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
        return taxComboboxList;
    }
    private  ObservableList<String> fillUnitMeasureComboBox(){
        ObservableList<String> fillUnitMeasureList = FXCollections.observableArrayList();
        fillUnitMeasureList.add("szt.");
        fillUnitMeasureList.add("tona");
        fillUnitMeasureList.add("kg.");
        return fillUnitMeasureList;
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
            priceNettoColumn.setCellFactory(AcceptOnExitTableCell.forTableColumn(new DoubleStringConverter()));
        }

        invoiceField.setProductValue(priceNetto);
        invoiceField.setPriceVat(onlyVat);
        invoiceField.setPriceBrutto(priceBrutto);

        productValueColumn.setCellFactory(AcceptOnExitTableCell.forTableColumn(new DoubleStringConverter()));
        priceVatColumn.setCellFactory(AcceptOnExitTableCell.forTableColumn(new DoubleStringConverter()));
        priceBruttoColumn.setCellFactory(AcceptOnExitTableCell.forTableColumn(new DoubleStringConverter()));
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
        paidType.getItems().setAll(fillPaidTypeComboBox());
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
        customerList = FXCollections.observableArrayList();
        for (Customer customer: customerService.findAll()) {
            customerList.add(customer.getName()+" "+customer.getSurname()+"    "+customer.getAddress());
        }
        return customerList;
    }
    private void fillCustomerFiled(Customer customer, Company company){
        companyNameTA.setText(company.getName());
        nipTF.setText(company.getNip());
        customerNameTF.setText(customer.getName()+" "+customer.getSurname());
        streetTF.setText(customer.getAddress());
        postalCodeTF.setText(customer.getPostalCode());
        addressTF.setText(customer.getCity());
    }
    private void message(String message, Alert.AlertType alertType,String typeMessage){
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(typeMessage);
        alert.showAndWait();
    }
    private void generateInvoiceNumber(){
        int invoiceCounter=1;
        String invoiceNumber = String.valueOf(issueDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        long maxId=invoiceService.getLastInvoiceNumeber("Vat");
        Invoice invoice = invoiceService.find(maxId);
        String invoiceNumberDB = invoice.getInvoiceNumber();
        if(invoiceService.countInvoiceNumber(invoiceNumberDB) && !invoiceNumber.substring(0,2).equals("01")){
            if(invoiceNumberDB.substring(2,3).equals("/")) invoiceNumberDB = invoiceNumberDB.substring(0,2);
            else invoiceNumberDB = invoiceNumberDB.substring(0,1);
            invoiceCounter = Integer.parseInt(invoiceNumberDB);
            invoiceCounter++;
            String counterToString =Integer.toString(invoiceCounter);
            invoiceNumberDB = counterToString + invoiceNumber.substring(2);
        }else{
            invoiceNumberDB ="1"+ invoiceNumber.substring(2);
        }


        invoiceNumberTF.setText(invoiceNumberDB);
    }
}
