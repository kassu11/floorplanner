package view.events;

import controller.Controller;
import javafx.scene.input.KeyEvent;
import model.history.HistoryHandler;
import view.GUIElements.CustomCanvas;
import view.SettingsSingleton;
import view.ShapeType;

public class KeyboardEvents {
    public interface KeyboardEventHandler {
        void handle(KeyEvent event);
    }

    public static KeyboardEventHandler onKeyPressed(CustomCanvas previewGc, CustomCanvas gc, Controller controller) {
        return (KeyEvent event) -> {
            switch (event.getCode()) {
                case DIGIT1 -> SettingsSingleton.setCurrentShape(ShapeType.LINE);
                case DIGIT2 -> SettingsSingleton.setCurrentShape(ShapeType.RECTANGLE);
                case DIGIT3 -> SettingsSingleton.setCurrentShape(ShapeType.CIRCLE);
                case DIGIT4 -> SettingsSingleton.setCurrentShape(ShapeType.MULTILINE);
                case ESCAPE -> {
                    SettingsSingleton.setLastPoint(null);
                    previewGc.clear();
                }
                case Z -> handleHistoryShortCuts(event, controller.getHistoryManager()::undo, previewGc, gc, controller);
                case Y -> handleHistoryShortCuts(event, controller.getHistoryManager()::redo, previewGc, gc, controller);
                default -> {
                }
            }
        };
    }

    private static void handleHistoryShortCuts(KeyEvent keyEvent, HistoryHandler historyEvent, CustomCanvas previewGc, CustomCanvas gc, Controller controller) {
        if (keyEvent.isControlDown()) {
            historyEvent.handle();
            controller.drawAllShapes(previewGc, Controller.SingletonType.PREVIEW);
            controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
            if (SettingsSingleton.getLastPoint() != null) DrawUtilities.renderDrawingPreview(controller, SettingsSingleton.getMouseX(), SettingsSingleton.getMouseY(), previewGc);
        }
    }
}
