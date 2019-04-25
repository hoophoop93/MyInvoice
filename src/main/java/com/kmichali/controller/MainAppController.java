package com.kmichali;

import com.kmichali.service.SellerService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;
@Controller
public class MainAppController implements Initializable {

    @FXML
    private Label textLabel;
    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    SellerService sellerService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        sellerService.findAll().forEach(item -> System.out.println(item.toString()));

    }

}
