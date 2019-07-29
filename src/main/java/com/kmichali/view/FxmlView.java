package com.kmichali.view;

import java.util.ResourceBundle;

public enum FxmlView {

    PRIMARYSTAGE {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("primaryStage.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/main.fxml";
        }
    },
    INVOICERRSTAGE {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("invoiceVatRR.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/invoiceVatRR.fxml";
        }
    },
    INVOICEVATSTAGE {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("invoiceVat.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/invoiceVat.fxml";
        }
    },
    STORESTAGE {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("store.title");
        }
        @Override
        public String getFxmlFile() {
            return "/fxml/store.fxml";
        }
    },
    NEWPRODUCTDIALOG {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("addProductStore.title");
        }
        @Override
        public String getFxmlFile() {
            return "/fxml/addProductStore.fxml";
        }
    },
    SETTINGSSTAGE {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("settings.title");
        }
        @Override
        public String getFxmlFile() {
            return "/fxml/settings.fxml";
        }
    };

    public abstract String getTitle();
    public abstract String getFxmlFile();

    String getStringFromResourceBundle(String key){
        return ResourceBundle.getBundle("Bundle").getString(key);
    }

}
