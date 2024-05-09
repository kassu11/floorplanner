package view.events;

import controller.Controller;
import javafx.scene.input.KeyEvent;
import model.history.HistoryHandler;
import model.shapes.Point;
import model.shapes.Shape;
import view.GUIElements.canvas.CustomCanvas;
import view.types.ModeType;
import view.types.ShapeType;
/**
 * Class for handling keyboard events
 */
public class KeyboardEvents {
    /**
     * Interface for handling keyboard events
     */
    public interface KeyboardEventHandler {
        /**
         * Handles the keyboard event
         * @param event keyboard event
         */
        void handle(KeyEvent event);
    }
    /**
     * Handles the key pressed event
     * @param previewGc preview custom canvas
     * @param gc custom canvas
     * @param controller controller
     * @return keyboard event handler
     */

    public static KeyboardEventHandler onKeyPressed(CustomCanvas previewGc, CustomCanvas gc, Controller controller) {
        return (KeyEvent event) -> {
            switch (event.getCode()) {
                case DIGIT1 -> controller.setCurrentShape(ShapeType.LINE);
                case DIGIT2 -> controller.setCurrentShape(ShapeType.RECTANGLE);
                case DIGIT3 -> controller.setCurrentShape(ShapeType.DOOR);
                case DIGIT4 -> controller.setCurrentShape(ShapeType.MULTILINE);
                case ESCAPE -> {
                    controller.setLastPoint(null);
                    previewGc.clear();
                }
                case CONTROL -> controller.setCtrlDown(true);
                case A -> {
                    if(controller.isCtrlDown()) {
                        controller.setCurrentMode(ModeType.SELECT);
                        controller.transferAllShapesTo(Controller.SingletonType.PREVIEW);
                        for(Shape shape : controller.getShapes(Controller.SingletonType.PREVIEW)) {
                            if(shape.getType() == ShapeType.POINT) shape.setSelectedCoordinates(shape.getX() - controller.getMouseX(), shape.getY() - controller.getMouseY());
                        }
                        controller.setSelectedShape(controller.getShapes(Controller.SingletonType.PREVIEW).getFirst());
                        controller.setHoveredShape(controller.getShapes(Controller.SingletonType.PREVIEW).getFirst());
                        controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
                        controller.drawAllShapes(previewGc, Controller.SingletonType.PREVIEW);
                    }
                }
                case R -> {
                    if (controller.getCurrentMode() == ModeType.DRAW && controller.getCurrentShapeType() == ShapeType.DOOR) {
                        controller.setFlipDoors(!controller.isFlipDoors());
                        controller.drawAllShapes(previewGc, Controller.SingletonType.PREVIEW);
                        DrawUtilities.renderDrawingPreview(controller, controller.getMouseX(), controller.getMouseY(), previewGc);
                    }
                }
                case Z -> handleHistoryShortCuts(event, controller.getHistoryManager()::undo, previewGc, gc, controller);
                case Y -> handleHistoryShortCuts(event, controller.getHistoryManager()::redo, previewGc, gc, controller);
                case DELETE -> {
                    if(controller.getSelectedShape() != null && controller.getCurrentMode() == ModeType.AREA) {
                        Point selectedShape = (Point)controller.getSelectedShape();
                        controller.getAreaShapes().remove(selectedShape);
                        controller.getShapes(Controller.SingletonType.FINAL).remove(selectedShape);
                        controller.setSelectedShape(controller.getAreaShapes().isEmpty() ? null : controller.getAreaShapes().getLast());
                        controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
                        previewGc.clear();
                        AreaUtilities.drawArea(controller, controller.getAreaShapes(), previewGc);
                    }
                }
                default -> {
                }
            }
        };
    }
    /**
     * Handles the key released event
     * @param previewGc preview custom canvas
     * @param gc custom canvas
     * @param controller controller
     * @return keyboard event handler
     */
    public static KeyboardEventHandler onKeyReleased(CustomCanvas previewGc, CustomCanvas gc, Controller controller) {
        return (KeyEvent event) -> {
            switch (event.getCode()) {
                case CONTROL -> controller.setCtrlDown(false);
                default -> {
                }
            }
        };
    }
    /**
     * Handles the history shortcuts
     * @param keyEvent key event
     * @param historyEvent history event
     * @param previewGc preview custom canvas
     * @param gc custom canvas
     * @param controller controller
     */
    private static void handleHistoryShortCuts(KeyEvent keyEvent, HistoryHandler historyEvent, CustomCanvas previewGc, CustomCanvas gc, Controller controller) {
        if (keyEvent.isControlDown()) {
            historyEvent.handle();
            controller.drawAllShapes(previewGc, Controller.SingletonType.PREVIEW);
            controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
            if (controller.getLastPoint() != null) DrawUtilities.renderDrawingPreview(controller, controller.getMouseX(), controller.getMouseY(), previewGc);
        }
    }
}
