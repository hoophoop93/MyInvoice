package com.kmichali;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/scene.fxml"));
        primaryStage.setTitle("RSDS - Projekt 1");
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Moja Faktura");
        primaryStage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }
}
