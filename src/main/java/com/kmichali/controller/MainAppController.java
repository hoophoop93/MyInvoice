package com.kmichali.controller;

import com.kmichali.config.StageManager;
import com.kmichali.view.FxmlView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.io.UnsupportedEncodingException;

@Controller
public class MainAppController {

    @Lazy
    @Autowired
    private StageManager stageManager;

    @FXML
    void invoiceVatAction(ActionEvent event) throws UnsupportedEncodingException {
        stageManager.switchScene(FxmlView.INVOICEVATSTAGE);

    }

    @FXML
    void invoiceVatRrAction(ActionEvent event) throws UnsupportedEncodingException {
        stageManager.switchScene(FxmlView.INVOICERRSTAGE);

    }

    @FXML
    void storeAction(ActionEvent event) throws UnsupportedEncodingException {
        stageManager.switchScene(FxmlView.STORESTAGE);

    }

}
