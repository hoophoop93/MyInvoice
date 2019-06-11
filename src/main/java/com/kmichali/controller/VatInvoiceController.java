package com.kmichali.controller;


import com.itextpdf.text.DocumentException;
import com.kmichali.config.StageManager;
import com.kmichali.model.*;
import com.kmichali.model.Date;
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
import java.util.*;

@Controller
public class VatInvoiceController implements Initializable {

    @FXML
    private RadioButton invoiceTypeSellRB;
    @FXML
    private RadioButton invoiceTypeBuyRB;
    @FXML
    private ComboBox<String> taxComboBox;
    @FXML
    private ComboBox<String> unitMeasureComboBox;
    @FXML
    private ComboBox<String> productNameComboBox;
    @FXML
    private ComboBox<String> selectCustomerCB;
    @FXML
    private Button addNewRow;
    @FXML
    private TableView<InvoiceField> productTable;
    @FXML
    private TableColumn<InvoiceField,String>  lpColumn;
    @FXML
    private TableColumn<InvoiceField, ComboBox> productNameColumn;
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
    @FXML
    private ToggleGroup invoiceType;

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
    int invoiceTypeIdentyfier;

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
    @Autowired
    SettingsServiceImpl settingsService;

    /**
     *
     * This method will allow the user to double click on a cell and update
     * the name of product
     */


    @FXML
    void menuInvoiceVatRRActopm(ActionEvent event)throws UnsupportedEncodingException {
        stageManager.switchScene(FxmlView.INVOICERRSTAGE);
    }

    @FXML
    void menuSettingsAction(ActionEvent event)throws UnsupportedEncodingException {
        stageManager.switchScene(FxmlView.SETTINGSSTAGE);
    }

    @FXML
    void menuStoreAction(ActionEvent event) throws UnsupportedEncodingException {
        stageManager.switchScene(FxmlView.STORESTAGE);
    }
    public double calculateValuesByUnitMeasure(InvoiceField invoiceField, double amount)
    {
        if(invoiceField.getUnitMeasure().getSelectionModel().getSelectedItem().equals("szt.")){
            amount = amount;
        }else if(invoiceField.getUnitMeasure().getSelectionModel().getSelectedItem().equals("tona")){
            amount = amount / 1000;
        }
        else if(invoiceField.getUnitMeasure().getSelectionModel().getSelectedItem().equals("kg.")){
            amount =  amount/100;
        }
        return amount;
    }

    @FXML
    public void changeAmountCellEvent(TableColumn.CellEditEvent editedCell){
        invoiceField = productTable.getSelectionModel().getSelectedItem();
        invoiceField.setAmount(Double.parseDouble(editedCell.getNewValue().toString()));
        selectedValueComboBox = taxComboBox.getSelectionModel().getSelectedItem();


        priceNetto = invoiceField.getPriceNetto()* calculateValuesByUnitMeasure(invoiceField,invoiceField.getAmount());
        onlyVat = invoiceField.getPriceVat() * calculateValuesByUnitMeasure(invoiceField,invoiceField.getAmount()); ;
        priceBrutto = priceNetto + onlyVat ;
        fillPriceFields(round(priceNetto,2),round(onlyVat,2),round(priceBrutto,2));
        totalPrice();
    }
    @FXML
    public void changePriceNettoCellEvent(TableColumn.CellEditEvent editedCell){
        invoiceField = productTable.getSelectionModel().getSelectedItem();
        invoiceField.setPriceNetto(Double.parseDouble(editedCell.getNewValue().toString()));
        selectedValueComboBox = taxComboBox.getSelectionModel().getSelectedItem();

        priceNetto = invoiceField.getPriceNetto() * calculateValuesByUnitMeasure(invoiceField,invoiceField.getAmount());;
        onlyVat = priceNetto * StringToDoubleConverter(selectedValueComboBox);
        priceBrutto = priceNetto + onlyVat ;
        fillPriceFields(round(priceNetto,2),round(onlyVat,2),round(priceBrutto,2));

        totalPrice();
    }

    @FXML
    public void changeProductValueCellEvent(TableColumn.CellEditEvent editedCell){
        invoiceField = productTable.getSelectionModel().getSelectedItem();
        invoiceField.setProductValue(Double.parseDouble(editedCell.getNewValue().toString()));

        selectedValueComboBox = taxComboBox.getSelectionModel().getSelectedItem();
        priceNetto = invoiceField.getProductValue() * calculateValuesByUnitMeasure(invoiceField,invoiceField.getAmount());
        onlyVat = priceNetto * StringToDoubleConverter(selectedValueComboBox);
        priceBrutto = priceNetto + onlyVat ;
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
        priceNetto = onlyVat * 100/ (tax*100) * calculateValuesByUnitMeasure(invoiceField,invoiceField.getAmount());
        priceBrutto = priceNetto + onlyVat ;
        fillPriceFields(round(priceNetto,2),round(onlyVat,2),round(priceBrutto,2));
        totalPrice();
    }
    @FXML
    public void changePriceBruttoCellEvent(TableColumn.CellEditEvent editedCell){
        invoiceField = productTable.getSelectionModel().getSelectedItem();
        invoiceField.setPriceBrutto(Double.parseDouble(editedCell.getNewValue().toString()));
        selectedValueComboBox = taxComboBox.getSelectionModel().getSelectedItem();

        priceBrutto = round(invoiceField.getPriceBrutto(),2) ;
        priceNetto = priceBrutto *100 /(StringToDoubleConverter(selectedValueComboBox)*100 +100);
        onlyVat = priceBrutto - priceNetto ;

        fillPriceFields(round(priceNetto,2),round(onlyVat,2),round(priceBrutto,2));
        totalPrice();
    }
    @FXML
    private void addNewRowAction(ActionEvent event) {
        invoiceField = new InvoiceField();
        productTable.getItems().add(new InvoiceField(Integer.toString((productTable.getItems().size()+1)),productNameComboBox = new ComboBox<>(fillProductNameComboBox())
                ,unitMeasureComboBox = new ComboBox<>(fillUnitMeasureComboBox()),1,0, 0,
                taxComboBox = new ComboBox<>(fillTaxComboBox()),0,0));
        productTable.requestFocus();
        productTable.getSelectionModel().select(productTable.getItems().size()-1);
        productTable.getSelectionModel().focus(productTable.getItems().size()-1);

        taxComboBoxObjectList.add(taxComboBox);
        taxComboBox.setValue(fillTaxComboBox().get(0));
        productNameComboBox.setValue(fillProductNameComboBox().get(0));
        productNameComboBox.setMinWidth(300);
        productNameComboBox.setEditable(true);
        unitMeasureComboBox.setValue(fillUnitMeasureComboBox().get(0));
        invoiceField.setLp(Integer.toString((productTable.getItems().size())));

        taxComboBox.setOnAction(this::clickComboBox);
    }
    @FXML
    private void clickComboBox(ActionEvent event)  {
        taxComboBox = taxComboBoxObjectList.get(productTable.getSelectionModel().getFocusedIndex());
        invoiceField = productTable.getSelectionModel().getSelectedItem();
        try {
            selectedValueComboBox = taxComboBox.getSelectionModel().getSelectedItem();
            if(selectedValueComboBox.equals("zw.")){
                selectedValueComboBox = "0%";
            }
            priceNetto = invoiceField.getPriceNetto() * calculateValuesByUnitMeasure(invoiceField,invoiceField.getAmount());
            onlyVat = priceNetto * StringToDoubleConverter(selectedValueComboBox);
            priceBrutto = priceNetto + onlyVat ;
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
    @Transactional
    void createInvoiceAction(ActionEvent event) throws DocumentException, IOException {
        boolean sameInvoiceNumber = false;
        long idTransaction =0;
        Optional<ButtonType> result = null;
            if(invoiceService.countInvoiceNumber(invoiceNumberTF.getText())){
                ButtonType okButton = new ButtonType("Tak", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("Nie", ButtonBar.ButtonData.NO);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.getButtonTypes().setAll(okButton, noButton);
                alert.setTitle("Informacja");
                alert.setHeaderText("Faktura o takim numerze już istnieje");
                alert.setContentText("Chcesz ja zamienić?");
                result = alert.showAndWait();

                if (result.get().getText().equals("Nie")) {
                    generateInvoiceNumber();
                    return;
                }else{
                    sameInvoiceNumber= true;
                }
            }



        double getProductAmount=0;
        double calculateAmount=0;
        Seller seller = sellerService.find(1);

        if(invoiceTypeBuyRB.isSelected())invoiceTypeIdentyfier=1;
        else invoiceTypeIdentyfier=0;

        String issueDateToString = String.valueOf(issueDate.getValue());
        String sellDateToString = String.valueOf(sellDate.getValue());
        String paymentDateToString = String.valueOf(paymentDate.getValue());

        if(sameInvoiceNumber == true){
            invoice = invoiceService.findByinvoiceNumber(invoiceNumberTF.getText());
           date = invoice.getDate();
        }else {
            date = new Date();
        }
        date.setIssueDate(issueDateToString);
        date.setSellDate(sellDateToString);
        date.setPaymentDate(paymentDateToString);
        dateService.save(date);

        //custommer
        String nameCustomer = customerNameTF.getText();
        String [] splited = nameCustomer.split("\\s+");
        if(!(customerService.countCustomerByAddress(streetTF.getText()) && customerService.countCustomerBySurname(splited[1]))) {
        customer = new Customer();

        customer.setName(splited[0]);
        customer.setSurname(splited[1]);
        customer.setAddress(streetTF.getText());
        customer.setCity(addressTF.getText());
        customer.setPostalCode(postalCodeTF.getText());
        customerService.save(customer);
        }

        //company
        if(sameInvoiceNumber == true){
            customer = customerService.findCustomer(splited[0],splited[1],streetTF.getText());
        }
        company = new Company();
        company.setName(companyNameTA.getText());
        company.setNip(nipTF.getText());
        company.setCustomer(customer);
        if(!companyService.countByNip(company.getNip()))
        companyService.save(company);

        company = companyService.findBySeller(seller);
        company.setSeller(seller);

        if(!companyService.countByNip(company.getNip()))
        companyService.save(company);

       // invoice
        if(sameInvoiceNumber == true){
            invoice = invoiceService.findByinvoiceNumber(invoiceNumberTF.getText());
        }else{
            invoice = new Invoice();
        }

        invoice.setInvoiceNumber(invoiceNumberTF.getText());
        invoice.setPaidType(paidType.getSelectionModel().getSelectedItem());
        invoice.setInvoiceType("Vat");
        invoice.setDate(date);
        invoiceService.save(invoice);

        List idTransactionList =transactionService.findByInvoice(invoice.getInvoiceNumber(),invoice.getId());

        //transaction
        List<Transaction> transactionList = new ArrayList<>();
        for (InvoiceField row: productTable.getItems()) {
            if(sameInvoiceNumber == true){
                idTransaction = (Long)idTransactionList.get(productTable.getSelectionModel().getSelectedIndex()-1);
                transaction = transactionService.find(idTransaction);
            }else {
                transaction = new Transaction();
            }
            transaction.setTax(row.getTax().getSelectionModel().getSelectedItem());
            transaction.setPriceNetto(row.getPriceNetto());
            transaction.setPriceBrutto(row.getPriceBrutto());
            transaction.setAmount(row.getAmount());
            transaction.setInvoice(invoice);
            transaction.setCustomer(customer);
            transaction.setPriceVat(row.getPriceVat());
            transaction.setProductValue(row.getProductValue());
            transaction.setUnitMeasure(row.getUnitMeasure().getSelectionModel().getSelectedItem());
            transaction.setSeller(seller);

            store = storeService.findByName(row.getNameProduct().getSelectionModel().getSelectedItem());
            if(store == null){
                if (invoiceTypeIdentyfier == 0)  transaction.setType("sprzedaż");
                else {
                    transaction.setType("kupno");
                    //store = new Store();
                    //store.setName(row.getNameProduct().getSelectionModel().getSelectedItem());
                    //store.setAmount(row.getAmount());
                }
            }else{
                getProductAmount = store.getAmount();
                if (invoiceTypeIdentyfier == 1) {
                    calculateAmount = getProductAmount + row.getAmount();
                    transaction.setType("kupno");
                }
                else {
                    if(getProductAmount - row.getAmount() > 0) {
                        calculateAmount = getProductAmount - row.getAmount();
                        transaction.setType("sprzedaż");

                    }else{
                        message("Nie masz tyle ilości "+ row.getNameProduct().getSelectionModel().getSelectedItem()+". W magazynie jest: "
                                +store.getAmount()+" "+row.getUnitMeasure().getSelectionModel().getSelectedItem(),Alert.AlertType.NONE,"");
                        return;
                    }
                }
                store.setAmount(calculateAmount);
                storeService.update(store);
                transaction.setStoreAmount(calculateAmount);

                transaction.setStore(store);
            }

            transactionService.save(transaction);

            //product
            if(productService.checkIfExist(row.getNameProduct().getSelectionModel().getSelectedItem())){
                product = productService.findByName(row.getNameProduct().getSelectionModel().getSelectedItem());
            }else {
                product = new Product();
                product.setName(row.getNameProduct().getSelectionModel().getSelectedItem());
                productService.save(product);
            }

            //join product and transaction
            if(sameInvoiceNumber == true) {
                productTransaction = productTransactionService.findByTransaction(transaction);
            }else {
                productTransaction = new ProductTransaction();
            }
            productTransaction.setProduct(product);
            productTransaction.setTransaction(transaction);
            productTransactionService.save(productTransaction);
        }
//        selectCustomerCB.setItems(fillCustomerComboBox());
//        invoice= invoiceService.find(1);
//        date = dateService.find(1);
//        customer = customerService.find(1);
        Company companyCustomer=companyService.findByCustomer(customer);
        Company companySeller=companyService.findBySeller(seller);
        Settings settings = settingsService.find(1);
        String path = settings.getPath();
        VatInvoicePDF pdfCreator = new VatInvoicePDF(invoice ,date,companySeller,companyCustomer,productTable, paidType,invoiceTypeIdentyfier,invoiceNumberTF.getText(),path);
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
        productNameComboBox.setValue(fillProductNameComboBox().get(0));
        productNameComboBox.setEditable(true);
        productNameComboBox.setMinWidth(300);
        taxComboBoxObjectList.add(taxComboBox);
        productTable.setEditable(true);
        lpColumn.setEditable(false);

        lpColumn.setCellFactory(AcceptOnExitTableCell.forTableColumn());
        amountColumn.setCellFactory(AcceptOnExitTableCell.forTableColumn(new DoubleStringConverter()));
        productValueColumn.setCellFactory(AcceptOnExitTableCell.forTableColumn((new DoubleStringConverter())));
        priceNettoColumn.setCellFactory(AcceptOnExitTableCell.forTableColumn((new DoubleStringConverter())));
        priceVatColumn.setCellFactory(AcceptOnExitTableCell.forTableColumn((new DoubleStringConverter())));
        priceBruttoColumn.setCellFactory(AcceptOnExitTableCell.forTableColumn((new DoubleStringConverter())));


        lpColumn.setCellValueFactory(  new PropertyValueFactory<>("lp"));
        productNameColumn.setCellValueFactory(   new PropertyValueFactory<InvoiceField, ComboBox>("nameProduct"));
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
        selectComboBox.add(new InvoiceField("1",productNameComboBox = new ComboBox<>(fillProductNameComboBox()),
                unitMeasureComboBox = new ComboBox<>(fillUnitMeasureComboBox()),1,0,0 ,
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
    private  ObservableList<String> fillProductNameComboBox(){
        List<Store> allProductList = (List<Store>) storeService.findAll();
        ObservableList<String> allProductListObservable = FXCollections.observableArrayList();

        for (Store s: allProductList ) {
            allProductListObservable.add(s.getName());
        }
        return allProductListObservable;
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
        String invoiceNumberDB,currentMonthInvoiceNumber;
        String invoiceNumber = String.valueOf(issueDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        long maxId=invoiceService.getLastInvoiceNumeber("Vat");
        if(maxId ==0) invoiceNumberDB ="1"+ invoiceNumber.substring(2);
        else {
            Invoice invoice = invoiceService.find(maxId);
            invoiceNumberDB = invoice.getInvoiceNumber();

            String currentMonth = invoiceNumber.substring(3,5);
            if(invoiceNumberDB.length() == 10) currentMonthInvoiceNumber = invoiceNumberDB.substring(3,5);
            else currentMonthInvoiceNumber = invoiceNumberDB.substring(2,4);

            if (invoiceService.countInvoiceNumber(invoiceNumberDB) && currentMonth.equals(currentMonthInvoiceNumber) ) {
                if (invoiceNumberDB.substring(2, 3).equals("/")) invoiceNumberDB = invoiceNumberDB.substring(0, 2);
                else invoiceNumberDB = invoiceNumberDB.substring(0, 1);
                invoiceCounter = Integer.parseInt(invoiceNumberDB);
                invoiceCounter++;
                String counterToString = Integer.toString(invoiceCounter);
                invoiceNumberDB = counterToString + invoiceNumber.substring(2);
            } else {
                invoiceNumberDB = "1" + invoiceNumber.substring(2);
            }
        }


        invoiceNumberTF.setText(invoiceNumberDB);
    }
    @FXML
    public void onEnter(ActionEvent ae){
        ObservableList<String> allProductListObservableTmp = FXCollections.observableArrayList();
        int productIndex=0;
        int taxIndex =0;
        int unitMeasureIndex =0;

        Invoice invoice = invoiceService.findByinvoiceNumber(invoiceNumberTF.getText());
        LocalDate localDate = LocalDate.parse(invoice.getDate().getIssueDate()); //parse String to localdate
        issueDate.setValue(localDate);
        localDate = LocalDate.parse(invoice.getDate().getSellDate()); //parse String to localdate
        sellDate.setValue(localDate);
        localDate = LocalDate.parse(invoice.getDate().getPaymentDate()); //parse String to localdate
        paymentDate.setValue(localDate);
        paidType.setValue(invoice.getPaidType());

        List idTransactionList =transactionService.findByInvoice(invoice.getInvoiceNumber(),invoice.getId());
        long idTransaction = (Long)idTransactionList.get(0);
        Transaction transaction = transactionService.find(idTransaction);
        Company customerCompany = companyService.findByCustomer(transaction.getCustomer());
        companyNameTA.setText(customerCompany.getName());
        customerNameTF.setText(transaction.getCustomer().getName()+ " "+transaction.getCustomer().getSurname());
        streetTF.setText(transaction.getCustomer().getAddress());
        postalCodeTF.setText(transaction.getCustomer().getPostalCode());
        addressTF.setText(transaction.getCustomer().getCity());
        nipTF.setText(customerCompany.getNip());
        if(transaction.getType().equals("kupno"))invoiceTypeBuyRB.setSelected(true);
        else invoiceTypeSellRB.setSelected(true);
        productTable.getItems().clear();
        //create row in table

        Transaction transaction2;
        ProductTransaction productTransaction;
        for(int i=0; i<idTransactionList.size(); i++){
            idTransaction = (Long)idTransactionList.get(i);
            transaction2 = transactionService.find(idTransaction);
            productTransaction = productTransactionService.findByTransaction(transaction2);
            allProductListObservableTmp = fillProductNameComboBox();

            if(!allProductListObservableTmp.contains(productTransaction.getProduct().getName()))
                allProductListObservableTmp.add(productTransaction.getProduct().getName());

            for(int t=0; t<allProductListObservableTmp.size(); t++){
                if(allProductListObservableTmp.get(t).equals(productTransaction.getProduct().getName()))
                productIndex =t;
            }
            for(int t=0; t<fillTaxComboBox().size(); t++){
                if(fillTaxComboBox().get(t).equals(productTransaction.getTransaction().getTax()))
                taxIndex =t;
            }
            for(int t=0; t<fillUnitMeasureComboBox().size(); t++){
                if(fillUnitMeasureComboBox().get(t).equals(productTransaction.getTransaction().getUnitMeasure()))
                unitMeasureIndex =t;
            }
            productTable.getItems().add(new InvoiceField(Integer.toString(
                    i+1),
                    productNameComboBox = new ComboBox<>(allProductListObservableTmp),
                    unitMeasureComboBox = new ComboBox<>(fillUnitMeasureComboBox()),
                    productTransaction.getTransaction().getAmount(),
                    productTransaction.getTransaction().getPriceNetto(),
                    productTransaction.getTransaction().getProductValue(),
                    taxComboBox = new ComboBox<>(fillTaxComboBox()),
                    productTransaction.getTransaction().getPriceVat(),
                    productTransaction.getTransaction().getPriceBrutto()));

                    productNameComboBox.setValue(allProductListObservableTmp.get(productIndex));
                    productNameComboBox.setMinWidth(300);
                    productNameComboBox.setEditable(true);
                    taxComboBox.setValue(fillTaxComboBox().get(taxIndex));
                    unitMeasureComboBox.setValue(fillUnitMeasureComboBox().get(unitMeasureIndex));
        }
    }

}
