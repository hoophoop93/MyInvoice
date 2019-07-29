package com.kmichali.controller;

import com.kmichali.config.StageManager;
import com.kmichali.model.EmptyTransaction;
import com.kmichali.model.Store;
import com.kmichali.serviceImpl.EmptyTransactionImpl;
import com.kmichali.serviceImpl.StoreServiceImpl;
import com.kmichali.view.FxmlView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class NewProductController implements Initializable {

    @FXML
    private ComboBox<String> productComboBox;
    @FXML
    private TextField productName;
    @FXML
    private TextField invoiceNumber;
    @FXML
    private TextField customerName;
    @FXML
    private TextField amount;
    @FXML
    private TextField priceNettoTF;
    @FXML
    private DatePicker transactionDate;
    @FXML
    private Button addToStore;

    @Lazy
    @Autowired
    private StageManager stageManager;
    @Autowired
    StoreServiceImpl storeService;
    @Autowired
    EmptyTransactionImpl emptyTransactionImpl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productComboBox.setItems(fillProductCB());
        setLocalDateForDataPicker();
    }

    private void setLocalDateForDataPicker(){
        setDatePattern(transactionDate);
        transactionDate.setValue(LocalDate.now());
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
    @FXML
    void addToStoreAction(ActionEvent event) {
        Store store = null;
        EmptyTransaction emptyTransaction = new EmptyTransaction();
        if(!productName.isDisable()){
            emptyTransaction.setProductName(productName.getText());
            store = storeService.findByName(productName.getText());
            if(store == null){
                store = new Store();
                store.setName(productName.getText());
                store.setUnitMeasure("tona");
                store.setAmount(Double.parseDouble(amount.getText().replace(",",".")));
                storeService.save(store);
                emptyTransaction.setAmount(store.getAmount());
            }else{
                message("Taki produkt jest już w magazynie. Wybierz produkt z listy rozwijalnej.", Alert.AlertType.NONE, "Informacja");
                return;
            }
        }else{
            emptyTransaction.setProductName(productComboBox.getSelectionModel().getSelectedItem());
            store = storeService.findByName(productComboBox.getSelectionModel().getSelectedItem());
            double calculateTransactionAmount;
            calculateTransactionAmount = round(store.getAmount()+Double.parseDouble(amount.getText().replace(",",".")),2);
            emptyTransaction.setAmount(calculateTransactionAmount);
            double calculateStore = store.getAmount()+Double.parseDouble(amount.getText().replace(",","."));
            calculateStore = round(calculateStore,2);
            store.setAmount(calculateStore);
            storeService.update(store);
        }

        emptyTransaction.setCustomerName(customerName.getText());
        emptyTransaction.setInvoiceNumber(invoiceNumber.getText());
        emptyTransaction.setType("kupno");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate transDateLocal= LocalDate.parse(transactionDate.getEditor().getText(), formatter);
        transactionDate.setValue(transDateLocal);

        String dateTransaction = String.valueOf(transactionDate.getValue());

        emptyTransaction.setDate(dateTransaction);
        emptyTransaction.setPrice(Double.parseDouble(priceNettoTF.getText().replace(",",".")));
        emptyTransaction.setTransactionAmount(Double.parseDouble(amount.getText().replace(",",".")));
        emptyTransactionImpl.save(emptyTransaction);

        Stage stage = (Stage) addToStore.getScene().getWindow();
        stage.close();
        Parent parent  = stageManager.setParent(FxmlView.STORESTAGE);
        stageManager.refreshScene(parent);
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    private ObservableList<String> fillProductCB(){
        List<Store> allProductList = (List<Store>) storeService.findAll();
        ObservableList<String> allProductListObservable = FXCollections.observableArrayList();
        allProductListObservable.add("");

        for (Store s: allProductList ) {
            allProductListObservable.add(s.getName());
        }
        return allProductListObservable;
    }
    @FXML
    public void productComboBoxAction(ActionEvent event)  {
        if(productComboBox.getSelectionModel().getSelectedItem().equals("")){
            productName.setDisable(false);
        }else{
            productName.setDisable(true);
        }
    }
    private void message(String message, Alert.AlertType alertType, String typeMessage){
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(typeMessage);
        alert.showAndWait();
    }

}
