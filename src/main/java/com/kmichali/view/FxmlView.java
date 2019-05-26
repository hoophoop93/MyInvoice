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
            return getStringFromResourceBundle("primaryStage.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/invoiceVatRR.fxml";
        }
    },
    INVOICEVATSTAGE {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("primaryStage.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/invoiceVat.fxml";
        }
    },
    STORESTAGE {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("primaryStage.title");
        }
        @Override
        public String getFxmlFile() {
            return "/fxml/store.fxml";
        }
    };

    public abstract String getTitle();
    public abstract String getFxmlFile();

    String getStringFromResourceBundle(String key){
        return ResourceBundle.getBundle("Bundle").getString(key);
    }

}
