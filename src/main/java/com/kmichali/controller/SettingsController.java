package com.kmichali.controller;

import com.kmichali.config.StageManager;
import com.kmichali.model.Company;
import com.kmichali.model.Seller;
import com.kmichali.model.Settings;
import com.kmichali.serviceImpl.CompanyServiceImpl;
import com.kmichali.serviceImpl.SellerServiceImpl;
import com.kmichali.serviceImpl.SettingsServiceImpl;
import com.kmichali.view.FxmlView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
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

    @FXML
    private TextField pathTF;

    @Lazy
    @Autowired
    private StageManager stageManager;
    @Autowired
    SellerServiceImpl sellerService;
    @Autowired
    CompanyServiceImpl companyService;
    @Autowired
    SettingsServiceImpl settingsService;

    Settings settings;
    Seller seller;
    Company company;
    @FXML
    void menuInvoiceVatAction(ActionEvent event)throws UnsupportedEncodingException {
        stageManager.switchScene(FxmlView.INVOICEVATSTAGE);
    }

    @FXML
    void menuInvoiceVatRRAction(ActionEvent event)throws UnsupportedEncodingException {
        stageManager.switchScene(FxmlView.INVOICERRSTAGE);
    }

    @FXML
    void menuStoreAction(ActionEvent event) throws UnsupportedEncodingException {
        stageManager.switchScene(FxmlView.STORESTAGE);
    }
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

       settings = settingsService.find(1L);
       if(settings != null)
       pathTF.setText(settings.getPath());
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

    @FXML
    void selectPathAction(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);

        if(selectedDirectory == null){
            //No Directory selected
        }else{

            settings.setPath(selectedDirectory.getAbsolutePath());
            settingsService.save(settings);
            pathTF.setText(selectedDirectory.getAbsolutePath());
        }
    }
}
