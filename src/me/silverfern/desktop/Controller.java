package me.silverfern.desktop;

import javafx.application.Platform;
import javafx.fxml.FXML;

public class Controller {

    @FXML
    private void closeApp() {
        Platform.exit();
    }
}
