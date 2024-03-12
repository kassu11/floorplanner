package view.events;

import controller.Controller;
import javafx.scene.input.KeyEvent;
import model.history.HistoryHandler;
import view.GUIElements.canvas.CustomCanvas;
import view.SettingsSingleton;
import view.types.ShapeType;

public class KeyboardEvents {
    public interface KeyboardEventHandler {
        void handle(KeyEvent event);
    }

    public static KeyboardEventHandler onKeyPressed(CustomCanvas previewGc, CustomCanvas gc, Controller controller) {
        return (KeyEvent event) -> {
            switch (event.getCode()) {
                case DIGIT1 -> controller.setCurrentShape(ShapeType.LINE);
                case DIGIT2 -> controller.setCurrentShape(ShapeType.RECTANGLE);
                case DIGIT3 -> controller.setCurrentShape(ShapeType.CIRCLE);
                case DIGIT4 -> controller.setCurrentShape(ShapeType.MULTILINE);
                case ESCAPE -> {
                    controller.setLastPoint(null);
                    previewGc.clear();
                }
                case CONTROL -> controller.setCtrlDown(true);
                case Z -> handleHistoryShortCuts(event, controller.getHistoryManager()::undo, previewGc, gc, controller);
                case Y -> handleHistoryShortCuts(event, controller.getHistoryManager()::redo, previewGc, gc, controller);
                default -> {
                }
            }
        };
    }

    public static KeyboardEventHandler onKeyReleased(CustomCanvas previewGc, CustomCanvas gc, Controller controller) {
        return (KeyEvent event) -> {
            switch (event.getCode()) {
                case CONTROL -> controller.setCtrlDown(false);
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
            if (controller.getLastPoint() != null) DrawUtilities.renderDrawingPreview(controller, controller.getMouseX(), controller.getMouseY(), previewGc);
        }
    }
}
