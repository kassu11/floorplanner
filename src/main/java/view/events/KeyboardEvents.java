package view.events;

import javafx.scene.input.KeyEvent;
import view.GUIElements.CustomCanvas;
import view.ModeType;
import view.SettingsSingleton;
import view.ShapeType;

public class KeyboardEvents {
    public interface KeyboardEventHandler {
        void handle(KeyEvent event);
    }
    public static KeyboardEventHandler onKeyPressed(CustomCanvas previewGc, EventCallback lastPoint) {
        KeyboardEventHandler handleKeyboardShortCuts = (KeyEvent event) -> {
            SettingsSingleton.setCurrentMode(ModeType.DRAW);
            switch (event.getCode()) {
                case DIGIT1 -> SettingsSingleton.setCurrentShape(ShapeType.LINE);
                case DIGIT2 -> SettingsSingleton.setCurrentShape(ShapeType.RECTANGLE);
                case DIGIT3 -> SettingsSingleton.setCurrentShape(ShapeType.CIRCLE);
                case DIGIT4 -> SettingsSingleton.setCurrentShape(ShapeType.MULTILINE);
                case ESCAPE -> {
                    lastPoint.reset();
                    previewGc.clear();
                }
                default -> {
                }
            }
        };

        return handleKeyboardShortCuts;
    }

}

