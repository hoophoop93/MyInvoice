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
import javafx.scene.paint.Color;
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
    @FXML
    private Label transactionDateLabel;

    private InvoiceField invoiceField;
    private double priceNetto;
    private double onlyVat;
    private double priceBrutto;
    private String selectedValueComboBox;
    private List<ComboBox> taxComboBoxObjectList = new ArrayList<>();
    private List<ComboBox> nameProductComboBoxObjectList = new ArrayList<>();
    private List<ComboBox> unitMeasureComboBoxObjectList = new ArrayList<>();
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
        Seller seller = sellerService.find(1);
        Settings settings = settingsService.find(1);
        if(seller == null || settings == null){
            message("Przed rozpoczęciem dodaj swoje dane i ścieżke do zapisu faktur w ustawieniach!!", Alert.AlertType.NONE,"Informacja");
        }else{
            stageManager.switchScene(FxmlView.INVOICERRSTAGE);
        }
    }

    @FXML
    void menuSettingsAction(ActionEvent event)throws UnsupportedEncodingException {
        stageManager.switchScene(FxmlView.SETTINGSSTAGE);
    }

    @FXML
    void menuStoreAction(ActionEvent event) throws UnsupportedEncodingException {
        stageManager.switchScene(FxmlView.STORESTAGE);
    }
    @FXML
    void buyRadioButtonAction(ActionEvent event) {
        if(invoiceTypeBuyRB.isSelected()){
            transactionDateLabel.setText("Data zakupu");
        }
    }
    @FXML
    void sellRadioButtonAction(ActionEvent event) {
        if(invoiceTypeSellRB.isSelected()){
            transactionDateLabel.setText("Data sprzedaży");
        }
    }
    @FXML
    void nextNewInvoice(ActionEvent event) {
        customerNameTF.setText("");
        companyNameTA.setText("");
        addressTF.setText("");
        streetTF.setText("");
        nipTF.setText("");
        postalCodeTF.setText("");
        selectCustomerCB.getSelectionModel().clearSelection();
        setLocalDateForDataPicker();
        taxComboBoxObjectList = new ArrayList<>();
        nameProductComboBoxObjectList = new ArrayList<>();
        unitMeasureComboBoxObjectList = new ArrayList<>();
        productTable.getItems().clear();
        productTable.setItems(getFirstRow());
        taxComboBox.setValue(fillTaxComboBox().get(0));
        unitMeasureComboBox.setValue(fillUnitMeasureComboBox().get(0));
        productNameComboBox.setValue(fillProductNameComboBox().get(0));
        productNameComboBox.setEditable(true);
        productNameComboBox.setMinWidth(300);
        addComboBoxObjectToList();
        generateInvoiceNumber();
    }
    @FXML
    public void changeAmountCellEvent(TableColumn.CellEditEvent editedCell){
        invoiceField = productTable.getSelectionModel().getSelectedItem();
        invoiceField.setAmount(Double.parseDouble(editedCell.getNewValue().toString()));
        selectedValueComboBox = taxComboBox.getSelectionModel().getSelectedItem();


        priceNetto = invoiceField.getPriceNetto();
        onlyVat = priceNetto * StringToDoubleConverter(selectedValueComboBox);
        priceBrutto = priceNetto + onlyVat ;
        fillPriceFields(round(priceNetto,2),round(onlyVat,2),round(priceBrutto,2),round(invoiceField.getAmount(),2));
        totalPrice();
    }
    @FXML
    public void changePriceNettoCellEvent(TableColumn.CellEditEvent editedCell){
        invoiceField = productTable.getSelectionModel().getSelectedItem();
        invoiceField.setPriceNetto(Double.parseDouble(editedCell.getNewValue().toString().replace(",",".")));

        selectedValueComboBox = taxComboBox.getSelectionModel().getSelectedItem();
        priceNetto = invoiceField.getPriceNetto();
        onlyVat = priceNetto * StringToDoubleConverter(selectedValueComboBox);
        priceBrutto = priceNetto + onlyVat ;
        fillPriceFields(round(priceNetto,2),round(onlyVat,2),round(priceBrutto,2),round(invoiceField.getAmount(),2));

        totalPrice();
    }

    @FXML
    public void changeProductValueCellEvent(TableColumn.CellEditEvent editedCell){
        invoiceField = productTable.getSelectionModel().getSelectedItem();
        invoiceField.setProductValue(Double.parseDouble(editedCell.getNewValue().toString()));

        selectedValueComboBox = taxComboBox.getSelectionModel().getSelectedItem();
        priceNetto = invoiceField.getProductValue() * invoiceField.getAmount();
        onlyVat = priceNetto * StringToDoubleConverter(selectedValueComboBox);
        priceBrutto = priceNetto + onlyVat ;
        fillPriceFields(round(priceNetto,2),round(onlyVat,2),round(priceBrutto,2),round(invoiceField.getAmount(),2));
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
        priceBrutto = priceNetto + onlyVat ;
        fillPriceFields(round(priceNetto,2),round(onlyVat,2),round(priceBrutto,2),round(invoiceField.getAmount(),2));
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

        fillPriceFields(round(priceNetto,2),round(onlyVat,2),round(priceBrutto,2),round(invoiceField.getAmount(),2));
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
        nameProductComboBoxObjectList.add(productNameComboBox);
        unitMeasureComboBoxObjectList.add(unitMeasureComboBox);
        taxComboBox.setValue(fillTaxComboBox().get(0));
        productNameComboBox.setValue(fillProductNameComboBox().get(0));
        productNameComboBox.setMinWidth(300);
        productNameComboBox.setEditable(true);
        unitMeasureComboBox.setValue(fillUnitMeasureComboBox().get(0));
        invoiceField.setLp(Integer.toString((productTable.getItems().size())));

        taxComboBox.setOnMouseClicked(this::clickTaxComboBoxAction);
        taxComboBox.setOnAction(this::taxComboBoxAction);
        productNameComboBox.setOnMouseClicked(this::clickNameProductComboBox);
        unitMeasureComboBox.setOnMouseClicked(this::clickUnitMeasureComboBox);
    }
    @FXML
    private void taxComboBoxAction(ActionEvent event)  {

        taxComboBox = taxComboBoxObjectList.get(productTable.getSelectionModel().getFocusedIndex());
        invoiceField = productTable.getSelectionModel().getSelectedItem();
        try {
            selectedValueComboBox = taxComboBox.getSelectionModel().getSelectedItem();
            if(selectedValueComboBox.equals("zw.")){
                selectedValueComboBox = "0%";
            }
            priceNetto = invoiceField.getPriceNetto();
            onlyVat = priceNetto * StringToDoubleConverter(selectedValueComboBox);
            priceBrutto = priceNetto + onlyVat ;
            fillPriceFields(round(priceNetto,2),round(onlyVat,2),round(priceBrutto,2),round(invoiceField.getAmount(),2));
            totalPrice();
            productTable.requestFocus();
        }catch(NullPointerException ex){

        }
    }
    private void clickTaxComboBoxAction(MouseEvent event)  {
        for (int i=0; i<taxComboBoxObjectList.size(); i++){
            if(event.getSource().equals(taxComboBoxObjectList.get(i))) {
                productTable.getSelectionModel().select(i);

            }
        }
    }

    @FXML
    private void clickNameProductComboBox(MouseEvent event) {
        for (int i = 0; i < nameProductComboBoxObjectList.size(); i++) {
            if (event.getSource().equals(nameProductComboBoxObjectList.get(i))) {
                productTable.getSelectionModel().select(i);
                //productTable.getFocusModel().focus(0);
            }
        }
    }
    @FXML
    private void clickUnitMeasureComboBox(MouseEvent event) {
        for (int i = 0; i < unitMeasureComboBoxObjectList.size(); i++) {
            if (event.getSource().equals(unitMeasureComboBoxObjectList.get(i))) {
                productTable.getSelectionModel().select(i);
                //productTable.getFocusModel().focus(i);
            }
        }
    }
    @FXML
    void removeRowAction(ActionEvent event) {
        InvoiceField selectedRow = productTable.getSelectionModel().getSelectedItem();
        if(productTable.getItems().size() >1) {
            int rowIndex = productTable.getSelectionModel().getFocusedIndex();
            taxComboBoxObjectList.remove(rowIndex);
            nameProductComboBoxObjectList.remove(rowIndex);
            unitMeasureComboBoxObjectList.remove(rowIndex);
            productTable.getItems().remove(selectedRow);
            productTable.refresh();

            int counter=1;
            for (InvoiceField row : productTable.getItems()) {
                row.setLp(String.valueOf(counter));
                lpColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                lpColumn.setCellValueFactory(  new PropertyValueFactory<>("lp"));

                counter++;
            }
            totalPrice();
        }
    }

    @FXML
    private void unitMeasureCBACtion(ActionEvent event)  {
        //String selectedUnitMeasureCB;
        invoiceField = productTable.getSelectionModel().getSelectedItem();
        selectedValueComboBox = taxComboBox.getSelectionModel().getSelectedItem();

        priceNetto = invoiceField.getPriceNetto(); ;
        onlyVat = priceNetto * StringToDoubleConverter(selectedValueComboBox);
        priceBrutto = priceNetto + onlyVat ;
        fillPriceFields(round(priceNetto,2),round(onlyVat,2),round(priceBrutto,2),round(invoiceField.getAmount(),2));

        totalPrice();
    }

    @FXML
    void addNewCustomerAction(ActionEvent event) {

        if(!validateCustomerField()) return;
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
            if (!companyService.countByNip(nipTF.getText())) {
                company = new Company();
                company.setName(companyNameTA.getText());
                company.setNip(nipTF.getText());
                company.setCustomer(customer);
                companyService.save(company);
            }
            message("Klient został dodany do listy klientów. ",Alert.AlertType.NONE,"Sukces");
            customerList.add(customer.getName()+" "+customer.getSurname()+"    "+customer.getAddress());
            selectCustomerCB.setItems(customerList);
            new ComboBoxAutoComplete<String>(selectCustomerCB);
        }else{
            message("Taki klient jest juz w bazie!",Alert.AlertType.NONE,"Wiadomość");
        }
    }
    @FXML
    private void selectCustomerAction(Event event) {
        selectCustomerCB.setItems(customerList);
            if(selectCustomerCB.getSelectionModel().getSelectedItem() != null) {
                String[] splitedItem = selectCustomerCB.getSelectionModel().getSelectedItem().split("\\s");
                String[] allCustomerArray = Arrays.stream(splitedItem).filter(value -> value != null && value.length() > 0).toArray(size -> new String[size]);
                if (allCustomerArray.length == 3) {
                    customer = customerService.findCustomer(allCustomerArray[0], allCustomerArray[1], allCustomerArray[2]);
                } else {
                    customer = customerService.findCustomer(allCustomerArray[0], allCustomerArray[1], allCustomerArray[2] + " " + allCustomerArray[3]);
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
        double getProductAmount = 0;
        double calculateAmount = 0;
        Optional<ButtonType> result = null;
        String[] splited = null;
        boolean countInvoiceNum = invoiceService.countInvoiceNumber(invoiceNumberTF.getText(),"Vat");
            if(countInvoiceNum){
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

        if(!countInvoiceNum){
            if(!validateAllField())
            return;
        }

        Seller seller = sellerService.find(1);

        if (invoiceTypeBuyRB.isSelected()) invoiceTypeIdentyfier = 1;
        else invoiceTypeIdentyfier = 0;

        String issueDateToString = String.valueOf(issueDate.getValue());
        String sellDateToString = String.valueOf(sellDate.getValue());
        String paymentDateToString = String.valueOf(paymentDate.getValue());

        if (sameInvoiceNumber == true) {
            invoice = invoiceService.findByinvoiceNumber(invoiceNumberTF.getText(),"Vat");
            date = invoice.getDate();
        } else {
            date = new Date();
        }
        date.setIssueDate(issueDateToString);
        date.setSellDate(sellDateToString);
        date.setPaymentDate(paymentDateToString);
        dateService.save(date);

        //custommer
            String nameCustomer = customerNameTF.getText();
            splited = nameCustomer.split("\\s+");
            if (!(customerService.countCustomerByAddress(streetTF.getText()) && customerService.countCustomerBySurname(splited[1]))) {
                customer = new Customer();

                customer.setName(splited[0]);
                customer.setSurname(splited[1]);
                customer.setAddress(streetTF.getText());
                customer.setCity(addressTF.getText());
                customer.setPostalCode(postalCodeTF.getText());
                customerService.save(customer);
                customerList.add(customer.getName()+" "+customer.getSurname()+"    "+customer.getAddress());
                selectCustomerCB.setItems(customerList);
                new ComboBoxAutoComplete<String>(selectCustomerCB);
            }else {
                if (customer == null)
                    customer = customerService.findCustomer(splited[0], splited[1], streetTF.getText());
            }
        if (!companyService.countByNip(nipTF.getText())) {
            company = new Company();
            company.setName(companyNameTA.getText());
            company.setNip(nipTF.getText());
            company.setCustomer(customer);
            companyService.save(company);
        }

        company = companyService.findBySeller(seller);
        company.setSeller(seller);

        if (!companyService.countByNip(company.getNip()))
            companyService.save(company);

        // invoice
        if (sameInvoiceNumber == true) {
            invoice = invoiceService.findByinvoiceNumber(invoiceNumberTF.getText(),"Vat");
        } else {
            invoice = new Invoice();
        }

        invoice.setInvoiceNumber(invoiceNumberTF.getText());
        invoice.setPaidType(paidType.getSelectionModel().getSelectedItem());
        invoice.setInvoiceType("Vat");
        invoice.setDate(date);
        invoiceService.save(invoice);

        List idTransactionList = transactionService.findByInvoice(invoice.getInvoiceNumber(), invoice.getId(),"Vat");

        //transaction
        int counter =0;
        List<Transaction> transactionList = new ArrayList<>();
        for (InvoiceField row : productTable.getItems()) {
            if (sameInvoiceNumber == true) {
                if(counter < idTransactionList.size()) {
                    idTransaction = (Long) idTransactionList.get(counter);
                    transaction = transactionService.find(idTransaction);
                }else{
                    transaction = new Transaction();
                }
            } else {
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
            //if(row.getUnitMeasure().getSelectionModel().getSelectedItem().equals("tona")){
              //  transaction.setConversionKilograms(row.getAmount()*1000);
            ///}else{
               // transaction.setConversionKilograms(-1);
            //}

            store = storeService.findByName(row.getNameProduct().getSelectionModel().getSelectedItem());
            if (store == null) {
                if (invoiceTypeIdentyfier == 0) transaction.setType("sprzedaż");
                else {
                    transaction.setType("kupno");
                    //store = new Store();
                    //store.setName(row.getNameProduct().getSelectionModel().getSelectedItem());
                    //store.setAmount(row.getAmount());
                }
            } else {
                getProductAmount = store.getAmount();
                if (invoiceTypeIdentyfier == 1) {
                        calculateAmount = getProductAmount + row.getAmount();
                    transaction.setType("kupno");
                } else {
                    //if (getProductAmount - row.getAmount() > 0) {

                      calculateAmount = getProductAmount - row.getAmount();

                        transaction.setType("sprzedaż");
                    }
//                else {
//                        message("Nie masz tyle ilości " + row.getNameProduct().getSelectionModel().getSelectedItem() + ". W magazynie jest: "
//                                + store.getAmount() + " " + row.getUnitMeasure().getSelectionModel().getSelectedItem(), Alert.AlertType.NONE, "");
//                        return;
//                    }
                //}
                store.setAmount(calculateAmount);
                storeService.update(store);
                transaction.setStoreAmount(calculateAmount);

                transaction.setStore(store);
            }

            transactionService.save(transaction);

            //product
            if (productService.checkIfExist(row.getNameProduct().getEditor().getText())) {
                product = productService.findByName(row.getNameProduct().getSelectionModel().getSelectedItem());
            } else {
                product = new Product();
                product.setName(row.getNameProduct().getSelectionModel().getSelectedItem());
                productService.save(product);
            }

            //join product and transaction
            if (sameInvoiceNumber == true) {
                if(counter < idTransactionList.size()) {
                    productTransaction = productTransactionService.findByTransaction(transaction);
                    counter++;
                }else{
                    productTransaction = new ProductTransaction();
                }
            } else {
                productTransaction = new ProductTransaction();
            }
            productTransaction.setProduct(product);
            productTransaction.setTransaction(transaction);
            productTransactionService.save(productTransaction);
        }
        if (sameInvoiceNumber == true) {
            if(counter < idTransactionList.size()) {
                for (int i = counter; i < idTransactionList.size(); i++) {
                    idTransaction = (Long) idTransactionList.get(counter);
                    counter++;
                    transaction = transactionService.find(idTransaction);
                    productTransaction = productTransactionService.findByTransaction(transaction);
                    productTransactionService.delete(productTransaction);
                    transactionService.delete(idTransaction);
                }
            }
        }

        Company companyCustomer = companyService.findByCustomer(customer);
        Company companySeller = companyService.findBySeller(seller);
        Settings settings = settingsService.find(1);
        String path = settings.getPath();
        VatInvoicePDF pdfCreator = new VatInvoicePDF(invoice, date, companySeller, companyCustomer, productTable, paidType, invoiceTypeIdentyfier, invoiceNumberTF.getText(), path);


    }
    @FXML
    void clearData(ActionEvent event) {
        customerNameTF.setText("");
        companyNameTA.setText("");
        addressTF.setText("");
        streetTF.setText("");
        nipTF.setText("");
        postalCodeTF.setText("");
        selectCustomerCB.getSelectionModel().clearSelection();
        setLocalDateForDataPicker();
    }
    @FXML
    void clearTableAction(ActionEvent event) {
        taxComboBoxObjectList = new ArrayList<>();
        nameProductComboBoxObjectList = new ArrayList<>();
        unitMeasureComboBoxObjectList = new ArrayList<>();
        productTable.getItems().clear();
        productTable.setItems(getFirstRow());
        taxComboBox.setValue(fillTaxComboBox().get(0));
        unitMeasureComboBox.setValue(fillUnitMeasureComboBox().get(0));
        productNameComboBox.setValue(fillProductNameComboBox().get(0));
        productNameComboBox.setEditable(true);
        productNameComboBox.setMinWidth(300);
        addComboBoxObjectToList();
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

//        List<Store> allProductList = (List<Store>) storeService.findAll();
//        if(allProductList.size() >0)
//        productNameComboBox.setValue(fillProductNameComboBox().get(0));

        productNameComboBox.setEditable(true);
        productNameComboBox.setMinWidth(300);

        productTable.setEditable(true);
        lpColumn.setEditable(false);
        addComboBoxObjectToList();
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

        taxComboBox.setOnMouseClicked(this::clickTaxComboBoxAction);
        taxComboBox.setOnAction(this::taxComboBoxAction);
        productNameComboBox.setOnMouseClicked(this::clickNameProductComboBox);
        unitMeasureComboBox.setOnMouseClicked(this::clickUnitMeasureComboBox);
    }
    private void addComboBoxObjectToList(){
        taxComboBoxObjectList.add(taxComboBox);
        nameProductComboBoxObjectList.add(productNameComboBox);
        unitMeasureComboBoxObjectList.add(unitMeasureComboBox);
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
        fillUnitMeasureList.add("tona");
        //fillUnitMeasureList.add("kg.");
        fillUnitMeasureList.add("szt.");
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
        paidTypeList.add("Przelew");
        paidTypeList.add("Gotówka");
        return paidTypeList;
    }

    public void fillPriceFields(double priceNetto, double onlyVat,double priceBrutto, double amount){
        if(invoiceField.getAmount()<2){
            invoiceField.setPriceNetto(priceNetto);
            priceNettoColumn.setCellFactory(AcceptOnExitTableCell.forTableColumn(new DoubleStringConverter()));
        }

        invoiceField.setProductValue(round(priceNetto * amount,2));
        invoiceField.setPriceVat(round(onlyVat * amount,2));
        invoiceField.setPriceBrutto(round(priceBrutto * amount,2));

        productValueColumn.setCellFactory(AcceptOnExitTableCell.forTableColumn(new DoubleStringConverter()));
        priceVatColumn.setCellFactory(AcceptOnExitTableCell.forTableColumn(new DoubleStringConverter()));
        priceBruttoColumn.setCellFactory(AcceptOnExitTableCell.forTableColumn(new DoubleStringConverter()));
    }

    private void totalPrice(){
        double totalNetto = 0;
        double totalVat = 0;
        double totalPrice =0;

        for (InvoiceField row: productTable.getItems()) {
            totalNetto = totalNetto + row.getPriceNetto() * row.getAmount();
            totalVat = totalVat + row.getPriceVat();
            totalPrice = totalPrice + row.getPriceNetto() * row.getAmount();
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
        paymentDate.setValue(LocalDate.now().plusDays(14));
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
    public boolean validateAllField(){
        boolean productNameEmpty = false;
        for (InvoiceField row : productTable.getItems()) {
            if(row.getNameProduct().getEditor().getText().equals("")){
                productNameEmpty = true;
                break;
            }
        }
        if(customerNameTF.getText().equals("") || companyNameTA.getText().equals("") || addressTF.getText().equals("") ||
                streetTF.getText().equals("") || nipTF.getText().equals("")|| postalCodeTF.getText().equals("") || invoiceNumberTF.getText().equals("")
        || issueDate.getEditor().getText().equals("")|| sellDate.getEditor().getText().equals("")|| paymentDate.getEditor().getText().equals("")|| productNameEmpty) {
            message("Nie wszystkie pola są wypełnione", Alert.AlertType.NONE, "Informacja");
            return false;
        }
        return true;
    }
    public boolean validateCustomerField(){
        if(customerNameTF.getText().equals("") || companyNameTA.getText().equals("") || addressTF.getText().equals("") ||
                streetTF.getText().equals("") || nipTF.getText().equals("")|| postalCodeTF.getText().equals("")) {
            message("Nie wszystkie pola klienta są wypełnione", Alert.AlertType.NONE, "Informacja");
            return false;
        }
        return true;
    }
    private ObservableList<String> fillCustomerComboBox(){
        customerList = FXCollections.observableArrayList();
        for (Customer customer: customerService.findAll()) {
            customerList.add(customer.getName()+" "+customer.getSurname()+"    "+customer.getAddress());
        }
        return customerList;
    }
    private void fillCustomerFiled(Customer customer, Company company){
        if(company != null) {
            companyNameTA.setText(company.getName());
            nipTF.setText(company.getNip());
        }
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

            if (invoiceService.countInvoiceNumber(invoiceNumberDB,"Vat") && currentMonth.equals(currentMonthInvoiceNumber) ) {
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
    public void onEnter(ActionEvent actionEvent){
        taxComboBoxObjectList = new ArrayList<>();
        nameProductComboBoxObjectList = new ArrayList<>();
        unitMeasureComboBoxObjectList = new ArrayList<>();
        ObservableList<String> allProductListObservableTmp = FXCollections.observableArrayList();
        int productIndex=0;
        int taxIndex =0;
        int unitMeasureIndex =0;
        if(invoiceService.countInvoiceNumber(invoiceNumberTF.getText(),"Vat")) {
            Invoice invoice = invoiceService.findByinvoiceNumber(invoiceNumberTF.getText(),"Vat");
            LocalDate localDate = LocalDate.parse(invoice.getDate().getIssueDate()); //parse String to localdate
            issueDate.setValue(localDate);
            localDate = LocalDate.parse(invoice.getDate().getSellDate()); //parse String to localdate
            sellDate.setValue(localDate);
            localDate = LocalDate.parse(invoice.getDate().getPaymentDate()); //parse String to localdate
            paymentDate.setValue(localDate);
            paidType.setValue(invoice.getPaidType());

            List idTransactionList = transactionService.findByInvoice(invoice.getInvoiceNumber(), invoice.getId(),"Vat");
            long idTransaction = (Long) idTransactionList.get(0);
            Transaction transaction = transactionService.find(idTransaction);
            Company customerCompany = companyService.findByCustomer(transaction.getCustomer());
            companyNameTA.setText(customerCompany.getName());
            customerNameTF.setText(transaction.getCustomer().getName() + " " + transaction.getCustomer().getSurname());
            streetTF.setText(transaction.getCustomer().getAddress());
            postalCodeTF.setText(transaction.getCustomer().getPostalCode());
            addressTF.setText(transaction.getCustomer().getCity());
            nipTF.setText(customerCompany.getNip());
            if (transaction.getType().equals("kupno")) invoiceTypeBuyRB.setSelected(true);
            else invoiceTypeSellRB.setSelected(true);
            productTable.getItems().clear();
            //create row in table

            Transaction transaction2;
            ProductTransaction productTransaction;
            for (int i = 0; i < idTransactionList.size(); i++) {
                idTransaction = (Long) idTransactionList.get(i);
                transaction2 = transactionService.find(idTransaction);
                productTransaction = productTransactionService.findByTransaction(transaction2);
                allProductListObservableTmp = fillProductNameComboBox();

                if (!allProductListObservableTmp.contains(productTransaction.getProduct().getName()))
                    allProductListObservableTmp.add(productTransaction.getProduct().getName());

                for (int t = 0; t < allProductListObservableTmp.size(); t++) {
                    if (allProductListObservableTmp.get(t).equals(productTransaction.getProduct().getName())) {
                        productIndex = t;
                        break;
                    }
                }
                for (int t = 0; t < fillTaxComboBox().size(); t++) {
                    if (fillTaxComboBox().get(t).equals(productTransaction.getTransaction().getTax())) {
                        taxIndex = t;
                        break;
                    }
                }
                for (int t = 0; t < fillUnitMeasureComboBox().size(); t++) {
                    if (fillUnitMeasureComboBox().get(t).equals(productTransaction.getTransaction().getUnitMeasure())) {
                        unitMeasureIndex = t;
                        break;
                    }
                }
                productTable.getItems().add(new InvoiceField(Integer.toString(
                        i + 1),
                        productNameComboBox = new ComboBox<>(allProductListObservableTmp),
                        unitMeasureComboBox = new ComboBox<>(fillUnitMeasureComboBox()),
                        productTransaction.getTransaction().getAmount(),
                        productTransaction.getTransaction().getPriceNetto(),
                        productTransaction.getTransaction().getProductValue(),
                        taxComboBox = new ComboBox<>(fillTaxComboBox()),
                        productTransaction.getTransaction().getPriceVat(),
                        productTransaction.getTransaction().getPriceBrutto()));

                taxComboBox.setOnMouseClicked(this::clickTaxComboBoxAction);
                taxComboBox.setOnAction(this::taxComboBoxAction);
                productNameComboBox.setOnMouseClicked(this::clickNameProductComboBox);
                unitMeasureComboBox.setOnMouseClicked(this::clickUnitMeasureComboBox);
                unitMeasureComboBox.setOnAction(this::unitMeasureCBACtion);
                addComboBoxObjectToList();
                productNameComboBox.getEditor().setText(allProductListObservableTmp.get(productIndex));
                productNameComboBox.setMinWidth(300);
                productNameComboBox.setEditable(true);
                taxComboBox.setValue(fillTaxComboBox().get(taxIndex));
                unitMeasureComboBox.setValue(fillUnitMeasureComboBox().get(unitMeasureIndex));
            }
            totalPrice();
        }else{
            message("Nie ma faktury o takim numerze!", Alert.AlertType.NONE,"Informacja");
        }
    }

}
