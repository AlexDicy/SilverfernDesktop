package me.silverfern.desktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Timer;

public class Main extends Application {

    static Timer timer;
    private double xOff = 0;
    private double yOff = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Silverfern");
        Scene scene = new Scene(root, Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        //primaryStage.setAlwaysOnTop(true);
        primaryStage.show();

        // allow window move
        scene.setOnMousePressed(event -> {
            xOff = event.getScreenX() - primaryStage.getX();
            yOff = event.getScreenY() - primaryStage.getY();
        });
        scene.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOff);
            primaryStage.setY(event.getScreenY() - yOff);
        });


        // Temporary "realtime" scene reload
        timer = new Timer();
        /*timer.schedule(new TimerTask() {
            @Override
            public void run() {
                FutureTask<Void> updateUITask = new FutureTask<>(() -> {
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
                        root.getStylesheets().clear();
                        root.getStylesheets().add("/assets/base.css");
                        primaryStage.setScene(new Scene(root, 480, 270, Color.TRANSPARENT));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, null);
                Platform.runLater(updateUITask);
                try {
                    updateUITask.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }, 200, 200);*/
    }


    public static void main(String[] args) {
        launch(args);
    }
}
