package view.events;

import controller.Controller;
import model.Point;
import model.Shape;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.GUI;
import view.SettingsSingleton;
import view.ShapeType;

import static org.junit.jupiter.api.Assertions.*;

class SelectUtilitiesTest {

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
    void selectLine() {
        DrawUtilities.addShapesFirstPoint(controller, 15, 50);
        Shape line = DrawUtilities.addShapesLastPoint(controller, 10, -23, ShapeType.LINE);
        controller.setHoveredShape(line);
        SelectUtilities.selectHoveredShape(controller, 5, 5);
        assertEquals(3, controller.getShapes(Controller.SingletonType.PREVIEW).size(), "Should have 3 shaped selected");
    }

    @Test
    void selectPoint() {
        DrawUtilities.addShapesFirstPoint(controller, 15, 50);
        Shape line = DrawUtilities.addShapesLastPoint(controller, 10, -23, ShapeType.LINE);
        controller.setHoveredShape(line.getPoints().get(0));
        SelectUtilities.selectHoveredShape(controller, 5, 5);
        assertEquals(2, controller.getShapes(Controller.SingletonType.PREVIEW).size(), "Should have 2 shaped selected");
    }

    @Test
    void selectPointThatLinksToMultipleLines() {
        DrawUtilities.addShapesFirstPoint(controller, 15, 50);
        Shape line = DrawUtilities.addShapesLastPoint(controller, 10, -23, ShapeType.LINE);
        Point point = line.getPoints().get(0);
        controller.setLastPoint(point);
        DrawUtilities.addShapesLastPoint(controller, 435, 96, ShapeType.LINE);

        controller.setHoveredShape(point);
        SelectUtilities.selectHoveredShape(controller, 5, 5);
        assertEquals(3, controller.getShapes(Controller.SingletonType.PREVIEW).size(), "Should have 2 shaped selected");
    }

    @Test
    void selectMultipleShapes() {
        DrawUtilities.addShapesFirstPoint(controller, 15, 50);
        DrawUtilities.addShapesLastPoint(controller, 5, 5, ShapeType.MULTILINE);
        Shape lineA = DrawUtilities.addShapesLastPoint(controller, 10, -23, ShapeType.MULTILINE);
        DrawUtilities.addShapesLastPoint(controller, 34, -67, ShapeType.MULTILINE);
        Shape lineB = DrawUtilities.addShapesLastPoint(controller, 1, 163, ShapeType.MULTILINE);
        DrawUtilities.addShapesLastPoint(controller, 54, -163, ShapeType.MULTILINE);

        controller.setHoveredShape(lineA);
        SelectUtilities.selectHoveredShape(controller, 5, 5);
        controller.setHoveredShape(lineB);
        SelectUtilities.selectHoveredShape(controller, 10, 15);

        assertEquals(9, controller.getShapes(Controller.SingletonType.PREVIEW).size(), "Should have 9 shaped selected");
    }

    @Test
    void selectHoveredShape() {
    }

    @Test
    void testSelectHoveredShape() {
    }

    @Test
    void updateSelectionCoordinates() {
    }

    @Test
    void unselectMissingShape() {
    }

    @Test
    void moveSelectedArea() {
    }

    @Test
    void finalizeSelectedShapes() {
    }

    @Test
    void testFinalizeSelectedShapes() {
    }

    @Test
    void rotateSelectedShape() {
    }

    @Test
    void finalizeSelectedRotation() {
    }
}