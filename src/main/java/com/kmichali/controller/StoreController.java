package com.kmichali.controller;

import com.kmichali.config.StageManager;
import com.kmichali.model.Store;
import com.kmichali.model.ProductRaport;
import com.kmichali.serviceImpl.StoreServiceImpl;
import com.kmichali.serviceImpl.TransactionServiceImpl;
import com.kmichali.view.FxmlView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class StoreController  implements Initializable {

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
    TableColumn<Store, String> amountColumn;
    @FXML
    TableColumn<ProductRaport, String> nameCustomer;
    @FXML
    TableColumn<ProductRaport, String> amountTransaction;
    @FXML
    TableColumn<ProductRaport, String> sellDate;
    @FXML
    TableColumn<ProductRaport, String> wholeAmount;
    @FXML
    TableColumn<ProductRaport, String> type;

    Store store;
    List<Store> allProductList;
    ObservableList<String> allProductListObservable;

    @Autowired
    StoreServiceImpl storeService;
    @Autowired
    TransactionServiceImpl transactionService;


    @FXML
    void addNewProductAction(ActionEvent event) {
        store = new Store();
        store.setName(productName.getText());
        store.setAmount(Double.parseDouble(amount.getText()));

        if(!storeService.countProductStore(store.getName())) {
            storeService.save(store);
            allProductList.add(store);
            prepareTableColumn();
        }else{
            message("Taki produkt jest już w magazynie!", Alert.AlertType.NONE,"Informacja");
        }

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productCB.setItems(fillProductCB());

        productCB.setValue(allProductListObservable.get(0));
        prepareTableColumn();

    }

    private void prepareTableColumn(){
        storeProductsTable.getColumns().clear();
        nameColumn  = new TableColumn<Store, String>("Nazwa produktu");
        nameColumn.setMinWidth(200);

        // Create column Email (Data type of String).
        amountColumn   = new TableColumn<Store, String>("Ilość w mgazynie");
        amountColumn.setMinWidth(150);

        storeProductsTable.getColumns().addAll(nameColumn,amountColumn);
        nameColumn.setCellValueFactory(  new PropertyValueFactory<>("name"));
        amountColumn.setCellValueFactory(  new PropertyValueFactory<>("amount"));

        storeProductsTable.setItems(getNameAndAmount());


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
    void backButtonAction(ActionEvent event) throws UnsupportedEncodingException {
        stageManager.switchScene(FxmlView.PRIMARYSTAGE);
    }

    @FXML
    void productComboBoxAction(ActionEvent event) {
        ObservableList<ProductRaport> allProductList = FXCollections.observableArrayList();;
        List<ProductRaport> testList = transactionService.findTransactionByProduct(productCB.getSelectionModel().getSelectedItem());

        if (productCB.getSelectionModel().getSelectedItem().equals("Wszystko")) {
            prepareTableColumn();
        }else {
            nameCustomer = new TableColumn<ProductRaport, String>("Klient");
            nameCustomer.setMinWidth(200);
            amountTransaction = new TableColumn<ProductRaport, String>("Ilość - transakcja");
            amountTransaction.setMinWidth(200);
            wholeAmount = new TableColumn<ProductRaport, String>("Ilość w magazynie");
            wholeAmount.setMinWidth(200);
            sellDate = new TableColumn<ProductRaport, String>("Data sprzedaży");
            sellDate.setMinWidth(200);
            type = new TableColumn<ProductRaport, String>("Typ transakcji");
            type.setMinWidth(200);

            storeProductsTable.getColumns().clear();
            storeProductsTable.getColumns().addAll(nameCustomer, amountTransaction, type, wholeAmount, sellDate);
            nameCustomer.setCellValueFactory(new PropertyValueFactory<>("name"));
            amountTransaction.setCellValueFactory(new PropertyValueFactory<>("storeAmount"));
            type.setCellValueFactory(new PropertyValueFactory<>("type"));
            sellDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            wholeAmount.setCellValueFactory(new PropertyValueFactory<>("wholeAmount"));

            for (ProductRaport productRaport : testList) {
                allProductList.add(productRaport);
            }
            storeProductsTable.setItems(allProductList);
        }
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
    }


}
