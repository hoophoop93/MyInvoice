package com.kmichali.controller;

import com.itextpdf.text.DocumentException;
import com.kmichali.config.StageManager;
import com.kmichali.model.*;
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
public class VatRRInvoiceController implements Initializable {

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
    private TextField peselOrNipTF;
    @FXML
    private TextField idCardTF;
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
    private RadioButton peselRadioButton;
    @FXML
    private RadioButton nipRadioButton;

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

    private List<ComboBox> taxComboBoxObjectList = new ArrayList<>();
    private InvoiceField invoiceField;
    private double priceNetto;
    private double onlyVat;
    private double priceBrutto;
    private String selectedValueComboBox;


    @FXML
    void menuInvoiceVatAction(ActionEvent event)throws UnsupportedEncodingException {
        stageManager.switchScene(FxmlView.INVOICEVATSTAGE);
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
    void addNewCustomerAction(ActionEvent event) {

    }
    @FXML
    void nipRadioButtonAction(ActionEvent event) {
        if(nipRadioButton.isSelected()){
            radioButtonLabel.setText("Nip");
        }
    }

    @FXML
    void peselRadioButtonAction(ActionEvent event) {
        if(peselRadioButton.isSelected()){
            radioButtonLabel.setText("Pesel");
        }
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
        taxComboBox.setValue(fillTaxComboBox().get(3));
        unitMeasureComboBox.setValue(fillUnitMeasureComboBox().get(0));
        invoiceField.setLp(Integer.toString((productTable.getItems().size())));

        productNameComboBox.setEditable(true);
        productNameComboBox.setMinWidth(300);
        taxComboBox.setOnAction(this::clickComboBox);
    }

//    @FXML
//    public void changeNameProductCellEvent(TableColumn.CellEditEvent editedCell){
//        invoiceField = productTable.getSelectionModel().getSelectedItem();
//        invoiceField.setNameProduct(editedCell.getNewValue().toString());
//
//    }
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
    void createInvoiceAction(ActionEvent event)throws DocumentException, IOException {
        Seller seller = sellerService.find(1);
        invoice= invoiceService.find(1);
        date = dateService.find(1);
        customer = customerService.find(1);
        Company companyCustomer=companyService.findByCustomer(customer);
        Company companySeller=companyService.findBySeller(seller);
        identityCard = identityCardService.findByCustomer(customer);
        VatRRInvoicePDF pdfCreator = new VatRRInvoicePDF(invoice ,date,companySeller,companyCustomer,productTable, paidType,identityCard,promotionFoundCB);
        //StatementPDF statementPDF = new StatementPDF(customer,veterinaryInspectorateCB,issueDate,productTable);
    }

    @FXML
    void invoiceTableAction(MouseEvent event) {

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
    private void selectCustomerAction(Event event) {

        if(selectCustomerCB.getSelectionModel().getSelectedItem() != null) {
            String[] splitedItem = selectCustomerCB.getSelectionModel().getSelectedItem().split("\\s");
            String[] removedEmptyElemnt = Arrays.stream(splitedItem).filter(value -> value != null && value.length() > 0).toArray(size -> new String[size]);
            if (removedEmptyElemnt.length == 3) {
                customer = customerService.findCustomer(removedEmptyElemnt[0], removedEmptyElemnt[1], removedEmptyElemnt[2]);
            } else {
                customer = customerService.findCustomer(removedEmptyElemnt[0], removedEmptyElemnt[1], removedEmptyElemnt[2] + " " + removedEmptyElemnt[3]);
            }
            fillCustomerFiled(customer);
        }
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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectCustomerCB.setItems(fillCustomerComboBox());
        new ComboBoxAutoComplete<String>(selectCustomerCB);
        promotionFoundCB.setItems(fillPromotionFoundComboBox());
        promotionFoundCB.setValue(fillPromotionFoundComboBox().get(0));
        setLocalDateForDataPicker();
        setValueForCombobox();
        productTable.setItems(getFirstRow());
        productNameComboBox.setEditable(true);
        productNameComboBox.setMinWidth(300);
        taxComboBox.setValue(fillTaxComboBox().get(3));
        unitMeasureComboBox.setValue(fillUnitMeasureComboBox().get(0));
        veterinaryInspectorateCB.setItems(fillVeterinaryInspectorateComboBox());
        veterinaryInspectorateCB.setValue(fillVeterinaryInspectorateComboBox().get(0));

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
        productNameColumn.setCellValueFactory(  new PropertyValueFactory<>("nameProduct"));
        unitMeasureColumn.setCellValueFactory(  new PropertyValueFactory<>("unitMeasure"));
        amountColumn.setCellValueFactory(  new PropertyValueFactory<>("amount"));
        priceNettoColumn.setCellValueFactory(  new PropertyValueFactory<>("priceNetto"));
        productValueColumn.setCellValueFactory(  new PropertyValueFactory<>("productValue"));
        taxColumn.setCellValueFactory(  new PropertyValueFactory<InvoiceField, ComboBox>("tax"));
        priceVatColumn.setCellValueFactory(  new PropertyValueFactory<>("priceVat"));
        priceBruttoColumn.setCellValueFactory(  new PropertyValueFactory<>("priceBrutto"));

        taxComboBox.setOnAction(this::clickComboBox);
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
        issueDate.setValue(LocalDate.now());
        sellDate.setValue(LocalDate.now());
        paymentDate.setValue(LocalDate.now().plusDays(10));
    }
    private ObservableList<String> fillCustomerComboBox(){
        customerList = FXCollections.observableArrayList();
        for (Customer customer: customerService.findAll()) {
            customerList.add(customer.getName()+" "+customer.getSurname()+"    "+customer.getAddress());
        }
        return customerList;
    }
    private ObservableList<String> fillPaidTypeComboBox(){
        ObservableList<String> paidTypeList = FXCollections.observableArrayList();
        paidTypeList.add("Gotówka");
        paidTypeList.add("Przelew");

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
        fillUnitMeasureList.add("szt.");
        fillUnitMeasureList.add("tona");
        fillUnitMeasureList.add("kg.");
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
            totalNetto = totalNetto + row.getPriceNetto();
            totalVat = totalVat + row.getPriceVat();
            totalPrice = totalPrice + row.getPriceNetto();
        }
        double allForPrice = totalPrice + totalVat;
        sumNetto.setText(Double.toString(round(totalNetto,2)));
        sumVat.setText(Double.toString(round(totalVat,2)));
        forPrice.setText(Double.toString(round(allForPrice,2)));
    }
    private void fillCustomerFiled(Customer customer){
        identityCard = identityCardService.findByCustomer(customer);
        customerNameTF.setText(customer.getName()+" "+customer.getSurname());
        streetTF.setText(customer.getAddress());
        postalCodeTF.setText(customer.getPostalCode());
        addressTF.setText(customer.getCity());
        peselOrNipTF.setText(customer.getPesel());
        seriaAndNumberIdCard.setText(identityCard.getSeriaAndNumber());
        setDatePattern(releaseDateTF);
        String releaseDate = identityCard.getReleaseDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(releaseDate, formatter);

        releaseDateTF.setValue(localDate);
        releaseByTF.setText(identityCard.getOrganization());
    }
    private  ObservableList<String> fillProductNameComboBox(){
        List<Store> allProductList = (List<Store>) storeService.findAll();
        ObservableList<String> allProductListObservable = FXCollections.observableArrayList();

        for (Store s: allProductList ) {
            allProductListObservable.add(s.getName());
        }
        return allProductListObservable;
    }
}
