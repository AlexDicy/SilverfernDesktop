package me.silverfern.desktop;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.prefs.Preferences;

public class Main extends Application {
    private static Main instance;

    private double xOff = 0;
    private double yOff = 0;

    private Stage stage;
    private Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        // Fix the AntiAliasing for the font
        System.setProperty("prism.lcdtext", "false");
        // Load fonts
        Font.loadFont(getClass().getResource("/assets/fonts/Roboto-Light.ttf").toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/assets/fonts/Roboto-Regular.ttf").toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/assets/fonts/Roboto-Medium.ttf").toExternalForm(), 12);
        // Load the main scene
        Parent root = FXMLLoader.load(getClass().getResource("/assets/fxml/main.fxml"));
        stage.setTitle("Silverfern");
        Scene scene = new Scene(root, Color.TRANSPARENT);
        instance = this;
        this.stage = stage;
        this.scene = scene;
        stage.setScene(scene);
        stage.sizeToScene();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();

        // allow window move
        scene.setOnMousePressed(event -> {
            xOff = event.getScreenX() - stage.getX();
            yOff = event.getScreenY() - stage.getY();
        });
        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOff);
            stage.setY(event.getScreenY() - yOff);
        });

        new Thread(() -> {
            String mcToken = Preferences.userRoot().get("user_mc_token", null);
            //Preferences.userRoot().put("user_mc_token", "IT'S WORKING");
            if (mcToken == null) {
                Platform.runLater(() -> loadPanel("login", "Login"));
            }
        }).start();
    }

    public void loadPanel(String name) {
        try {
            Pane pane = (Pane) scene.lookup("#panels");
            Parent content = FXMLLoader.load(getClass().getResource("/assets/fxml/" + name + ".fxml"));
            pane.getChildren().clear();
            pane.getChildren().add(content);
            stage.sizeToScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPanel(String name, String title) {
        ((Label) scene.lookup("#title-big")).setText(title);
        loadPanel(name);
    }

    public static Main getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
