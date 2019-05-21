package com.kmichali.controller;

import com.kmichali.config.StageManager;
import com.kmichali.view.FxmlView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class VatRRInvoiceController implements Initializable {

    @Lazy
    @Autowired
    private StageManager stageManager;
    @FXML
    private TextField invoiceNumberTF;
    @FXML
    private TextField peselOrNipTF;
    @FXML
    private TextField idCardTF;
    @FXML
    private TextField seriaIdCardTf;
    @FXML
    private TextField numberIdCardTF;
    @FXML
    private DatePicker releaseDateTF;
    @FXML
    private ComboBox<?> paidType;
    @FXML
    private DatePicker issueDate;
    @FXML
    private DatePicker sellDate;
    @FXML
    private DatePicker paymentDate;
    @FXML
    private TextField releaseByTF;
    @FXML
    private TableView<?> productTable;
    @FXML
    private TableColumn<?, ?> lpColumn;
    @FXML
    private TableColumn<?, ?> productNameColumn;
    @FXML
    private TableColumn<?, ?> classProductColumn;
    @FXML
    private TableColumn<?, ?> unitMeasureColumn;
    @FXML
    private TableColumn<?, ?> amountColumn;
    @FXML
    private TableColumn<?, ?> priceNettoColumn;
    @FXML
    private TableColumn<?, ?> productValueColumn;
    @FXML
    private TableColumn<?, ?> taxColumn;
    @FXML
    private TableColumn<?, ?> priceVatColumn;
    @FXML
    private TableColumn<?, ?> priceBruttoColumn;
    @FXML
    private Button addNewRow;
    @FXML
    private Button removeRow;
    @FXML
    private TextField forPrice;
    @FXML
    private TextField sumNetto;
    @FXML
    private TextField sumVat;
    @FXML
    private ComboBox<?> selectCustomerCB;
    @FXML
    private Label label2;
    @FXML
    private TextField customerNameTF;
    @FXML
    private Label label3;
    @FXML
    private TextField streetTF;
    @FXML
    private Label label4;
    @FXML
    private TextField postalCodeTF;
    @FXML
    private Label label5;
    @FXML
    private TextField addressTF;
    @FXML
    void addNewCustomerAction(ActionEvent event) {

    }

    @FXML
    void addNewRowAction(ActionEvent event) {

    }

    @FXML
    void backButtonAction(ActionEvent event) throws UnsupportedEncodingException {
        stageManager.switchScene(FxmlView.PRIMARYSTAGE);
    }

    @FXML
    void changeAmountCellEvent(ActionEvent event) {

    }

    @FXML
    void changeNameProductCellEvent(ActionEvent event) {

    }

    @FXML
    void changePriceBruttoCellEvent(ActionEvent event) {

    }

    @FXML
    void changePriceNettoCellEvent(ActionEvent event) {

    }

    @FXML
    void changePriceVatCellEvent(ActionEvent event) {

    }

    @FXML
    void changeProductClassCellEvent(ActionEvent event) {

    }

    @FXML
    void changeProductValueCellEvent(ActionEvent event) {

    }

    @FXML
    void createInvoiceAction(ActionEvent event) {

    }

    @FXML
    void invoiceTableAction(MouseEvent event) {

    }

    @FXML
    void removeRowAction(ActionEvent event) {

    }

    @FXML
    void selectCustomerAction(ActionEvent event) {

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
