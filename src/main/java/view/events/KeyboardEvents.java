package view.events;

import controller.Controller;
import javafx.scene.input.KeyEvent;
import view.GUIElements.CustomCanvas;
import view.ModeType;
import view.SettingsSingleton;
import view.ShapeType;

public class KeyboardEvents {
    public interface KeyboardEventHandler {
        void handle(KeyEvent event);
    }

    public static KeyboardEventHandler onKeyPressed(CustomCanvas previewGc, Controller controller) {
        KeyboardEventHandler handleKeyboardShortCuts = (KeyEvent event) -> {
            if(event.isShiftDown()) {
                SettingsSingleton.setCurrentMode(ModeType.SELECT);
            } else {
                SettingsSingleton.setCurrentMode(ModeType.DRAW);
            }
            switch (event.getCode()) {
                case DIGIT1 -> SettingsSingleton.setCurrentShape(ShapeType.LINE);
                case DIGIT2 -> SettingsSingleton.setCurrentShape(ShapeType.RECTANGLE);
                case DIGIT3 -> SettingsSingleton.setCurrentShape(ShapeType.CIRCLE);
                case DIGIT4 -> SettingsSingleton.setCurrentShape(ShapeType.MULTILINE);
                case ESCAPE -> {
                    SettingsSingleton.setLastPoint(null);
                    previewGc.clear();
                }
                case Z -> {
                    if (event.isControlDown()) {
                        controller.getHistoryManager().undo();
                    }
                }
                case Y -> {
                    if (event.isControlDown()) {
                        controller.getHistoryManager().redo();
                    }
                }
                default -> {
                }
            }
        };

        return handleKeyboardShortCuts;
    }

}
