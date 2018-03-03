package me.silverfern.desktop.controllers;

import javafx.fxml.FXMLLoader;
import me.silverfern.desktop.Main;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by HAlexTM on 01/03/2018.
 */
public class Logout {

    public void logout() {
        Main.getPref().remove("mc_user_token");
        Main.getPref().remove("mc_client_token");
        try {
            Main.getScene().setRoot(FXMLLoader.load(getClass().getResource("/assets/fxml/main.fxml"), ResourceBundle.getBundle("assets.locales.Messages", Locale.ENGLISH)));
            Main.getInstance().loadPanel("login", "Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.getStage().sizeToScene();
    }
}
