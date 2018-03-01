package me.silverfern.desktop.controllers;

import javafx.application.Platform;
import me.silverfern.desktop.Main;

/**
 * Created by HAlexTM on 01/03/2018.
 */
public class Home {

    //public AnchorPane box;

    public void closeApp() {
        Platform.exit();
    }

    public void maximizeApp() {
        Main.getInstance().toggleMax();
    }
}
