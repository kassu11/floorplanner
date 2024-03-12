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
import view.events.SelectUtilities;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShapeDeleteHistoryTest {
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
    void deleteLineShapes() {
        DrawUtilities.addShapesFirstPoint(controller, 0, 0);
        Shape lineA = DrawUtilities.addShapesLastPoint(controller, 8, 100, ShapeType.LINE);
        DrawUtilities.addShapesFirstPoint(controller, -100, 0);
        Shape lineB = DrawUtilities.addShapesLastPoint(controller, 534, 32, ShapeType.LINE);
        DrawUtilities.addShapesFirstPoint(controller, 10, 59);
        Shape lineC = DrawUtilities.addShapesLastPoint(controller, 2, -100, ShapeType.LINE);

        SelectUtilities.deleteShape(controller, lineB);
        controller.getHistoryManager().undo();

        assertEquals(9, controller.getShapeContainer(Controller.SingletonType.FINAL).getShapes().size(), "Should have 9 shapes");
        SelectUtilities.deleteShape(controller, lineB);
        assertEquals(6, controller.getShapeContainer(Controller.SingletonType.FINAL).getShapes().size(), "Should have 6 shapes");
        SelectUtilities.deleteShape(controller, lineA);
        assertEquals(3, controller.getShapeContainer(Controller.SingletonType.FINAL).getShapes().size(), "Should have 3 shapes");
        SelectUtilities.deleteShape(controller, lineC);
        assertEquals(0, controller.getShapeContainer(Controller.SingletonType.FINAL).getShapes().size(), "Should have 0 shapes");

        controller.getHistoryManager().undo();
        assertEquals(3, controller.getShapeContainer(Controller.SingletonType.FINAL).getShapes().size(), "Should have 3 shapes");
        controller.getHistoryManager().undo();
        assertEquals(6, controller.getShapeContainer(Controller.SingletonType.FINAL).getShapes().size(), "Should have 6 shapes");
    }

    @Test
    void deletePoint() {
        DrawUtilities.addShapesFirstPoint(controller, 0, 0);
        Shape line = DrawUtilities.addShapesLastPoint(controller, 8, 100, ShapeType.LINE);
        Point point = line.getPoints().getFirst();

        SelectUtilities.deleteShape(controller, point);

        assertEquals(0, controller.getShapeContainer(Controller.SingletonType.FINAL).getShapes().size(), "Should have 0 shapes");
        controller.getHistoryManager().undo();
        assertEquals(3, controller.getShapeContainer(Controller.SingletonType.FINAL).getShapes().size(), "Should have 3 shapes");
        controller.getHistoryManager().redo();
        assertEquals(0, controller.getShapeContainer(Controller.SingletonType.FINAL).getShapes().size(), "Should have 0 shapes");
    }

    @Test
    void deletePointWhenMultipleLinesConnected() {
        DrawUtilities.addShapesFirstPoint(controller, 0, 0);
        Shape lineA = DrawUtilities.addShapesLastPoint(controller, 50, 50, ShapeType.MULTILINE);
        Shape lineB = DrawUtilities.addShapesLastPoint(controller, 100, 0, ShapeType.MULTILINE);
        Shape lineC = DrawUtilities.addShapesLastPoint(controller, 150, 0, ShapeType.MULTILINE);

        SelectUtilities.deleteShape(controller, lineA.getPoints().getFirst());

        assertEquals(3, controller.getShapeContainer(Controller.SingletonType.FINAL).getShapes().size(), "Should have 3 shapes");
        controller.getHistoryManager().undo();
        assertEquals(7, controller.getShapeContainer(Controller.SingletonType.FINAL).getShapes().size(), "Should have 7 shapes");

        assertEquals(2, lineA.getPoints().size(), "Should have 2 points");
        assertEquals(2, lineB.getPoints().size(), "Should have 2 points");
        assertEquals(2, lineC.getPoints().size(), "Should have 2 points");
        controller.getHistoryManager().redo();
        assertEquals(3, controller.getShapeContainer(Controller.SingletonType.FINAL).getShapes().size(), "Should have 3 shapes");
        assertEquals(0, lineA.getPoints().size(), "Should have 0 points");
        assertEquals(0, lineB.getPoints().size(), "Should have 0 points");
        assertEquals(2, lineC.getPoints().size(), "Should have 2 points");
        controller.getHistoryManager().undo();
        controller.getHistoryManager().undo();
        controller.getHistoryManager().undo();
        controller.getHistoryManager().undo();
        controller.getHistoryManager().undo();
        controller.getHistoryManager().undo();
        assertEquals(0, controller.getShapeContainer(Controller.SingletonType.FINAL).getShapes().size(), "Should have 0 shapes");
    }

    @Test
    void deletePointWhenMultipleLinesConnected2() {
        DrawUtilities.addShapesFirstPoint(controller, 0, 0);
        DrawUtilities.addShapesLastPoint(controller, 50, 50, ShapeType.MULTILINE);
        Shape lineB = DrawUtilities.addShapesLastPoint(controller, 100, 0, ShapeType.MULTILINE);
        DrawUtilities.addShapesLastPoint(controller, 150, 0, ShapeType.MULTILINE);

        SelectUtilities.deleteShape(controller, lineB);

        assertEquals(6, controller.getShapeContainer(Controller.SingletonType.FINAL).getShapes().size(), "Should have 6 shapes");
        controller.getHistoryManager().undo();
        assertEquals(7, controller.getShapeContainer(Controller.SingletonType.FINAL).getShapes().size(), "Should have 7 shapes");
    }
}
