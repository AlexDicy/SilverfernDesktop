package me.silverfern.desktop;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Created by HAlexTM on 01/03/2018.
 */
public class ResizeHelper {
    private static Cursor cursorEvent = Cursor.DEFAULT;
    private static double startX = 0;
    private static double startY = 0;
    private static double maxWidth = 1000;
    private static double maxHeight = 1000;

    public static void addResizeListener(Stage stage) {
        maxWidth = stage.getWidth();
        maxHeight = stage.getHeight();
        EventHandler<MouseEvent> listener = event -> {
            EventType<? extends MouseEvent> mouseEventType = event.getEventType();
            Scene scene = stage.getScene();

            double mouseEventX = event.getSceneX();
            double mouseEventY = event.getSceneY();
            double sceneWidth = scene.getWidth();
            double sceneHeight = scene.getHeight();

            if (mouseEventType == MouseEvent.MOUSE_MOVED) {
                if (mouseEventX < 4 && mouseEventY > sceneHeight - 4) {
                    cursorEvent = Cursor.SW_RESIZE;
                } else if (mouseEventX > sceneWidth - 4 && mouseEventY > sceneHeight - 4) {
                    cursorEvent = Cursor.SE_RESIZE;
                } else if (mouseEventX < 4) {
                    cursorEvent = Cursor.W_RESIZE;
                } else if (mouseEventX > sceneWidth - 4) {
                    cursorEvent = Cursor.E_RESIZE;
                } else if (mouseEventY > sceneHeight - 4) {
                    cursorEvent = Cursor.S_RESIZE;
                } else {
                    cursorEvent = Cursor.DEFAULT;
                }
                scene.setCursor(cursorEvent);
            } else if (mouseEventType == MouseEvent.MOUSE_EXITED || mouseEventType == MouseEvent.MOUSE_EXITED_TARGET) {
                scene.setCursor(Cursor.DEFAULT);
            } else if (mouseEventType == MouseEvent.MOUSE_PRESSED) {
                startX = stage.getWidth() - mouseEventX;
                startY = stage.getHeight() - mouseEventY;
            } else if (mouseEventType == MouseEvent.MOUSE_DRAGGED) {
                if (cursorEvent != Cursor.DEFAULT) {
                    if (cursorEvent != Cursor.W_RESIZE && cursorEvent != Cursor.E_RESIZE) {
                        double minHeight = stage.getMinHeight() > 8 ? stage.getMinHeight() : 8;
                        if (cursorEvent == Cursor.NW_RESIZE || cursorEvent == Cursor.N_RESIZE || cursorEvent == Cursor.NE_RESIZE) {
                            if (stage.getHeight() > minHeight || mouseEventY < 0) {
                                stage.setHeight(Math.max(maxHeight, stage.getY() - event.getScreenY() + stage.getHeight()));
                                stage.setY(event.getScreenY());
                            }
                        } else {
                            if (stage.getHeight() > minHeight || mouseEventY + startY - stage.getHeight() > 0) {
                                stage.setHeight(Math.max(maxHeight, mouseEventY + startY));
                            }
                        }
                    }

                    if (cursorEvent != Cursor.N_RESIZE && cursorEvent != Cursor.S_RESIZE) {
                        double minWidth = stage.getMinWidth() > 8 ? stage.getMinWidth() : 8;
                        if (cursorEvent == Cursor.NW_RESIZE || cursorEvent == Cursor.W_RESIZE || cursorEvent == Cursor.SW_RESIZE) {
                            double width = stage.getWidth();
                            if (width > minWidth || mouseEventX < 0) {
                                stage.setWidth(Math.max(maxWidth, stage.getX() - event.getScreenX() + stage.getWidth()));
                                if (width != stage.getWidth()) stage.setX(event.getScreenX());
                            }
                        } else {
                            if (stage.getWidth() > minWidth || mouseEventX + startX - stage.getWidth() > 0) {
                                stage.setWidth(Math.max(maxWidth, mouseEventX + startX));
                            }
                        }
                    }
                }
            }
        };
        stage.getScene().addEventHandler(MouseEvent.MOUSE_MOVED, listener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED, listener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_DRAGGED, listener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_EXITED, listener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, listener);
    }
}