package com.kmichali;

import com.kmichali.config.StageManager;
import com.kmichali.view.FxmlView;
import com.sun.javafx.application.LauncherImpl;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import javafx.scene.Scene;
import org.springframework.context.event.EventListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

@SpringBootApplication
@EnableAutoConfiguration
public class MainApp extends Application {

    private ConfigurableApplicationContext springContext;
    private StageManager stageManager;
    Stage splashStage;

    void loadSplashScreen() {
         splashStage = new Stage();
        try {
            BorderPane splashPane = FXMLLoader.load(getClass().getResource("/fxml/splashScreen.fxml"));
            Scene splashScene = new Scene(splashPane);
            splashStage.setScene(splashScene);
            splashStage.initStyle(StageStyle.UNDECORATED);
            splashStage.getIcons().add(new Image("/images/logo.png"));
            splashStage.show();

            setFadeInOut(splashPane, splashStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setFadeInOut(Parent splashScene, Stage splashStage) {

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(30), splashScene);
        fadeIn.setFromValue(1);
        fadeIn.setToValue(0.4);
        fadeIn.setCycleCount(1);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(30), splashScene);
        fadeOut.setFromValue(0.4);
        fadeOut.setToValue(1);
        fadeOut.setCycleCount(1);
        fadeIn.play();

        fadeIn.setOnFinished((e) -> fadeOut.play());
        fadeOut.setOnFinished((e) -> splashStage.close());
    }
    @Override
    public void init(){
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                loadSplashScreen();
            }
        });
        System.out.println("Inside init() method! Perform necessary initializations here.");
        springContext = springBootApplicationContext();
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        stageManager = springContext.getBean(StageManager.class, primaryStage);
        splashStage.close();
        displayInitialScene();
    }
    @Override
    public void stop() throws Exception {
        System.out.println("Inside stop() method! Destroy resources. Perform Cleanup.");
        springContext.close();
    }
    /**
     * Useful to override this method by sub-classes wishing to change the first
     * Scene to be displayed on startup. Example: Functional tests on main
     * window.
     */
    protected void displayInitialScene() throws UnsupportedEncodingException {
        stageManager.switchScene(FxmlView.PRIMARYSTAGE);
    }
    private ConfigurableApplicationContext springBootApplicationContext() {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(MainApp.class);
        builder.web(false);
        String[] args = getParameters().getRaw().stream().toArray(String[]::new);
        return builder.run(args);
    }
    public static void main(String[] args) {
        launch(MainApp.class,args);
    }
}
