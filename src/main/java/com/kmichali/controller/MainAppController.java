package com.kmichali.controller;

import com.kmichali.config.StageManager;
import com.kmichali.model.Store;
import com.kmichali.serviceImpl.StoreServiceImpl;
import com.kmichali.view.FxmlView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class MainAppController {

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    StoreServiceImpl storeService;
    Store store;

    @FXML
    void invoiceVatAction(ActionEvent event) throws UnsupportedEncodingException {

        List<Store> productList = (List<Store>) storeService.findAll();
        if(productList.isEmpty()){
           message("Przed rozpoczęciem musisz dodac produkty do magazynu!!!", Alert.AlertType.INFORMATION,"Informacja");
        }else{
            stageManager.switchScene(FxmlView.INVOICEVATSTAGE);
        }
    }

    @FXML
    void invoiceVatRrAction(ActionEvent event) throws UnsupportedEncodingException {

        List<Store> productList = (List<Store>) storeService.findAll();
        if(productList.isEmpty()){
            message("Przed rozpoczęciem musisz dodac produkty do magazynu!!!", Alert.AlertType.INFORMATION,"Informacja");
        }else{
            stageManager.switchScene(FxmlView.INVOICERRSTAGE);
        }
    }

    @FXML
    public void settingsAction(ActionEvent event) throws UnsupportedEncodingException {
        stageManager.switchScene(FxmlView.SETTINGSSTAGE);
    }

    @FXML
    void storeAction(ActionEvent event) throws UnsupportedEncodingException {
        stageManager.switchScene(FxmlView.STORESTAGE);
    }
    private void message(String message, Alert.AlertType alertType, String typeMessage){
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(typeMessage);
        alert.showAndWait();
    }

}
