package me.silverfern.desktop.controllers;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import me.silverfern.desktop.Main;

/**
 * Created by HAlexTM on 01/03/2018.
 */
public class Home {
    public GridPane box;
    public Label usernameLabel;

    public void initialize() {
        usernameLabel.setText(Main.getPref().get("mc_user_name", "What!?"));
    }

    public void closeApp() {
        Platform.exit();
    }

    public void maximizeApp() {
        Main.getInstance().toggleMax();
    }
}
