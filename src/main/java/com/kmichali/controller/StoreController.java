package com.kmichali.controller;

import com.kmichali.config.StageManager;
import com.kmichali.model.Seller;
import com.kmichali.model.Settings;
import com.kmichali.model.Store;
import com.kmichali.model.ProductRaport;
import com.kmichali.serviceImpl.*;
import com.kmichali.view.FxmlView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class StoreController  implements Initializable {


    private static DecimalFormat df2 = new DecimalFormat("#,##0.00");

    @Lazy
    @Autowired
    private StageManager stageManager;
    @FXML
    private ComboBox<String> productCB;
    @FXML
    private TextField productName;
    @FXML
    private TextField amount;
    @FXML
    private TableView storeProductsTable;
    @FXML
    TableColumn<Store, String> nameColumn;
    @FXML
    TableColumn<Store, Double> amountColumn;
    @FXML
    TableColumn<Store, String> unitMeasure;
    @FXML
    TableColumn<ProductRaport, String> nameCustomer;
    @FXML
    TableColumn<ProductRaport, String> invoiceNumber;
    @FXML
    TableColumn<ProductRaport, Double> amountTransaction;
    @FXML
    TableColumn<ProductRaport, String> sellDate;
    @FXML
    TableColumn<ProductRaport, Double> wholeAmount;
    @FXML
    TableColumn<ProductRaport, Double> priceNetto;
    @FXML
    TableColumn<ProductRaport, String> type;
    @FXML
    private ComboBox<String> unitMeasureCB;

    Store store;
    List<Store> allProductList;
    ObservableList<String> allProductListObservable;

    @Autowired
    StoreServiceImpl storeService;
    @Autowired
    TransactionServiceImpl transactionService;
    @Autowired
    SellerServiceImpl sellerService;
    @Autowired
    SettingsServiceImpl settingsService;
    @Autowired
    EmptyTransactionImpl emptyTransaction;



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
    void menuInvoiceVatRRAction(ActionEvent event)throws UnsupportedEncodingException {
        Seller seller = sellerService.find(1);
        Settings settings = settingsService.find(1L);
        if(seller == null || settings == null){
            message("Przed rozpoczęciem dodaj swoje dane i ścieżke do zapisu faktur w ustawieniach!!", Alert.AlertType.NONE,"Informacja");
        }else{
            stageManager.switchScene(FxmlView.INVOICERRSTAGE);
        }
    }

    @FXML
    void menuSettingsAction(ActionEvent event) throws UnsupportedEncodingException {
        stageManager.switchScene(FxmlView.SETTINGSSTAGE);
    }
    @FXML
    void addNewProductAction(ActionEvent event) {
        if(productName.getText().equals("") || amount.getText().equals("")){
            message("Pola muszą być wypełnione!", Alert.AlertType.NONE, "Informacja");
        }else {
            store = new Store();
            store.setName(productName.getText());
            store.setAmount(Double.parseDouble(amount.getText().replace(",",".")));
            store.setUnitMeasure(unitMeasureCB.getSelectionModel().getSelectedItem());

            if (!storeService.countProductStore(store.getName())) {
                storeService.save(store);
                allProductList.add(store);
                productCB.setItems(fillProductCB());
                productCB.setValue(allProductListObservable.get(0));
                prepareTableColumn();
                productName.setText("");
                amount.setText("");
                unitMeasureCB.setItems(fillUnitMeasureComboBox());
                unitMeasureCB.setValue(fillUnitMeasureComboBox().get(0));
            } else {
                message("Taki produkt jest już w magazynie!", Alert.AlertType.NONE, "Informacja");
            }
        }

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productCB.setItems(fillProductCB());
        unitMeasureCB.setItems(fillUnitMeasureComboBox());
        unitMeasureCB.setValue(fillUnitMeasureComboBox().get(0));
        productCB.setValue(allProductListObservable.get(0));
        productCB.setPlaceholder(new Label("Tabela jest pusta."));
        prepareTableColumn();

    }
    public void updateCellFactory2() {
        amountColumn.setCellFactory(column -> {
            return new TableCell<Store, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty); //This is mandatory

                    if (item == null || empty) { //If the cell is empty
                        setText(null);
                        setStyle("");
                    } else { //If the cell is not empty
                        setText(String.valueOf(item));
                        Store store = getTableView().getItems().get(getIndex());
                        if (store.getAmount() <= 0) {
                            setTextFill(Color.RED); //The text in red
                            setStyle("-fx-font-weight:bold");
                        } else {
                            setTextFill(Color.GREEN); //The text in green
                            setStyle("-fx-font-weight:bold");
                        }
                    }
                }
            };
        });
    }
    private void prepareTableColumn(){
        storeProductsTable.getColumns().clear();
        nameColumn  = new TableColumn<Store, String>("Nazwa produktu");
        nameColumn.setMinWidth(200);

        // Create column Email (Data type of String).
        amountColumn   = new TableColumn<Store, Double>("Ilość w mgazynie");
        amountColumn.setMinWidth(150);
        unitMeasure   = new TableColumn<Store, String>("Jednotka miary");
        unitMeasure.setMinWidth(150);

        storeProductsTable.getColumns().addAll(nameColumn,amountColumn,unitMeasure);
        storeProductsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        nameColumn.setCellValueFactory(  new PropertyValueFactory<>("name"));
        amountColumn.setCellValueFactory(  new PropertyValueFactory<>("amount"));
        unitMeasure.setCellValueFactory(  new PropertyValueFactory<>("unitMeasure"));
        storeProductsTable.setPlaceholder(new Label("Tabela jest pusta."));
        storeProductsTable.setItems(getNameAndAmount());
        updateCellFactory2();
    }

    private ObservableList<Store> getNameAndAmount(){
        ObservableList<Store> allProductListObservable = FXCollections.observableArrayList();
        for (Store s: allProductList ) {
            allProductListObservable.add(s);
        }
        return allProductListObservable;
    }
    private ObservableList<String> fillProductCB(){
        allProductList = (List<Store>) storeService.findAll();
        allProductListObservable = FXCollections.observableArrayList();
        allProductListObservable.add("Wszystko");

        for (Store s: allProductList ) {
            allProductListObservable.add(s.getName());
        }
        return allProductListObservable;
    }
    private void message(String message, Alert.AlertType alertType, String typeMessage){
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(typeMessage);
        alert.showAndWait();
    }
    @FXML
    void unitMeasureCBAction(ActionEvent event) {

    }

    @FXML
    void productComboBoxAction(ActionEvent event) {
        ObservableList<ProductRaport> allProductList = FXCollections.observableArrayList();;
        List<ProductRaport> transactionList = transactionService.findTransactionByProduct(productCB.getSelectionModel().getSelectedItem());
        List<ProductRaport> emptyTransactionList = emptyTransaction.findEmptyTransactionByProduct(productCB.getSelectionModel().getSelectedItem());
        List<ProductRaport> allTransactionList = new ArrayList<ProductRaport>();
        allTransactionList.addAll(transactionList);
        allTransactionList.addAll(emptyTransactionList);

        //productCB.setValue(allProductListObservable.get(0));
        if(productCB.getSelectionModel().getSelectedItem() != null) {
            if (productCB.getSelectionModel().getSelectedItem().equals("Wszystko")) {
                prepareTableColumn();
            } else {
                if (!productCB.getSelectionModel().getSelectedItem().equals("Wszystko")) {
                    store = storeService.findByName(productCB.getSelectionModel().getSelectedItem());
                }
                invoiceNumber = new TableColumn<ProductRaport, String>("Faktura");
                nameCustomer = new TableColumn<ProductRaport, String>("Klient");
                nameCustomer.setMinWidth(200);
                amountTransaction = new TableColumn<ProductRaport, Double>("Ilość - transakcja" + " (" + store.getUnitMeasure() + ")");
                // amountTransaction.setMinWidth(200);
                wholeAmount = new TableColumn<ProductRaport, Double>("Ilość w magazynie" + " (" + store.getUnitMeasure() + ")");
                // wholeAmount.setMinWidth(200);

                priceNetto = new TableColumn<ProductRaport, Double>("Cena netto");
                // wholeAmount.setMinWidth(200);
                sellDate = new TableColumn<ProductRaport, String>("Data transakcji");
                // sellDate.setMinWidth(200);
                type = new TableColumn<ProductRaport, String>("Typ transakcji");
                // type.setMinWidth(200);

                storeProductsTable.getColumns().clear();
                storeProductsTable.getColumns().addAll(invoiceNumber, nameCustomer, amountTransaction,
                        type, wholeAmount,priceNetto, sellDate);
                storeProductsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                storeProductsTable.setPlaceholder(new Label("Tabela jest pusta."));
                invoiceNumber.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));
                nameCustomer.setCellValueFactory(new PropertyValueFactory<>("name"));
                amountTransaction.setCellValueFactory(new PropertyValueFactory<>("transactionAmount"));
                type.setCellValueFactory(new PropertyValueFactory<>("type"));
                sellDate.setCellValueFactory(new PropertyValueFactory<>("date"));
                wholeAmount.setCellValueFactory(new PropertyValueFactory<>("wholeAmount"));
                priceNetto.setCellValueFactory(new PropertyValueFactory<>("price"));

                for (ProductRaport productRaport : allTransactionList) {
                    allProductList.add(productRaport);
                }
                storeProductsTable.setItems(allProductList);
                storeProductsTable.getSortOrder().add(sellDate);
                sellDate.setComparator(sellDate.getComparator().reversed());
                updateCellFactory();
            }
        }
    }
    @FXML
    void newProductAction(ActionEvent event){
        Platform.runLater(new Runnable(){
            @Override
            public void run() {

                try {
                    stageManager.showNextScene(FxmlView.NEWPRODUCTDIALOG);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });

    }
    private  ObservableList<String> fillUnitMeasureComboBox(){
        ObservableList<String> fillUnitMeasureList = FXCollections.observableArrayList();
        fillUnitMeasureList.add("tona");
        fillUnitMeasureList.add("szt.");
        return fillUnitMeasureList;
    }
    public void updateCellFactory(){
        type.setCellFactory(column -> {
            return new TableCell<ProductRaport, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty); //This is mandatory

                    if (item == null || empty) { //If the cell is empty
                        setText(null);
                        setStyle("");
                    } else { //If the cell is not empty

                        setText(item); //Put the String data in the cell

                        //We get here all the info of the Person of this row
                        ProductRaport productRaport = getTableView().getItems().get(getIndex());

                        // Style all persons wich type is "sprzedaż"
                        if (productRaport.getType().equals("sprzedaż")) {
                            setTextFill(Color.RED); //The text in red
                            setStyle("-fx-font-weight:bold");
                        } else {
                            setTextFill(Color.GREEN); //The text in green
                            setStyle("-fx-font-weight:bold");
                        }
                    }
                }
            };
        });
//        amountTransaction.setCellFactory(column -> {
//            return new TableCell<ProductRaport, Double>() {
//                @Override
//                protected void updateItem(Double item, boolean empty) {
//                    super.updateItem(item, empty); //This is mandatory
//
//                    if (item == null || empty) { //If the cell is empty
//                        setText(null);
//                        setStyle("");
//                    } else { //If the cell is not empty
//                        ProductRaport productRaport = getTableView().getItems().get(getIndex());
//                        if (productRaport.getConversionKilograms() > 0) {
//                            setText(String.valueOf(productRaport.getConversionKilograms())); //Put the String data in the cell
//                        }
//                    }
//                }
//            };
//        });
//        if (!productCB.getSelectionModel().getSelectedItem().equals("Wszystko")) {
//            store = storeService.findByName(productCB.getSelectionModel().getSelectedItem());
//        }
//        wholeAmount.setCellFactory(column -> {
//            return new TableCell<ProductRaport, Double>() {
//                @Override
//                protected void updateItem(Double item, boolean empty) {
//                    super.updateItem(item, empty); //This is mandatory
//
//                    if (item == null || empty) { //If the cell is empty
//                        setText(null);
//                        setStyle("");
//                    } else { //If the cell is not empty
//                        setText(String.valueOf(item+" "+ store.getUnitMeasure())); //Put the String data in the cell
//
//                    }
//                }
//            };
//        });
        }
}
