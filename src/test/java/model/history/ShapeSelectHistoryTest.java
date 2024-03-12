package model.history;

import controller.Controller;
import model.Point;
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
        controller.setHoveredShape(line);
        SelectUtilities.selectHoveredShape(controller, 5, 5);
        controller.getHistoryManager().undo();
        assertEquals(0, controller.getShapes(Controller.SingletonType.PREVIEW).size(), "Should have 0 shaped selected");
        assertEquals(3, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 3 shaped unselected");
        controller.getHistoryManager().redo();
        assertEquals(3, controller.getShapes(Controller.SingletonType.PREVIEW).size(), "Should have 3 shaped selected");
        assertEquals(0, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 0 shaped unselected");
    }

    @Test
    void clearSelectedShapeHistoryProperly1() {
        DrawUtilities.addShapesFirstPoint(controller, 10, 10);
        Shape line = DrawUtilities.addShapesLastPoint(controller, 20, 20, ShapeType.MULTILINE);
        DrawUtilities.addShapesLastPoint(controller, 10, -23, ShapeType.MULTILINE);
        controller.setHoveredShape(line);
        SelectUtilities.selectHoveredShape(controller, 5, 5);
        assertEquals(4, controller.getShapes(Controller.SingletonType.PREVIEW).size(), "Should have 4 shaped selected");

        for(int i = 0; i < 10; i++) controller.getHistoryManager().undo();
        assertEquals(0, controller.getShapes(Controller.SingletonType.PREVIEW).size(), "Should have 0 shaped selected");

        for(int i = 0; i < 10; i++) controller.getHistoryManager().redo();
        for(int i = 0; i < 10; i++) controller.getHistoryManager().undo();

        assertEquals(0, controller.getShapes(Controller.SingletonType.PREVIEW).size(), "Should have 0 shaped selected");
        assertEquals(0, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 0 shaped unselected");

    }

    @Test
    void clearSelectedShapeHistoryProperly2() {
        DrawUtilities.addShapesFirstPoint(controller, 10, 10);
        Shape line = DrawUtilities.addShapesLastPoint(controller, 20, 20, ShapeType.MULTILINE);
        DrawUtilities.addShapesLastPoint(controller, 10, -23, ShapeType.MULTILINE);
        Shape line2 = DrawUtilities.addShapesLastPoint(controller, 30, -33, ShapeType.MULTILINE);
        controller.setHoveredShape(line);
        SelectUtilities.selectHoveredShape(controller, 5, 5);
        controller.setHoveredShape(line2);
        SelectUtilities.selectHoveredShape(controller, 5, 5);
        assertEquals(7, controller.getShapes(Controller.SingletonType.PREVIEW).size(), "Should have 7 shaped selected");
        assertEquals(0, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 0 shaped unselected");

        for(int i = 0; i < 10; i++) controller.getHistoryManager().undo();

        assertEquals(0, controller.getShapes(Controller.SingletonType.PREVIEW).size(), "Should have 0 shaped selected");
        assertEquals(0, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 0 shaped unselected");

        for(int i = 0; i < 10; i++) controller.getHistoryManager().redo();

        assertEquals(7, controller.getShapes(Controller.SingletonType.PREVIEW).size(), "Should have 7 shaped selected");
        assertEquals(0, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 0 shaped unselected");

        for(int i = 0; i < 10; i++) controller.getHistoryManager().undo();

        assertEquals(0, controller.getShapes(Controller.SingletonType.PREVIEW).size(), "Should have 0 shaped selected");
        assertEquals(0, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 0 shaped unselected");
    }

    @Test
    void testSimpleMerge() {
        DrawUtilities.addShapesFirstPoint(controller, 10, 10);
        Shape line1 = DrawUtilities.addShapesLastPoint(controller, 20, 20, ShapeType.LINE);
        DrawUtilities.addShapesFirstPoint(controller, 50, 50);
        Shape line2 = DrawUtilities.addShapesLastPoint(controller, 30, -33, ShapeType.LINE);
        Point pointA= line1.getPoints().get(0);
        Point pointB = line2.getPoints().get(0);
        controller.setHoveredShape(pointA);
        SelectUtilities.selectHoveredShape(controller, 5, 5);
        controller.setHoveredShape(pointB);
        SelectUtilities.finalizeSelectedShapes(controller, null, 5, 5);

        assertEquals(5, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 5 shaped unselected");
        assertEquals(0, controller.getShapes(Controller.SingletonType.PREVIEW).size(), "Should have 0 shaped selected");

        for(int i = 0; i < 10; i++) controller.getHistoryManager().undo();

        assertEquals(0, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 0 shaped unselected");
        assertEquals(0, controller.getShapes(Controller.SingletonType.PREVIEW).size(), "Should have 0 shaped selected");

        for(int i = 0; i < 10; i++) controller.getHistoryManager().redo();

        assertEquals(5, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 5 shaped unselected");
        assertEquals(0, controller.getShapes(Controller.SingletonType.PREVIEW).size(), "Should have 0 shaped selected");

        for(int i = 0; i < 10; i++) controller.getHistoryManager().undo();

        assertEquals(0, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 0 shaped unselected");
        assertEquals(0, controller.getShapes(Controller.SingletonType.PREVIEW).size(), "Should have 0 shaped selected");
    }

    @Test
    void complexMerge1() {
        DrawUtilities.addShapesFirstPoint(controller, 5, 5);
        Shape lineA = DrawUtilities.addShapesLastPoint(controller, 15, 15, ShapeType.MULTILINE);
        DrawUtilities.addShapesLastPoint(controller, 25, 25, ShapeType.MULTILINE);
        Shape lineB = DrawUtilities.addShapesLastPoint(controller, 35, 35, ShapeType.MULTILINE);
        DrawUtilities.addShapesLastPoint(controller, 45, 45, ShapeType.MULTILINE);

        Point pointA = lineA.getPoints().get(0);
        Point pointB = lineB.getPoints().get(1);

        controller.setHoveredShape(pointA);
        SelectUtilities.selectHoveredShape(controller, 25, 25);
        controller.setHoveredShape(pointB);
        SelectUtilities.finalizeSelectedShapes(controller, null, 5, 5);

        assertEquals(7, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 7 shaped unselected");
        assertEquals(0, controller.getShapes(Controller.SingletonType.PREVIEW).size(), "Should have 0 shaped selected");

        for(Shape shape : controller.getShapes(Controller.SingletonType.FINAL)) {
            System.out.println(shape);
        }

        controller.getHistoryManager().undo();

        assertEquals(3, controller.getShapes(Controller.SingletonType.PREVIEW).size(), "Should have 3 shaped selected");
    }

    @Test
    void complexMerge2() {
        DrawUtilities.addShapesFirstPoint(controller, 5, 5);
        Shape lineA = DrawUtilities.addShapesLastPoint(controller, 15, 15, ShapeType.MULTILINE);
        DrawUtilities.addShapesLastPoint(controller, 25, 25, ShapeType.MULTILINE);
        Shape lineB = DrawUtilities.addShapesLastPoint(controller, 35, 35, ShapeType.MULTILINE);
        DrawUtilities.addShapesLastPoint(controller, 45, 45, ShapeType.MULTILINE);

        Point pointA = lineA.getPoints().get(0);
        Point pointB = lineB.getPoints().get(1);

        controller.setHoveredShape(pointA);
        SelectUtilities.selectHoveredShape(controller, 25, 25);
        controller.setHoveredShape(pointB);
        SelectUtilities.finalizeSelectedShapes(controller, null, 5, 5);

        assertEquals(7, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 7 shaped unselected");
        assertEquals(0, controller.getShapes(Controller.SingletonType.PREVIEW).size(), "Should have 0 shaped selected");

        for(int i = 0; i < 10; i++) controller.getHistoryManager().undo();

        assertEquals(0, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 0 shaped unselected");
        assertEquals(0, controller.getShapes(Controller.SingletonType.PREVIEW).size(), "Should have 0 shaped selected");

        for(int i = 0; i < 10; i++) controller.getHistoryManager().redo();

        assertEquals(7, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 7 shaped unselected");
        assertEquals(0, controller.getShapes(Controller.SingletonType.PREVIEW).size(), "Should have 0 shaped selected");

        for(int i = 0; i < 10; i++) controller.getHistoryManager().undo();

        assertEquals(0, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 0 shaped unselected");
        assertEquals(0, controller.getShapes(Controller.SingletonType.PREVIEW).size(), "Should have 0 shaped selected");
    }

    @Test
    void mergeToOwnPoint() {
        DrawUtilities.addShapesFirstPoint(controller, -5, -5);
        Shape line = DrawUtilities.addShapesLastPoint(controller, 0, 0, ShapeType.LINE);
        Point pointA = line.getPoints().get(0);
        Point pointB = line.getPoints().get(1);

        controller.setHoveredShape(pointA);
        SelectUtilities.selectHoveredShape(controller, 0, 0);
        controller.setHoveredShape(pointB);
        SelectUtilities.finalizeSelectedShapes(controller, null, 0, 0);

        assertEquals(0, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 0 total shapes");
        controller.getHistoryManager().undo();
        assertEquals(2, controller.getShapes(Controller.SingletonType.PREVIEW).size(), "Should have 2 selected shapes");
        assertEquals(1, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 1 unselected shape");

        controller.getHistoryManager().redo();
        assertEquals(0, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 0 total shapes");
    }
}
