package model.history;

import controller.Controller;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.GUI;
import view.types.ShapeType;
import view.events.DrawUtilities;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MultilineDrawHistoryTest {

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
    void longMultilineAndMultipleUndo() {
        DrawUtilities.addShapesFirstPoint(controller, -10, -10);
        DrawUtilities.addShapesLastPoint(controller, -20, -20, ShapeType.MULTILINE);
        DrawUtilities.addShapesLastPoint(controller, -30, -30, ShapeType.MULTILINE);
        DrawUtilities.addShapesLastPoint(controller, -40, -40, ShapeType.MULTILINE);

        assertEquals(7, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 7 shapes at the beginning");
        for(int i = 0; i < 10; i++) controller.getHistoryManager().undo();
        assertEquals(0, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 0 shapes after undo");
        for(int i = 0; i < 10; i++) controller.getHistoryManager().redo();
        assertEquals(7, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 7 shapes");
        for(int i = 0; i < 10; i++) controller.getHistoryManager().undo();
        assertEquals(0, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 0 shapes after undo");
    }
}
