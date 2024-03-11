package model.history;

import controller.Controller;
import model.Shape;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.GUI;
import view.SettingsSingleton;
import view.ShapeType;
import view.events.DrawUtilities;
import view.events.SelectUtilities;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShapeSelectHistoryTest {
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
    void testSelectionStart() {
        DrawUtilities.addShapesFirstPoint(controller, 5, 5);
        Shape line = DrawUtilities.addShapesLastPoint(controller, 10, -23, ShapeType.LINE);
        SettingsSingleton.setHoveredShape(line);
        SelectUtilities.selectHoveredShape(controller, 5, 5);
        controller.getHistoryManager().undo();
        assertEquals(0, controller.getShapes(Controller.SingletonType.PREVIEW).size(), "Should have 0 shaped selected");
        assertEquals(3, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 3 shaped unselected");
        controller.getHistoryManager().redo();
        assertEquals(3, controller.getShapes(Controller.SingletonType.PREVIEW).size(), "Should have 3 shaped selected");
        assertEquals(0, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 0 shaped unselected");
    }
}
