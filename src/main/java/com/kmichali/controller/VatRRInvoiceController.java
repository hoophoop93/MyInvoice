package com.kmichali.controller;

import com.itextpdf.text.DocumentException;
import com.kmichali.config.StageManager;
import com.kmichali.model.*;
import com.kmichali.model.Date;
import com.kmichali.repository.ProductRepository;
import com.kmichali.serviceImpl.*;
import com.kmichali.utility.*;
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

import javax.swing.text.DateFormatter;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class VatRRInvoiceController implements Initializable {

    private static DecimalFormat df2 = new DecimalFormat("#,##0.00");
    @Lazy
    @Autowired
    private StageManager stageManager;
    @FXML
    private ComboBox<String> veterinaryInspectorateCB;
    @FXML
    private ComboBox<String> productNameComboBox;
    @FXML
    private TextField invoiceNumberTF;
    @FXML
    private TextField peselTF;
    @FXML
    private TextField idCardTF;
    @FXML
    private TextField accountNumberTF;
    @FXML
    private TextField seriaAndNumberIdCard;
    @FXML
    private DatePicker releaseDateTF;
    @FXML
    private ComboBox<String> paidType;
    @FXML
    private ComboBox<String> promotionFoundCB;
    @FXML
    private ComboBox<String> taxComboBox;
    @FXML
    private ComboBox<String> unitMeasureComboBox;
    @FXML
    private DatePicker issueDate;
    @FXML
    private DatePicker sellDate;
    @FXML
    private DatePicker paymentDate;
    @FXML
    private TextArea releaseByTF;
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
    private Button addNewRow;
    @FXML
    private Button removeRow;
    @FXML
    private TextField forPrice;
    @FXML
    private TextField sumNetto;
    @FXML
    private TextField sumVat;
    @FXML
    private ComboBox<String> selectCustomerCB;
    @FXML
    private TextField customerNameTF;
    @FXML
    private TextField streetTF;
    @FXML
    private TextField postalCodeTF;
    @FXML
    private TextField addressTF;
    @FXML
    private Label radioButtonLabel;
    @FXML
    private RadioButton statementRadioButton;
    @FXML
    private RadioButton statementAndRodoRadioButton;
    @FXML
    private TextField oldInvoiceNumber;
    @FXML
    private TextField newInvoiceNumber;

    private Customer customer;
    private Company company;
    private Date date;
    private Invoice invoice;
    private Product product;
    private Store store;
    private Transaction transaction;
    private ProductTransaction productTransaction;
    private IdentityCard identityCard;
    private ObservableList<String> customerList;

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
    @Autowired
    IdentityCardImpl identityCardService;
    @Autowired
    SettingsServiceImpl settingsService;

    private List<ComboBox> taxComboBoxObjectList = new ArrayList<>();
    private List<ComboBox> nameProductComboBoxObjectList = new ArrayList<>();
    private List<ComboBox> unitMeasureComboBoxObjectList = new ArrayList<>();
    private InvoiceField invoiceField;
    private double priceNetto;
    private double onlyVat;
    private double priceBrutto;
    private String selectedValueComboBox;


    @FXML
    void menuInvoiceVatAction(ActionEvent event)throws UnsupportedEncodingException {
        Seller seller = sellerService.find(1);
        Settings settings = settingsService.find(1L);
        if(seller == null || settings == null){
            message("Przed rozpoczęciem dodaj swoje dane i ścieżke do zapisu faktur w ustawieniach!!", Alert.AlertType.NONE,"Informacja");
        }else{
            stageManager.switchScene(FxmlView.INVOICEVATSTAGE);
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
    void nextInvoiceNumber(ActionEvent event) {
        generateInvoiceNumber();
    }
    @FXML
    void nextNewInvoice(ActionEvent event) {
        forPrice.setText("0.00");
        sumNetto.setText("0.00");
        sumVat.setText("0.00");
        customerNameTF.setText("");
        addressTF.setText("");
        streetTF.setText("");
        peselTF.setText("");
        postalCodeTF.setText("");
        seriaAndNumberIdCard.setText("");
        releaseDateTF.setValue(null);
        releaseByTF.setText("");
        accountNumberTF.setText("");
        selectCustomerCB.getSelectionModel().clearSelection();
        setLocalDateForDataPicker();
        taxComboBoxObjectList = new ArrayList<>();
        nameProductComboBoxObjectList = new ArrayList<>();
        unitMeasureComboBoxObjectList = new ArrayList<>();
        productTable.getItems().clear();
        productTable.setItems(getFirstRow());
        taxComboBox.setValue(fillTaxComboBox().get(3));
        unitMeasureComboBox.setValue(fillUnitMeasureComboBox().get(0));
        //productNameComboBox.setValue(fillProductNameComboBox().get(0));
        productNameComboBox.setEditable(true);
        productNameComboBox.setMinWidth(300);
        addComboBoxObjectToList();
        generateInvoiceNumber();
    }
    @FXML
    void addNewCustomerAction(ActionEvent event) {
        String[] splitedItem = new String[0];
        if(!validateCustomerField()) return;
        String nameCustomer = customerNameTF.getText();
        String [] splited = nameCustomer.split("\\s+");
//        if(splited.length ==1){
//            message("Imie i nazwisko jest niepoprawne!",Alert.AlertType.NONE,"Wiadomość");
//            return;
//        } else if(splited.length >2){
//            message("Imie i nazwisko jest niepoprawne!",Alert.AlertType.NONE,"Wiadomość");
//            return;
//        }
//        if(selectCustomerCB.getSelectionModel().getSelectedItem() != null) {
//            splitedItem = selectCustomerCB.getSelectionModel().getSelectedItem().split("\\s");
//
//            String nipOrPesel = splitedItem[splitedItem.length - 1].substring(1, splitedItem[splitedItem.length - 1].length() - 1);
//            if (nipOrPesel.length() == 10) {
//                customer = customerService.findCustomerByNip(nipOrPesel);
//            } else {
//                customer = customerService.findCustomerByPesel(nipOrPesel);
//            }
//        }
            if (!(customerService.countCustomerByPesel(peselTF.getText()))){
                customer = new Customer();
                customer.setName(splited[0]);
                customer.setSurname(splited[1]);
                customer.setAddress(streetTF.getText());
                customer.setCity(addressTF.getText());
                customer.setPesel(peselTF.getText());
                customer.setPostalCode(postalCodeTF.getText());
                customer.setAccountNumber(accountNumberTF.getText());
                customerService.save(customer);
                accountNumberTF.setText(setAccountNumber(customer.getAccountNumber()));
                customerList.add(customer.getName()+" "+customer.getSurname()+", "+customer.getAddress()+" ("+customer.getPesel()
                        +")");
                selectCustomerCB.setItems(customerList);
                new ComboBoxAutoComplete<String>(selectCustomerCB);
//            } else {
//                if(customer == null)
//                customer = customerService.findCustomer(splited[0], splited[1], streetTF.getText());
//
//                if(customer.getPesel()==null)
//                customer.setPesel(peselTF.getText());
//                if(customer.getAccountNumber() == null)
//                customer.setAccountNumber(accountNumberTF.getText());
//
//                customerService.save(customer);
            }else{
                message("Takie klient już istanieje.", Alert.AlertType.NONE, "Sukces");
                return;
            }

            if (identityCardService.findByCustomer(customer) == null) {
                identityCard = new IdentityCard();
                identityCard.setReleaseDate(String.valueOf(releaseDateTF.getValue()));
                identityCard.setSeriaAndNumber(seriaAndNumberIdCard.getText());
                identityCard.setOrganization(releaseByTF.getText());
                identityCard.setCustomer(customer);
                identityCardService.save(identityCard);
                message("Klient został dodany do listy klientów. ", Alert.AlertType.NONE, "Sukces");
                //message("Klient został zaktualizowany!",Alert.AlertType.NONE,"Wiadomość");
            }else{
                message("Takie klient już istanieje.", Alert.AlertType.NONE, "Sukces");
            }
        }

    @FXML
    void clearData(ActionEvent event) {
        customerNameTF.setText("");
        addressTF.setText("");
        streetTF.setText("");
        peselTF.setText("");
        postalCodeTF.setText("");
        seriaAndNumberIdCard.setText("");
        releaseDateTF.setValue(null);
        releaseByTF.setText("");
        accountNumberTF.setText("");
        selectCustomerCB.getItems().clear();
        selectCustomerCB.setItems(fillCustomerComboBox());
        new ComboBoxAutoComplete<String>(selectCustomerCB);

        setLocalDateForDataPicker();
    }
    @FXML
    void clearTableAction(ActionEvent event) {
        forPrice.setText("0.00");
        sumNetto.setText("0.00");
        sumVat.setText("0.00");
        taxComboBoxObjectList = new ArrayList<>();
        nameProductComboBoxObjectList = new ArrayList<>();
        unitMeasureComboBoxObjectList = new ArrayList<>();
        productTable.getItems().clear();
        productTable.setItems(getFirstRow());
        taxComboBox.setValue(fillTaxComboBox().get(3));
        unitMeasureComboBox.setValue(fillUnitMeasureComboBox().get(0));
        //productNameComboBox.setValue(fillProductNameComboBox().get(0));
        productNameComboBox.setEditable(true);
        productNameComboBox.setMinWidth(300);
        addComboBoxObjectToList();
    }
    @FXML
    private void addNewRowAction(ActionEvent event) {
        invoiceField = new InvoiceField();
        productTable.getItems().add(new InvoiceField(Integer.toString((productTable.getItems().size()+1)),productNameComboBox = new ComboBox<>(fillProductNameComboBox())
                ,unitMeasureComboBox = new ComboBox<>(fillUnitMeasureComboBox())
                ,1,0, 0,taxComboBox = new ComboBox<>(fillTaxComboBox()),0,0));
        productTable.requestFocus();
        productTable.getSelectionModel().select(productTable.getItems().size()-1);
        productTable.getSelectionModel().focus(productTable.getItems().size()-1);


        taxComboBoxObjectList.add(taxComboBox);
        nameProductComboBoxObjectList.add(productNameComboBox);
        unitMeasureComboBoxObjectList.add(unitMeasureComboBox);
        taxComboBox.setValue(fillTaxComboBox().get(3));
//        productNameComboBox.setValue(fillProductNameComboBox().get(0));
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
    public void changeAmountCellEvent(TableColumn.CellEditEvent editedCell){
        invoiceField = productTable.getSelectionModel().getSelectedItem();
        invoiceField.setAmount(Double.parseDouble(editedCell.getNewValue().toString()));
        selectedValueComboBox = taxComboBox.getSelectionModel().getSelectedItem();
        if(selectedValueComboBox.equals("zw.")){
            selectedValueComboBox = "0%";
        }

        priceNetto = invoiceField.getPriceNetto();
        onlyVat = priceNetto * StringToDoubleConverter(selectedValueComboBox);
        priceBrutto = priceNetto + onlyVat ;
        fillPriceFields(round(priceNetto,2),round(onlyVat,2),round(priceBrutto,2),round(invoiceField.getAmount(),2));
        totalPrice();
    }
    @FXML
    public void changePriceNettoCellEvent(TableColumn.CellEditEvent editedCell){
        invoiceField = productTable.getSelectionModel().getSelectedItem();
        invoiceField.setPriceNetto(Double.parseDouble(editedCell.getNewValue().toString()));
        selectedValueComboBox = taxComboBox.getSelectionModel().getSelectedItem();
        if(selectedValueComboBox.equals("zw.")){
            selectedValueComboBox = "0%";
        }
        priceNetto = invoiceField.getPriceNetto();
        onlyVat = priceNetto * StringToDoubleConverter(selectedValueComboBox);
        priceBrutto = priceNetto + onlyVat ;
        fillPriceFields(round(priceNetto,2),round(onlyVat,2),round(priceBrutto,2),round(invoiceField.getAmount(),2));

        totalPrice();
    }

    @FXML
    public void changeProductValueCellEvent(TableColumn.CellEditEvent editedCell){
//        invoiceField = productTable.getSelectionModel().getSelectedItem();
//        invoiceField.setProductValue(Double.parseDouble(editedCell.getNewValue().toString()));
//        selectedValueComboBox = taxComboBox.getSelectionModel().getSelectedItem();
//
//        priceNetto = invoiceField.getProductValue() * invoiceField.getAmount();
//        onlyVat = priceNetto * StringToDoubleConverter(selectedValueComboBox);
//        priceBrutto = priceNetto + onlyVat ;
//        fillPriceFields(round(priceNetto,2),round(onlyVat,2),round(priceBrutto,2),round(invoiceField.getAmount(),2));
//        totalPrice();
    }
    @FXML
    public void changePriceVatCellEvent(TableColumn.CellEditEvent editedCell){
//        invoiceField = productTable.getSelectionModel().getSelectedItem();
//        invoiceField.setPriceVat(Double.parseDouble(editedCell.getNewValue().toString()));
//        selectedValueComboBox = taxComboBox.getSelectionModel().getSelectedItem();
//
//        double tax = StringToDoubleConverter(selectedValueComboBox);
//        onlyVat = invoiceField.getPriceVat();
//        priceNetto = onlyVat * 100/ (tax*100);
//        priceBrutto = priceNetto + onlyVat ;
//        fillPriceFields(round(priceNetto,2),round(onlyVat,2),round(priceBrutto,2),round(invoiceField.getAmount(),2));
//        totalPrice();
    }
    @FXML
    public void changePriceBruttoCellEvent(TableColumn.CellEditEvent editedCell){
//        invoiceField = productTable.getSelectionModel().getSelectedItem();
//        invoiceField.setPriceBrutto(Double.parseDouble(editedCell.getNewValue().toString()));
//        selectedValueComboBox = taxComboBox.getSelectionModel().getSelectedItem();
//
//        priceBrutto = round(invoiceField.getPriceBrutto(),2) ;
//        priceNetto = priceBrutto *100 /(StringToDoubleConverter(selectedValueComboBox)*100 +100);
//        onlyVat = priceBrutto - priceNetto ;
//
//        fillPriceFields(round(priceNetto,2),round(onlyVat,2),round(priceBrutto,2),round(invoiceField.getAmount(),2));
//        totalPrice();
    }

    @FXML
    void createInvoiceAction(ActionEvent event)throws DocumentException, IOException {
        boolean sameInvoiceNumber = false;
        long idTransaction =0;
        double getProductAmount = 0;
        double calculateAmount = 0;
        Optional<ButtonType> result = null;
        String[] splited = null;
        boolean countInvoiceNum = invoiceService.countInvoiceNumber(invoiceNumberTF.getText(),"VatRR");
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

        //if(!countInvoiceNum){
            if(!validateAllField()) {
                return;
            }
            String nameCustomer = customerNameTF.getText();
            String [] nameSplited = nameCustomer.split("\\s+");
            if(nameSplited.length ==1){
                message("Imie i nazwisko jest niepoprawne!",Alert.AlertType.NONE,"Wiadomość");
                return;
            } else if(nameSplited.length >2){
                message("Imie i nazwisko jest niepoprawne!",Alert.AlertType.NONE,"Wiadomość");
                return;
            }
            for (InvoiceField row : productTable.getItems()){
                if(row.getPriceNetto() ==0){
                    message("Cena netto wynosi 0 zł!",Alert.AlertType.NONE,"Wiadomość");
                    return;
                }
            }
        //}


        Seller seller = sellerService.find(1);

        //String date2 = "22/04/17";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDateIssue= LocalDate.parse(issueDate.getEditor().getText(), formatter);
        String issueDateToString = issueDate.getEditor().getText();
        String sellDateToString = sellDate.getEditor().getText();
        String paymentDateToString = paymentDate.getEditor().getText();

        LocalDate localDateSell= LocalDate.parse(sellDate.getEditor().getText(), formatter);
        LocalDate localDatePay= LocalDate.parse(paymentDate.getEditor().getText(), formatter);
        paymentDate.setValue(localDatePay);
        //localDate =  LocalDate.parse(sellDateToString);
        sellDate.setValue(localDateSell);
       // localDate =  LocalDate.parse(issueDateToString);
        issueDate.setValue(localDateIssue);

        String paymentDateToString2 = String.valueOf(paymentDate.getValue());
        String sellDate2 = String.valueOf(sellDate.getValue());
        String issueDate2 = String.valueOf(issueDate.getValue());

        if (sameInvoiceNumber == true) {
            invoice = invoiceService.findByinvoiceNumber(invoiceNumberTF.getText(),"VatRR");
            date = invoice.getDate();
        } else {
            date = new Date();
        }
        date.setIssueDate(issueDate2);
        date.setSellDate(sellDate2);
        date.setPaymentDate(paymentDateToString2);
        dateService.save(date);

        //custommer
            //String nameCustomer = customerNameTF.getText();
            splited = nameCustomer.split("\\s+");
            if (!(customerService.countCustomerByPesel(peselTF.getText()))) {
                customer = new Customer();

                customer.setName(splited[0]);
                customer.setSurname(splited[1]);
                customer.setAddress(streetTF.getText());
                customer.setCity(addressTF.getText());
                customer.setPostalCode(postalCodeTF.getText());
                customer.setPesel(peselTF.getText());
                customer.setAccountNumber(accountNumberTF.getText());
                customerService.save(customer);
                customerList.add(customer.getName()+" "+customer.getSurname()+", "+customer.getAddress()+" ("+customer.getPesel()
                +")");
                selectCustomerCB.setItems(customerList);
                new ComboBoxAutoComplete<String>(selectCustomerCB);
            }else {
                if (customer == null)
                    customer = customerService.findCustomerByPesel(peselTF.getText());
            }
//        if(customer.getAccountNumber() == null && accountNumberTF.getText() != null){
//            customer.setAccountNumber(accountNumberTF.getText());
//            customerService.save(customer);
//        }
//        if(customer.getPesel() == null){
//            customer.setPesel(peselTF.getText());
//            customerService.save(customer);
//        }
        identityCard = identityCardService.findByCustomer(customer);
        if(identityCard == null){
            identityCard = new IdentityCard();
            identityCard.setReleaseDate(String.valueOf(releaseDateTF.getValue()));
            identityCard.setSeriaAndNumber(seriaAndNumberIdCard.getText());
            identityCard.setOrganization(releaseByTF.getText());
            identityCard.setCustomer(customer);
            identityCardService.save(identityCard);
        }

        // invoice
        if (sameInvoiceNumber == true) {
            invoice = invoiceService.findByinvoiceNumber(invoiceNumberTF.getText(),"VatRR");
        } else {
            invoice = new Invoice();
        }

        invoice.setInvoiceNumber(invoiceNumberTF.getText());
        invoice.setPaidType(paidType.getSelectionModel().getSelectedItem());
        invoice.setInvoiceType("VatRR");
        invoice.setDate(date);
        invoiceService.save(invoice);

        List idTransactionList = transactionService.findByInvoice(invoice.getInvoiceNumber(), invoice.getId(),"VatRR");

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
            transaction.setType("kupno");

            store = storeService.findByName(row.getNameProduct().getEditor().getText());
            if (sameInvoiceNumber == false) {
                if (store == null) {

                }else {
                    getProductAmount = store.getAmount();
                    calculateAmount = getProductAmount + row.getAmount();

                    transaction.setType("kupno");

                    store.setAmount(calculateAmount);
                    storeService.update(store);
                    transaction.setStoreAmount(calculateAmount);

                    transaction.setStore(store);
                }

            }
            transaction.setStore(store);
            transactionService.save(transaction);

            //product

            if (productService.checkIfExist(row.getNameProduct().getEditor().getText())) {
                product = productService.findByName(row.getNameProduct().getEditor().getText());
            } else {
                product = new Product();
                product.setName(row.getNameProduct().getEditor().getText());
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
        accountNumberTF.setText(setAccountNumber(customer.getAccountNumber()));

        Company companySeller = companyService.findBySeller(seller);
        Settings settings = settingsService.find(1);
        String path = settings.getPath();
        System.out.println(date.getPaymentDate());
        VatRRInvoicePDF pdfCreator = new VatRRInvoicePDF(invoice ,date,companySeller,customer,productTable, paidType,
                identityCard,promotionFoundCB,invoiceNumberTF.getText(), path,paymentDateToString2);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(statementRadioButton.isSelected()){
            StatementPDF statementPDF = new StatementPDF(customer,veterinaryInspectorateCB,issueDate,productTable,path,identityCard);
        }else if(statementAndRodoRadioButton.isSelected()){
            StatementAndRodoPDF statementAndRodoPDF = new StatementAndRodoPDF(customer,veterinaryInspectorateCB,issueDate,productTable,path,identityCard);
        }
        SellAgreement sellAgreement = new SellAgreement(customer,issueDate,productTable,path,identityCard);
    }
    public String setAccountNumber(String string){
        String accountNum = string.replaceAll(" ","");
        String numberAccount = "";
        int counter2 = 0;
        for (int i = 0; i < accountNum.length(); i++) {

            numberAccount += accountNum.charAt(i);

            if (i == 1) numberAccount += " ";
            if (i > 1) counter2++;
            if (counter2 == 4) {
                numberAccount += " ";
                counter2 = 0;
            }
        }
        return numberAccount;
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
            int counter=1;

            for (InvoiceField row : productTable.getItems()) {
                row.setLp(String.valueOf(counter));
                lpColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                lpColumn.setCellValueFactory(  new PropertyValueFactory<>("lp"));

                counter++;
            }
        }

        productTable.refresh();
        totalPrice();
    }

    @FXML
    private void selectCustomerAction(Event event) {

        if(selectCustomerCB.getSelectionModel().getSelectedItem() != null) {
            String[] splitedItem = selectCustomerCB.getSelectionModel().getSelectedItem().split("\\(");
//            String[] removedEmptyElemnt = Arrays.stream(splitedItem).filter(value -> value != null && value.length() > 0).toArray(size -> new String[size]);
//            if (removedEmptyElemnt.length == 3) {
//                customer = customerService.findCustomer(removedEmptyElemnt[0], removedEmptyElemnt[1], removedEmptyElemnt[2]);
//            } else {
//                customer = customerService.findCustomer(removedEmptyElemnt[0], removedEmptyElemnt[1], removedEmptyElemnt[2] + " " + removedEmptyElemnt[3]);
//            }
            String peselOrNip = splitedItem[splitedItem.length-1].substring(0,splitedItem[splitedItem.length-1].length()-1);
            customer = customerService.findCustomerByPesel(peselOrNip);

            fillCustomerFiled(customer);
        }
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
//    @FXML
//    private void productNameAction(ActionEvent event)  {
//        if(productNameComboBox.getEditor().getText().equals("Usługa transportowa")){
//            unitMeasureComboBox.getSelectionModel().select(1);
//        }else{
//            unitMeasureComboBox.getSelectionModel().select(0);
//        }
//    }
    private void clickTaxComboBoxAction(MouseEvent event)  {
        for (int i=0; i<taxComboBoxObjectList.size(); i++){
            if(event.getSource().equals(taxComboBoxObjectList.get(i))) {
                productTable.getSelectionModel().select(i);
                productTable.getFocusModel().focus(0);
            }
        }
    }
    @FXML
    private void clickNameProductComboBox(MouseEvent event) {
        for (int i = 0; i < nameProductComboBoxObjectList.size(); i++) {
            if (event.getSource().equals(nameProductComboBoxObjectList.get(i))) {
                productTable.getSelectionModel().select(i);

            }
        }
    }
    @FXML
    private void clickUnitMeasureComboBox(MouseEvent event) {
        for (int i = 0; i < unitMeasureComboBoxObjectList.size(); i++) {
            if (event.getSource().equals(unitMeasureComboBoxObjectList.get(i))) {
                productTable.getSelectionModel().select(i);

            }
        }
    }
    @FXML
    void updateCustomerAction(ActionEvent event) {
        if(customer == null) {
            customer = customerService.findCustomerByPesel(peselTF.getText());
        }
        identityCard = identityCardService.findByCustomer(customer);

        String nameCustomer = customerNameTF.getText();
        String[] splited = nameCustomer.split("\\s+");
        customer.setName(splited[0]);
        customer.setSurname(splited[1]);
        customer.setAddress(streetTF.getText());
        customer.setCity(addressTF.getText());
        customer.setPesel(peselTF.getText());
        customer.setPostalCode(postalCodeTF.getText());
        customer.setAccountNumber(accountNumberTF.getText());
        customerService.update(customer);

        identityCard.setReleaseDate(String.valueOf(releaseDateTF.getValue()));
        identityCard.setSeriaAndNumber(seriaAndNumberIdCard.getText());
        identityCard.setOrganization(releaseByTF.getText());
        identityCard.setCustomer(customer);
        identityCardService.update(identityCard);


        message("Dane klienta zostały zaaktualizowane.", Alert.AlertType.NONE, "Informacja");
        selectCustomerCB.getItems().clear();
        customerList.clear();
        selectCustomerCB.setItems(fillCustomerComboBox());
        new ComboBoxAutoComplete<String>(selectCustomerCB);
    }
    @FXML
    void changeInvoiceNumber(ActionEvent event) throws IOException, DocumentException {
        long idTransaction;
        Settings settings=null;
        Invoice oldInvoice = invoiceService.findByinvoiceNumber(oldInvoiceNumber.getText(),"VatRR");
        if(oldInvoice != null){
            //if(!invoiceService.countInvoiceNumber(newInvoiceNumber.getText(),"VatRR")){
            Invoice newInvoice = invoiceService.findByinvoiceNumber(newInvoiceNumber.getText(),"VatRR");
            if(newInvoice !=null) {
                Date date = dateService.findByInvoice(newInvoice);
                List idTransactionList = transactionService.findByInvoice(newInvoice.getInvoiceNumber(), newInvoice.getId(), "VatRR");
                for (int i = 0; i < idTransactionList.size(); i++) {
                    idTransaction = (Long) idTransactionList.get(i);
                    Transaction transaction = transactionService.find(idTransaction);
                    ProductTransaction productTransaction = productTransactionService.findByTransaction(transaction);
                    productTransactionService.delete(productTransaction);
                }
                for (int i = 0; i < idTransactionList.size(); i++) {
                    idTransaction = (Long) idTransactionList.get(i);
                    transactionService.delete(idTransaction);
                }
                invoiceService.delete(newInvoice.getId());
                dateService.delete(date.getId());

                settings = settingsService.find(1);
                deleteFile(settings.getPath(),oldInvoice.getInvoiceNumber());
            }
            oldInvoice.setInvoiceNumber(newInvoiceNumber.getText());
                invoiceService.save(oldInvoice);
                recreateInvoiceField(invoiceNumberTF.getText());
                Customer customer = customerService.findCustomerByPesel(peselTF.getText());

                Company companyCustomer = companyService.findByCustomer(customer);
                IdentityCard identityCard = identityCardService.findByCustomer(customer);
                Seller seller = sellerService.find(1);
                Company companySeller = companyService.findBySeller(seller);
                settings = settingsService.find(1);
                String path = settings.getPath();

                deleteFile(path,oldInvoiceNumber.getText());
                Date date = oldInvoice.getDate();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String paymentDateToString = paymentDate.getEditor().getText();

                LocalDate localDatePay= LocalDate.parse(paymentDate.getEditor().getText(), formatter);
                paymentDate.setValue(localDatePay);

                String paymentDateToString2 = String.valueOf(paymentDate.getValue());

                VatRRInvoicePDF pdfCreator = new VatRRInvoicePDF(oldInvoice ,date,companySeller,customer,productTable, paidType,
                        identityCard,promotionFoundCB,newInvoiceNumber.getText(), path,paymentDateToString2);
                invoiceNumberTF.setText(newInvoiceNumber.getText());
                newInvoiceNumber.setText("");
                oldInvoiceNumber.setText("");


//            }else{
//                message("Taki numer faktury już istnieje. Wybierz inny.", Alert.AlertType.NONE, "Informacja");
//            }

        }else{
            message("Nie istnieje faktura o takim numerze.", Alert.AlertType.NONE, "Informacja");
        }
    }
    public void deleteFile(String path, String invoiceNumber){
        invoiceNumber = invoiceNumber.replaceAll("/","-");
        String yearWithInvoiceNumber;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        yearWithInvoiceNumber = Integer.toString(year);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        Month monthEnum = Month.values()[month];

        File file= new File(path+"\\VATRR\\"+yearWithInvoiceNumber+"\\"+monthEnum+"\\"+invoiceNumber+".pdf");
        if(file.exists()){
            file.delete();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        taxComboBoxObjectList = new ArrayList<>();
        nameProductComboBoxObjectList = new ArrayList<>();
        unitMeasureComboBoxObjectList = new ArrayList<>();
        selectCustomerCB.setItems(fillCustomerComboBox());
        new ComboBoxAutoComplete<String>(selectCustomerCB);
        promotionFoundCB.setItems(fillPromotionFoundComboBox());
        promotionFoundCB.setValue(fillPromotionFoundComboBox().get(0));
        setLocalDateForDataPicker();
        generateInvoiceNumber();
        setValueForCombobox();
        productTable.setItems(getFirstRow());
//        List<Store> allProductList = (List<Store>) storeService.findAll();
//        if(allProductList.size() >0)
//            productNameComboBox.setValue(fillProductNameComboBox().get(0));

        productNameComboBox.setEditable(true);
        productNameComboBox.setMinWidth(300);
        taxComboBox.setValue(fillTaxComboBox().get(3));
        unitMeasureComboBox.setValue(fillUnitMeasureComboBox().get(0));
        veterinaryInspectorateCB.setItems(fillVeterinaryInspectorateComboBox());
        veterinaryInspectorateCB.setValue(fillVeterinaryInspectorateComboBox().get(0));

        addComboBoxObjectToList();
        productTable.setEditable(true);
        lpColumn.setEditable(false);

        lpColumn.setCellFactory(AcceptOnExitTableCell.forTableColumn());
        amountColumn.setCellFactory(AcceptOnExitTableCell.forTableColumn(new DoubleStringConverter()));
        productValueColumn.setCellFactory(AcceptOnExitTableCell.forTableColumn((new DoubleStringConverter())));
        priceNettoColumn.setCellFactory(AcceptOnExitTableCell.forTableColumn((new DoubleStringConverter())));
        priceVatColumn.setCellFactory(AcceptOnExitTableCell.forTableColumn((new DoubleStringConverter())));
        priceBruttoColumn.setCellFactory(AcceptOnExitTableCell.forTableColumn((new DoubleStringConverter())));


        lpColumn.setCellValueFactory(  new PropertyValueFactory<>("lp"));
        productNameColumn.setCellValueFactory(  new PropertyValueFactory<>("nameProduct"));
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
    private void setLocalDateForDataPicker(){
        setDatePattern(issueDate);
        setDatePattern(sellDate);
        setDatePattern(paymentDate);
        setDatePattern(releaseDateTF);
        issueDate.setValue(LocalDate.now());
        sellDate.setValue(LocalDate.now());
        paymentDate.setValue(LocalDate.now().plusDays(10));
    }
    private ObservableList<String> fillCustomerComboBox(){
        customerList = FXCollections.observableArrayList();
        String nipOrPesel = null;
        Company company = null;
        for (Customer customer: customerService.findAll()) {
            if(customer.getPesel() != null){
                nipOrPesel = customer.getPesel();
                customerList.add(customer.getName()+" "+customer.getSurname()+", "
                        +customer.getAddress()+"  ("+nipOrPesel+")");
            }
//            if(customer.getName() == null){
//                customerList.add(company.getName()+ ", "
//                        +customer.getAddress()+"  ("+nipOrPesel+")");
//            }else{

          //  }
        }
        return customerList;
    }
    private ObservableList<String> fillPaidTypeComboBox(){
        ObservableList<String> paidTypeList = FXCollections.observableArrayList();
        paidTypeList.add("Przelew");
        paidTypeList.add("Gotówka");

        return paidTypeList;
    }
    private void setValueForCombobox(){
        paidType.getItems().setAll(fillPaidTypeComboBox());
        paidType.setValue(fillPaidTypeComboBox().get(0));
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
        fillUnitMeasureList.add("tona");
        //fillUnitMeasureList.add("kg.");
        fillUnitMeasureList.add("szt.");
        return fillUnitMeasureList;
    }
    private  ObservableList<String> fillPromotionFoundComboBox(){
        ObservableList<String> fillPromotionFoundList = FXCollections.observableArrayList();
        fillPromotionFoundList.add("zboża");
        fillPromotionFoundList.add("-");
        fillPromotionFoundList.add("-");
        return fillPromotionFoundList;
    }
    private  ObservableList<String> fillVeterinaryInspectorateComboBox(){
        ObservableList<String> fillVeterinaryInspectorateList = FXCollections.observableArrayList();
        fillVeterinaryInspectorateList.add("Jasło");
        fillVeterinaryInspectorateList.add("Krosno");
        fillVeterinaryInspectorateList.add("Strzyżów");
        return fillVeterinaryInspectorateList;
    }
    private ObservableList<InvoiceField> getFirstRow(){

        ObservableList<InvoiceField> selectComboBox = FXCollections.observableArrayList();
        selectComboBox.add(new InvoiceField("1",productNameComboBox = new ComboBox<>(fillProductNameComboBox()),
                unitMeasureComboBox = new ComboBox<>(fillUnitMeasureComboBox()),1,0,0 ,
                taxComboBox = new ComboBox<>(fillTaxComboBox()),0,0));
        unitMeasureComboBox.setValue(fillUnitMeasureComboBox().get(0));
        return selectComboBox;
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
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
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
        totalNetto = round(totalNetto,2);
        totalVat = round(totalVat,2);
        allForPrice = round(allForPrice,2);

        String totalNettoString = df2.format(totalNetto).replaceAll(",",".");
        String totalVatString = df2.format(totalVat).replaceAll(",",".");
        String allForPriceString = df2.format(allForPrice).replaceAll(",",".");
        sumNetto.setText(totalNettoString);
        sumVat.setText(totalVatString);
        forPrice.setText(allForPriceString);
    }
    private void fillCustomerFiled(Customer customer){
        identityCard = identityCardService.findByCustomer(customer);
        if(customer.getName()!= null) {
            customerNameTF.setText(customer.getName() + " " + customer.getSurname());
        }else{
            customerNameTF.setText("");
        }
        streetTF.setText(customer.getAddress());
        postalCodeTF.setText(customer.getPostalCode());
        addressTF.setText(customer.getCity());
        if(customer.getPesel() != null) peselTF.setText(customer.getPesel());
        else peselTF.setText("");
        if(customer.getAccountNumber() != null){
            accountNumberTF.setText(setAccountNumber(customer.getAccountNumber()));

        }

        if(identityCard != null) {
            seriaAndNumberIdCard.setText(identityCard.getSeriaAndNumber());
            setDatePattern(releaseDateTF);
            String releaseDate = identityCard.getReleaseDate();
            LocalDate localDate = LocalDate.parse(identityCard.getReleaseDate()); //parse String to localdate
            releaseDateTF.setValue(localDate);
            releaseByTF.setText(identityCard.getOrganization());
        }else{
            seriaAndNumberIdCard.setText("");
            releaseDateTF.getEditor().setText("");
            releaseByTF.setText("");
        }
    }
    private  ObservableList<String> fillProductNameComboBox(){
        List<Store> allProductList = (List<Store>) storeService.findAll();
        ObservableList<String> allProductListObservable = FXCollections.observableArrayList();

        for (Store s: allProductList ) {
            allProductListObservable.add(s.getName());
        }
        return allProductListObservable;
    }
    private void generateInvoiceNumber(){
        int invoiceCounter=1;
        int invoiceCounterTmp=1;
        String counterToString;
        String invoiceNumberDB,currentMonthInvoiceNumber;
        String invoiceNumber = String.valueOf(issueDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        long maxId=invoiceService.getLastInvoiceNumeber("VatRR");
        if(maxId ==0) invoiceNumberDB ="1"+ invoiceNumber.substring(2);
        else {
            Invoice invoice = invoiceService.find(maxId);
            invoiceNumberDB = invoice.getInvoiceNumber();

            String currentMonth = invoiceNumber.substring(3,5);
            if(invoiceNumberDB.substring(2, 3).equals("/")) currentMonthInvoiceNumber = invoiceNumberDB.substring(3,5);
            else currentMonthInvoiceNumber = invoiceNumberDB.substring(2,4);

            if (invoiceService.countInvoiceNumber(invoiceNumberDB,"VatRR") && currentMonth.equals(currentMonthInvoiceNumber) ) {
                if (invoiceNumberDB.substring(2, 3).equals("/")) invoiceNumberDB = invoiceNumberDB.substring(0, 2);
                else invoiceNumberDB = invoiceNumberDB.substring(0, 1);
                invoiceCounterTmp = Integer.parseInt(invoiceNumberDB);
                invoiceCounterTmp++;
                invoiceCounter = invoiceCounterTmp;
                counterToString = Integer.toString(invoiceCounterTmp);
                invoiceNumberDB = counterToString + invoiceNumber.substring(2);
                while(invoiceService.countInvoiceNumber(invoiceNumberDB,"VatRR")){
                    invoiceCounterTmp++;
                    counterToString = Integer.toString(invoiceCounterTmp);
                    invoiceNumberDB = counterToString + invoiceNumber.substring(2);
                }
            } else {
                invoiceNumberDB = "1" + invoiceNumber.substring(2);
                while(invoiceService.countInvoiceNumber(invoiceNumberDB,"VatRR")){
                    invoiceCounterTmp++;
                    counterToString = Integer.toString(invoiceCounterTmp);
                    invoiceNumberDB = counterToString + invoiceNumber.substring(2);
                }
            }
        }


        invoiceNumberTF.setText(invoiceNumberDB);
    }
    public boolean validateAllField(){
        boolean productNameEmpty = false;
        for (InvoiceField row : productTable.getItems()) {
            if(row.getNameProduct().getEditor().getText().equals("")){
                productNameEmpty = true;
                break;
            }
        }
        if(customerNameTF.getText().equals("") || addressTF.getText().equals("") ||
                streetTF.getText().equals("") || peselTF.getText().equals("") || postalCodeTF.getText().equals("") || invoiceNumberTF.getText().equals("")
                || issueDate.getEditor().getText().equals("")|| sellDate.getEditor().getText().equals("")|| paymentDate.getEditor().getText().equals("")
                || seriaAndNumberIdCard.getText().equals("") || releaseDateTF.getEditor().getText().equals("") || releaseByTF.getText().equals("")
        || productNameEmpty) {
            message("Nie wszystkie pola są wypełnione", Alert.AlertType.NONE, "Informacja");
            return false;
        }
        return true;
    }
    private void message(String message, Alert.AlertType alertType,String typeMessage){
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(typeMessage);
        alert.showAndWait();
    }
    public boolean validateCustomerField(){
        if(customerNameTF.getText().equals("") || addressTF.getText().equals("") ||
                streetTF.getText().equals("") || postalCodeTF.getText().equals("") || peselTF.getText().equals("")
                || seriaAndNumberIdCard.getText().equals("") || releaseDateTF.getEditor().getText().equals("") || releaseByTF.getText().equals("")) {
            message("Nie wszystkie pola klienta są wypełnione", Alert.AlertType.NONE, "Informacja");
            return false;
        }
        return true;
    }
    @FXML
    public void onEnter(ActionEvent actionEvent){
        recreateInvoiceField(invoiceNumberTF.getText());
    }
    public void recreateInvoiceField(String invoiceNumber){
        setDatePattern(issueDate);
        setDatePattern(sellDate);
        setDatePattern(paymentDate);
        setDatePattern(releaseDateTF);
        taxComboBoxObjectList = new ArrayList<>();
        nameProductComboBoxObjectList = new ArrayList<>();
        unitMeasureComboBoxObjectList = new ArrayList<>();
        ObservableList<String> allProductListObservableTmp = FXCollections.observableArrayList();
        int productIndex=0;
        int taxIndex =0;
        int unitMeasureIndex =0;

        if(!newInvoiceNumber.getText().equals("")){
            invoiceNumber = newInvoiceNumber.getText();
        }
        if(invoiceService.countInvoiceNumber(invoiceNumber,"VatRR")) {
            Invoice invoice = invoiceService.findByinvoiceNumber(invoiceNumber,"VatRR");
            LocalDate localDate = LocalDate.parse(invoice.getDate().getIssueDate());
            issueDate.setValue(localDate);
            localDate = LocalDate.parse(invoice.getDate().getSellDate()); //parse String to localdate
            sellDate.setValue(localDate);
            localDate = LocalDate.parse(invoice.getDate().getPaymentDate()); //parse String to localdate
            //paymentDate.getEditor().setText(invoice.getDate().getPaymentDate());
            paymentDate.setValue(localDate);
            paidType.setValue(invoice.getPaidType());

            List idTransactionList = transactionService.findByInvoice(invoice.getInvoiceNumber(), invoice.getId(),"VatRR");
            long idTransaction = (Long) idTransactionList.get(0);
            Transaction transaction = transactionService.find(idTransaction);
            IdentityCard identityCard = identityCardService.findByCustomer(transaction.getCustomer());
            customerNameTF.setText(transaction.getCustomer().getName() + " " + transaction.getCustomer().getSurname());
            streetTF.setText(transaction.getCustomer().getAddress());
            postalCodeTF.setText(transaction.getCustomer().getPostalCode());
            addressTF.setText(transaction.getCustomer().getCity());
            peselTF.setText(transaction.getCustomer().getPesel());
            seriaAndNumberIdCard.setText(identityCard.getSeriaAndNumber());
            releaseByTF.setText(identityCard.getOrganization());
            releaseDateTF.getEditor().setText(identityCard.getReleaseDate());
            accountNumberTF.setText(setAccountNumber(transaction.getCustomer().getAccountNumber()));
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
                    if (allProductListObservableTmp.get(t).equals(productTransaction.getProduct().getName())){
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

                addComboBoxObjectToList();
                taxComboBox.setOnMouseClicked(this::clickTaxComboBoxAction);
                taxComboBox.setOnAction(this::taxComboBoxAction);
                productNameComboBox.setOnMouseClicked(this::clickNameProductComboBox);
                unitMeasureComboBox.setOnMouseClicked(this::clickUnitMeasureComboBox);
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
