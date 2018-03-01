package me.silverfern.desktop.controllers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import me.silverfern.desktop.Main;
import me.silverfern.desktop.api.API;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by HAlexTM on 28/02/2018.
 */
public class Login implements Initializable {
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;

    private ResourceBundle bundle;

    public void initialize(URL location, ResourceBundle bundle) {
        this.bundle = bundle;
    }

    @FXML
    private void login() {
        Main.getInstance().loadPanel("loading", bundle.getString("login.loading"));
        JsonObject json = new JsonObject();
        JsonObject agent = new JsonObject();

        agent.addProperty("name", "Minecraft");
        agent.addProperty("version", 1);

        json.add("agent", agent);
        json.addProperty("username", email.getText());
        json.addProperty("password", password.getText());
        json.addProperty("clientToken", Main.getPref().get("mc_client_token", ""));

        JsonElement result = API.sendPost("https://authserver.mojang.com/authenticate", json);
        if (result != null) {
            JsonObject obj = result.getAsJsonObject();
            if (obj.has("errorMessage")) {
                Scene scene = Main.getInstance().loadPanel("login", "Login");
                Label label = (Label) scene.lookup("#message");
                label.setText(obj.get("errorMessage").getAsString());
                label.getStyleClass().remove("hidden");
            } else if (obj.has("accessToken")) {
                Main.getPref().put("mc_user_token", obj.get("accessToken").getAsString());
                JsonObject profile = obj.get("selectedProfile").getAsJsonObject();
                Main.getPref().put("mc_user_uuid", profile.get("id").getAsString());
                Main.getPref().put("mc_user_name", profile.get("name").getAsString());

                Main.getInstance().loadHome();
            }
        }
    }
}
