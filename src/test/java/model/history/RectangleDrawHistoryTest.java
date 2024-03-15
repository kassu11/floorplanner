package model.history;

import controller.Controller;
import model.shapes.Shape;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.GUI;
import view.types.ShapeType;
import view.events.DrawUtilities;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RectangleDrawHistoryTest {

    private static Controller controller;

    @BeforeEach
    void clearShapes() {
        controller.getShapeContainer(Controller.SingletonType.FINAL).clearShapes();
        controller.getShapeContainer(Controller.SingletonType.PREVIEW).clearShapes();
        controller.getHistoryManager().reset();
    }

    @BeforeAll
    static void setUp() {
        GUI gui = new GUI();
        controller = new Controller(gui);
    }

    @Test
    void goesToEditModeAfterOneUndo() {
        DrawUtilities.addShapesFirstPoint(controller, 0, 0);
        DrawUtilities.addShapesLastPoint(controller, -20, -20, ShapeType.RECTANGLE);

        controller.getHistoryManager().undo();
        assertEquals(1, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 1 shapes after undo");
        controller.getHistoryManager().redo();
        assertEquals(8, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 8 shape");
    }

    @Test
    void shapeCountAfterMultipleUndoAndDraws() {
        DrawUtilities.addShapesFirstPoint(controller, 123, 324);
        DrawUtilities.addShapesLastPoint(controller, -325, 12, ShapeType.RECTANGLE);
        controller.getHistoryManager().undo();
        DrawUtilities.addShapesLastPoint(controller, 7567, 2323, ShapeType.RECTANGLE);
        controller.getHistoryManager().undo();
        DrawUtilities.addShapesLastPoint(controller, 5, 5, ShapeType.RECTANGLE);
        assertEquals(8, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 8 shape");
        controller.getHistoryManager().undo();
        assertEquals(1, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 1 shapes after undo");
        controller.getHistoryManager().undo();
        assertEquals(0, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 0 shapes after undo");
        controller.getHistoryManager().redo();
        controller.getHistoryManager().redo();
        assertEquals(8, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 8 shape");
    }

    @Test
    void shapeChildCountAfterMultipleUndoAndDraws() {
        DrawUtilities.addShapesFirstPoint(controller, 123, 324);
        DrawUtilities.addShapesLastPoint(controller, -325, 12, ShapeType.RECTANGLE);
        controller.getHistoryManager().undo();
        DrawUtilities.addShapesLastPoint(controller, 7567, 2323, ShapeType.RECTANGLE);
        controller.getHistoryManager().undo();
        DrawUtilities.addShapesLastPoint(controller, 5, 5, ShapeType.RECTANGLE);
        for(Shape shape : controller.getShapes(Controller.SingletonType.FINAL)) {
            if(shape.getType() == ShapeType.LINE) {
                assertEquals(2, shape.getPoints().size(), "Should have 2 points");
            } else {
                assertEquals(2, shape.getChildren().size(), "Should have 2 line children");
            }
        }
    }
}
