package me.silverfern.desktop.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import me.silverfern.desktop.Main;

import java.util.HashMap;

/**
 * Created by HAlexTM on 28/02/2018.
 */
public class Login {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    @FXML
    private void login() {
        Main.getInstance().loadPanel("loading", "Loggin in");
        HashMap<String, String> data = new HashMap<>();

        /*JsonArray agent = new JsonArray();
        ;
        data.put("agent", ;)
        "agent" => array(
                "name" => "Minecraft",
                "version" => 1 ),
        "username" => $name,
                "password" => $passwd
        API.sendPost("", data);*/
    }
}
