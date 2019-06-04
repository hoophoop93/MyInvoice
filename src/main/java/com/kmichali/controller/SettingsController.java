package com.kmichali.controller;

import com.kmichali.model.Company;
import com.kmichali.model.Seller;
import com.kmichali.serviceImpl.CompanyServiceImpl;
import com.kmichali.serviceImpl.SellerServiceImpl;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class SettingsController implements Initializable {

    @FXML
    private TextField nameTF;

    @FXML
    private TextField surnameTF;

    @FXML
    private TextField streetTF;

    @FXML
    private TextField postalCodeTF;

    @FXML
    private TextField cityTF;

    @FXML
    private TextArea companyNameTA;

    @FXML
    private TextField nipTF;

    @FXML
    private TextField regonTF;

    @FXML
    private TextField phoneNumberTF;

    @FXML
    private TextField accountNumberTF;

    @Autowired
    SellerServiceImpl sellerService;
    @Autowired
    CompanyServiceImpl companyService;
    Seller seller;
    Company company;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        seller = sellerService.find(1);
        company = companyService.findBySeller(seller);

       nameTF.setText(company.getSeller().getName());
       surnameTF.setText(company.getSeller().getSurname());
       streetTF.setText(company.getSeller().getAddress());
       postalCodeTF.setText(company.getSeller().getPostalCode());
       cityTF.setText(company.getSeller().getCity());

       companyNameTA.setText(company.getName());
       nipTF.setText(company.getNip());
       regonTF.setText(company.getRegon());
       phoneNumberTF.setText(company.getPhoneNumber());
       accountNumberTF.setText(company.getAccountNumber());
    }
    @FXML
    void editAction(ActionEvent event) {
        if(nameTF.isDisable()){
            nameTF.setDisable(false);
            surnameTF.setDisable(false);
            streetTF.setDisable(false);
            postalCodeTF.setDisable(false);
            cityTF.setDisable(false);
            companyNameTA.setDisable(false);
            nipTF.setDisable(false);
            regonTF.setDisable(false);
            phoneNumberTF.setDisable(false);
            accountNumberTF.setDisable(false);

        }else{
            nameTF.setDisable(true);
            surnameTF.setDisable(true);
            streetTF.setDisable(true);
            postalCodeTF.setDisable(true);
            cityTF.setDisable(true);
            companyNameTA.setDisable(true);
            nipTF.setDisable(true);
            regonTF.setDisable(true);
            phoneNumberTF.setDisable(true);
            accountNumberTF.setDisable(true);
        }

    }

    @FXML
    void updateAction(ActionEvent event) {

        seller.setName(nameTF.getText());
        seller.setSurname(surnameTF.getText());
        seller.setAddress(streetTF.getText());
        seller.setPostalCode(postalCodeTF.getText());
        seller.setCity(cityTF.getText());
        sellerService.update(seller);

        company.setName(companyNameTA.getText());
        company.setNip(nipTF.getText());
        company.setRegon(regonTF.getText());
        company.setPhoneNumber(phoneNumberTF.getText());
        company.setAccountNumber(accountNumberTF.getText());
        companyService.update(company);
    }
}
