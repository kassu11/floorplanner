package model.history;

import controller.Controller;
import model.shapes.Point;
import model.shapes.Shape;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.GUI;
import view.types.ShapeType;
import view.events.DrawUtilities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LineDrawHistoryTest {

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
    void MultipleUndoToOneLineDraws() {
        DrawUtilities.addShapesFirstPoint(controller, 10, 10);
        DrawUtilities.addShapesLastPoint(controller, 20, 20, ShapeType.LINE);
        controller.getHistoryManager().undo();
        controller.getHistoryManager().undo();
        assertEquals(0, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 0 shapes after undo");
        controller.getHistoryManager().redo();
        assertEquals(1, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 1 shapes");
        controller.getHistoryManager().redo();
        assertEquals(3, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 3 shapes");
    }

    @Test
    void oneUndoToOneLineDraw() {
        DrawUtilities.addShapesFirstPoint(controller, 10, 10);
        DrawUtilities.addShapesLastPoint(controller, 20, 20, ShapeType.LINE);
        controller.getHistoryManager().undo();
        assertEquals(1, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 1 shapes after undo");
        controller.getHistoryManager().redo();
        assertEquals(3, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 3 shapes");
    }

    @Test
    void testLineConnectionsToPointsAfterOneUndo() {
        DrawUtilities.addShapesFirstPoint(controller, -10, -10);
        Shape line = DrawUtilities.addShapesLastPoint(controller, -50, 56, ShapeType.LINE);
        Point pointA = line.getPoints().get(0);
        Point pointB = line.getPoints().get(1);
        controller.getHistoryManager().undo();
        controller.getHistoryManager().redo();
        assertTrue(pointA.getChildren().contains(line), "Point A should be linked to line");
        assertTrue(pointB.getChildren().contains(line), "Point B should be linked to line");
        assertTrue(line.getPoints().contains(pointA), "Line should be linked to point A");
        assertTrue(line.getPoints().contains(pointB), "Line should be linked to point B");
        assertEquals(2, line.getPoints().size(), "Should have 2 points");
        assertEquals(1, pointA.getChildren().size(), "Should have 1 shape");
        assertEquals(1, pointB.getChildren().size(), "Should have 1 shape");
    }

    @Test
    void testLineConnectionsToPointsAfterTwoUndo() {
        DrawUtilities.addShapesFirstPoint(controller, 5, 5);
        Shape line = DrawUtilities.addShapesLastPoint(controller, 10, -23, ShapeType.LINE);
        Point pointA = line.getPoints().get(0);
        Point pointB = line.getPoints().get(1);
        controller.getHistoryManager().undo();
        controller.getHistoryManager().undo();
        controller.getHistoryManager().redo();
        controller.getHistoryManager().redo();
        assertTrue(pointA.getChildren().contains(line), "Point A should be linked to line");
        assertTrue(pointB.getChildren().contains(line), "Point B should be linked to line");
        assertTrue(line.getPoints().contains(pointA), "Line should be linked to point A");
        assertTrue(line.getPoints().contains(pointB), "Line should be linked to point B");
        assertEquals(2, line.getPoints().size(), "Should have 2 points");
        assertEquals(1, pointA.getChildren().size(), "Should have 1 shape");
        assertEquals(1, pointB.getChildren().size(), "Should have 1 shape");
    }

    @Test
    void multipleConnectingLines() {
        DrawUtilities.addShapesFirstPoint(controller, 10, 10);
        DrawUtilities.addShapesLastPoint(controller, 20, 20, ShapeType.LINE);
        DrawUtilities.addShapesFirstPoint(controller, 30, 30);
        Shape line = DrawUtilities.addShapesLastPoint(controller, 40, 40, ShapeType.LINE);
        Point pointA = line.getPoints().get(0);
        DrawUtilities.addShapesFirstPoint(controller, 50, 50);
        DrawUtilities.addShapesLastPoint(controller, 60, 60, ShapeType.LINE);
        assertEquals(9, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 9 shapes");
        controller.getHistoryManager().undo();
        controller.getHistoryManager().undo();
        controller.getHistoryManager().undo();
        assertEquals(4, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 4 shapes after undo");
        controller.setHoveredPoint(pointA);
        DrawUtilities.addShapesLastPoint(controller, 70, 70, ShapeType.LINE);
        controller.getHistoryManager().redo();
        controller.getHistoryManager().redo();
        controller.getHistoryManager().redo();
        assertEquals(5, controller.getShapes(Controller.SingletonType.FINAL).size(), "Should have 5 shapes");
    }
}
