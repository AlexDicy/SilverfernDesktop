package me.silverfern.desktop;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.UUID;
import java.util.prefs.Preferences;

public class Main extends Application {
    private static Main instance;
    private static Preferences pref;

    private static Stage stage;
    private static Scene scene;

    private double xOff = 0;
    private double yOff = 0;

    @Override
    public void start(Stage stage) throws Exception {
        pref = Preferences.userRoot();
        // Fix the AntiAliasing for the font
        System.setProperty("prism.lcdtext", "false");
        // Load fonts
        Font.loadFont(getClass().getResource("/assets/fonts/Roboto-Light.ttf").toExternalForm(), 14);
        Font.loadFont(getClass().getResource("/assets/fonts/Roboto-Regular.ttf").toExternalForm(), 14);
        Font.loadFont(getClass().getResource("/assets/fonts/Roboto-Medium.ttf").toExternalForm(), 14);
        // Load the main scene
        Parent root = FXMLLoader.load(getClass().getResource("/assets/fxml/main.fxml"));
        stage.setTitle("Silverfern");
        Scene scene = new Scene(root, Color.TRANSPARENT);

        Main.instance = this;
        Main.stage = stage;
        Main.scene = scene;

        stage.setScene(scene);
        stage.sizeToScene();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();

        // allow window move
        barMove();

        new Thread(() -> {
            if (pref.get("mc_user_token", null) != null) {
                Platform.runLater(this::loadHome);
            } else {
                pref.put("mc_client_token", UUID.randomUUID().toString().replace("-", ""));
                Platform.runLater(() -> loadPanel("login", "Login"));
            }
        }).start();
    }

    public Scene loadPanel(String name) {
        try {
            Pane pane = (Pane) scene.lookup("#panels");
            Parent content = FXMLLoader.load(getClass().getResource("/assets/fxml/" + name + ".fxml"));
            pane.getChildren().clear();
            pane.getChildren().add(content);
            stage.sizeToScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scene;
    }

    public Scene loadPanel(String name, String title) {
        ((Label) scene.lookup("#title-big")).setText(title);
        return loadPanel(name);
    }

    public Scene loadHome() {
        try {
            scene.setRoot(FXMLLoader.load(getClass().getResource("/assets/fxml/home.fxml")));
            barMove();
            stage.sizeToScene();

            stage.maximizedProperty().addListener((obj, oldValue, maximixed) -> pref.putBoolean("window_maximized", maximixed));
            stage.xProperty().addListener((observable, oldValue, x) -> pref.putDouble("window_x", x.doubleValue()));
            stage.yProperty().addListener((observable, oldValue, y) -> pref.putDouble("window_y", y.doubleValue()));
            stage.widthProperty().addListener((observable, oldValue, width) -> pref.putDouble("window_width", width.doubleValue()));
            stage.heightProperty().addListener((observable, oldValue, height) -> pref.putDouble("window_height", height.doubleValue()));

            if (pref.getDouble("window_x", -1) > -1) {
                double x = pref.getDouble("window_x", 0);
                double y = pref.getDouble("window_y", -1);
                double width = pref.getDouble("window_width", -1);
                double height = pref.getDouble("window_height", -1);
                boolean out = true;
                for (Screen screen : Screen.getScreens()) {
                    Rectangle2D bounds = screen.getVisualBounds();
                    if (bounds.getMinX() < x && x + 200 < bounds.getMaxX() &&
                            bounds.getMinY() < y && y + 200 < bounds.getMaxY()) {
                        out = false;
                        break;
                    }
                }
                if (out) return scene;

                stage.setX(x);
                if (y > -1) stage.setY(y);
                if (width > scene.getWidth()) stage.setWidth(width);
                if (height > scene.getHeight()) stage.setHeight(height);
            }
            if (pref.getBoolean("window_maximized", false)) {
                stage.setMaximized(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scene;
    }

    private void barMove() {
        Node bar = scene.lookup("#bar");
        bar.setOnMousePressed(event -> {
            xOff = event.getScreenX() - stage.getX();
            yOff = event.getScreenY() - stage.getY();
        });
        bar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOff);
            stage.setY(event.getScreenY() - yOff);
        });
        bar.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                toggleMax();
            }
        });
    }

    public void toggleMax() {
        if (stage.isMaximized()) {
            stage.setMaximized(false);
        } else {
            stage.setMaximized(true);
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setHeight(primaryScreenBounds.getHeight());
        }
    }

    public static Main getInstance() {
        return instance;
    }

    public static Preferences getPref() {
        return pref;
    }

    public static Stage getStage() {
        return stage;
    }

    public static Scene getScene() {
        return scene;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
