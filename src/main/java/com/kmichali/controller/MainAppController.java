package com.kmichali.controller;

import com.kmichali.config.StageManager;
import com.kmichali.model.Seller;
import com.kmichali.model.Settings;
import com.kmichali.model.Store;
import com.kmichali.serviceImpl.SellerServiceImpl;
import com.kmichali.serviceImpl.SettingsServiceImpl;
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

@Controller
public class MainAppController {

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    StoreServiceImpl storeService;
    @Autowired
    SellerServiceImpl sellerService;
    @Autowired
    SettingsServiceImpl settingsService;
    Seller seller;
    Store store;

    @FXML
    void invoiceVatAction(ActionEvent event) throws UnsupportedEncodingException {
        Seller seller = sellerService.find(1);
        Settings settings = settingsService.find(1L);
        if(seller == null || settings == null){
            message("Przed rozpoczęciem dodaj swoje dane i ścieżke do zapisu faktur w ustawieniach!!", Alert.AlertType.NONE,"Informacja");
        }else{
            stageManager.switchScene(FxmlView.INVOICEVATSTAGE);
        }
    }

    @FXML
    void invoiceVatRrAction(ActionEvent event) throws UnsupportedEncodingException {
        Seller seller = sellerService.find(1);
        Settings settings = settingsService.find(1L);
        if(seller == null || settings == null){
            message("Przed rozpoczęciem dodaj swoje dane i ścieżke do zapisu faktur w ustawieniach!!", Alert.AlertType.NONE,"Informacja");
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
