package com.kmichali;

import com.kmichali.config.StageManager;
import com.kmichali.view.FxmlView;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.*;
import javafx.concurrent.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.util.Duration;

import java.io.UnsupportedEncodingException;

@SpringBootApplication
@EnableAutoConfiguration
public class MainApp extends Application {

    public static final String SPLASH_IMAGE =
            "http://fxexperience.com/wp-content/uploads/2010/06/logo.png";

    private ConfigurableApplicationContext springContext;
    private StageManager stageManager;

    @Override
    public void init()throws Exception{
        ImageView splash = new ImageView(new Image(
                SPLASH_IMAGE
        ));
        springContext = springBootApplicationContext();
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        stageManager = springContext.getBean(StageManager.class, primaryStage);
        displayInitialScene();
    }
    @Override
    public void stop() throws Exception {
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
        String[] args = getParameters().getRaw().stream().toArray(String[]::new);
        return builder.run(args);
    }
    public static void main(String[] args) {
        launch(MainApp.class,args);
    }
}
