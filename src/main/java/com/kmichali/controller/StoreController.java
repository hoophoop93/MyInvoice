package com.kmichali.controller;

import com.kmichali.model.InvoiceField;
import com.kmichali.model.Store;
import com.kmichali.serviceImpl.StoreServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class StoreController  implements Initializable {

    @FXML
    private ComboBox<String> productCB;

    @FXML
    private TableView<Store> storeProductsTable;
    @FXML
    TableColumn<Store, String> nameColumn;
    @FXML
    TableColumn<Store, String> amountColumn;
    Store store;
    @Autowired
    StoreServiceImpl storeService;
    List<Store> allProductList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productCB.setItems(fillProductCB());

        prepareTableColumn();

        nameColumn.setCellValueFactory(  new PropertyValueFactory<>("name"));
        amountColumn.setCellValueFactory(  new PropertyValueFactory<>("amount"));

        storeProductsTable.setItems(getNameAndAmount());
    }

    private void prepareTableColumn(){
        nameColumn  = new TableColumn<Store, String>("Nazwa produktu");
        nameColumn.setMinWidth(200);

        // Create column Email (Data type of String).
        amountColumn   = new TableColumn<Store, String>("Ilość");
        amountColumn.setMinWidth(150);

        storeProductsTable.getColumns().addAll(nameColumn,amountColumn);
    }
    private ObservableList<Store> getNameAndAmount(){
        ObservableList<Store> allProductListObservable = FXCollections.observableArrayList();;
        for (Store s: allProductList ) {
            allProductListObservable.add(s);
        }
        return allProductListObservable;
    }
    private ObservableList<String> fillProductCB(){
        allProductList = (List<Store>) storeService.findAll();
        ObservableList<String> allProductListObservable = FXCollections.observableArrayList();;
        for (Store s: allProductList ) {
            allProductListObservable.add(s.getName());
        }
        return allProductListObservable;
    }
}
