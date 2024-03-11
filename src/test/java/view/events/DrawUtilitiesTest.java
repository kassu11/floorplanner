package view.events;

import controller.Controller;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.GUI;
import view.ShapeType;

import static org.junit.jupiter.api.Assertions.*;

class DrawUtilitiesTest {

    private static Controller controller;

    @BeforeEach
    void clearShapes() {
        controller.getShapeContainer(Controller.SingletonType.FINAL).clearShapes();
    }

    @BeforeAll
    static void setUp() {
        GUI gui = new GUI();
        controller = new Controller(gui);
    }

    @Test
    void addShapesFirstPoint() {
        DrawUtilities.addShapesFirstPoint(controller, 10, 10);
        assertEquals(1, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 1 shapes");
    }

    @Test
    void addLineToLastPoint() {
        DrawUtilities.addShapesFirstPoint(controller, 5345, 678);
        DrawUtilities.addShapesLastPoint(controller, 123, 567, ShapeType.LINE);
        assertEquals(3, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 3 shapes");
    }

    @Test
    void renderDrawingPreview() {
    }
}