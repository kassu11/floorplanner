package model.history;

import controller.Controller;
import model.Point;
import model.Shape;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.GUI;
import view.ShapeType;
import view.events.DrawUtilities;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
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
    }

    @Test
    void addEvent() {
    }

    @Test
    void reset() {
    }

    @Test
    void undo() {
    }

    @Test
    void redo() {
    }

    @Test
    void addShape() {
    }

    @Test
    void addFirstPoint() {
    }

    @Test
    void selectShape() {
    }

    @Test
    void finalizeSelection() {
    }

    @Test
    void moveArea() {
    }
}