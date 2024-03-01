package controller;

import model.Point;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import view.GUI;
import view.ShapeType;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
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
    void addLineShape() {
        controller.createShape(0, 234, 23, 65, ShapeType.LINE, Controller.SingletonType.FINAL);
        assertEquals(3, controller.getShapeContainer(Controller.SingletonType.FINAL).getShapes().size(),
                "Should have 3 shapes");
    }

    @Test
    void addMultipleLineShapes() {
        controller.createShape(0, 12, 43, 54, ShapeType.LINE, Controller.SingletonType.FINAL);
        controller.createShape(123, 2, 3, 45, ShapeType.LINE, Controller.SingletonType.FINAL);
        assertEquals(6, controller.getShapeContainer(Controller.SingletonType.FINAL).getShapes().size(),
                "Should have 6 shapes");
    }

    @Test
    void linkLineToPoint() {
        Point pointA = controller.createAbsolutePoint(-23, 23, Controller.SingletonType.FINAL);
        controller.createShape(pointA, 3, 3, ShapeType.LINE, Controller.SingletonType.FINAL);
        assertEquals(3, controller.getShapeContainer(Controller.SingletonType.FINAL).getShapes().size(),
                "Should have 3 shapes");
    }

    @Test
    void linkLinesToPoints() {
        Point pointA = controller.createAbsolutePoint(0, 0, Controller.SingletonType.FINAL);
        Point pointB = controller.createAbsolutePoint(5, 3, Controller.SingletonType.FINAL);
        controller.createShape(pointA, pointB, ShapeType.LINE, Controller.SingletonType.FINAL);
        assertEquals(3, controller.getShapeContainer(Controller.SingletonType.FINAL).getShapes().size(),
                "Should have 3 shapes");
    }

    @Test
    void linkMultipleLinesToPoints() {
        Point pointA = controller.createAbsolutePoint(123, -45, Controller.SingletonType.FINAL);
        Point pointB = controller.createAbsolutePoint(5, -324, Controller.SingletonType.FINAL);
        Point pointC = controller.createAbsolutePoint(-23, 54, Controller.SingletonType.FINAL);
        Point pointD = controller.createAbsolutePoint(-677, 23, Controller.SingletonType.FINAL);
        controller.createShape(pointA, pointB, ShapeType.LINE, Controller.SingletonType.FINAL);
        controller.createShape(pointB, pointC, ShapeType.LINE, Controller.SingletonType.FINAL);
        controller.createShape(pointC, pointD, ShapeType.LINE, Controller.SingletonType.FINAL);
        controller.createShape(pointD, pointA, ShapeType.LINE, Controller.SingletonType.FINAL);
        assertEquals(8, controller.getShapeContainer(Controller.SingletonType.FINAL).getShapes().size(),
                "Should have 8 shapes");
    }

    @Test
    void addRectangleShape() {
        controller.createShape(0, 234, 23, 65, ShapeType.RECTANGLE, Controller.SingletonType.FINAL);
        assertEquals(8, controller.getShapeContainer(Controller.SingletonType.FINAL).getShapes().size(),
                "Should have 8 shapes");
    }

    @Test
    void addMultipleRectangleShapes() {
        controller.createShape(0, 12, 43, 54, ShapeType.RECTANGLE, Controller.SingletonType.FINAL);
        controller.createShape(123, 2, 3, 45, ShapeType.RECTANGLE, Controller.SingletonType.FINAL);
        assertEquals(16, controller.getShapeContainer(Controller.SingletonType.FINAL).getShapes().size(),
                "Should have 16 shapes");
    }

    @Test
    void linkRectangleToPoint() {
        Point pointA = controller.createAbsolutePoint(-23, 23, Controller.SingletonType.FINAL);
        controller.createShape(pointA, 3, 3, ShapeType.RECTANGLE, Controller.SingletonType.FINAL);
        assertEquals(8, controller.getShapeContainer(Controller.SingletonType.FINAL).getShapes().size(),
                "Should have 8 shapes");
    }

    @Test
    void linkRectanglesToPoints() {
        Point pointA = controller.createAbsolutePoint(0, 0, Controller.SingletonType.FINAL);
        Point pointB = controller.createAbsolutePoint(5, 3, Controller.SingletonType.FINAL);
        controller.createShape(pointA, pointB, ShapeType.RECTANGLE, Controller.SingletonType.FINAL);
        assertEquals(8, controller.getShapeContainer(Controller.SingletonType.FINAL).getShapes().size(),
                "Should have 8 shapes");
    }

    @Test
    void testcreateShapeWithCoordinates() {
        controller.createShape(0, 234, 23, 65, ShapeType.LINE, Controller.SingletonType.FINAL);
        controller.createShape(0, 12, 43, 54, ShapeType.LINE, Controller.SingletonType.FINAL);
        controller.createShape(0, 12, 43, 54, ShapeType.RECTANGLE, Controller.SingletonType.FINAL);
        controller.createShape(0, 12, 43, 54, ShapeType.RECTANGLE, Controller.SingletonType.FINAL);
        assertEquals(22, controller.getShapeContainer(Controller.SingletonType.FINAL).getShapes().size(),
                "Should have 22 shapes");
    }

    @Test
    void testcreateShapeWithOnePoint() {
        Point pointA = controller.createAbsolutePoint(0, 0, Controller.SingletonType.FINAL);
        controller.createShape(pointA, 23, 20, ShapeType.LINE, Controller.SingletonType.FINAL);
        controller.createShape(pointA, 43, 62, ShapeType.LINE, Controller.SingletonType.FINAL);
        controller.createShape(pointA, -324, 54, ShapeType.RECTANGLE, Controller.SingletonType.FINAL);
        controller.createShape(pointA, -563, 14, ShapeType.RECTANGLE, Controller.SingletonType.FINAL);
        assertEquals(19, controller.getShapeContainer(Controller.SingletonType.FINAL).getShapes().size(),
                "Should have 19 shapes");
    }

    @Test
    void testcreateShapeWithTwoPoints() {
        Point pointA = controller.createAbsolutePoint(0, 0, Controller.SingletonType.FINAL);
        Point pointB = controller.createAbsolutePoint(5, 3, Controller.SingletonType.FINAL);
        controller.createShape(pointA, pointB, ShapeType.LINE, Controller.SingletonType.FINAL);
        controller.createShape(pointA, pointB, ShapeType.LINE, Controller.SingletonType.FINAL);
        controller.createShape(pointA, pointB, ShapeType.RECTANGLE, Controller.SingletonType.FINAL);
        controller.createShape(pointA, pointB, ShapeType.RECTANGLE, Controller.SingletonType.FINAL);
        assertEquals(16, controller.getShapeContainer(Controller.SingletonType.FINAL).getShapes().size(),
                "Should have 16 shapes");
    }

    @Disabled
    @Test
    void drawAllShapes() {
        fail("Not yet implemented");
    }

    @Disabled
    @Test
    void createRelativePoint() {
        fail("Not yet implemented");
    }

    @Disabled
    @Test
    void createAbsolutePoint() {
        fail("Not yet implemented");
    }

    @Disabled
    @Test
    void getCanvasMath() {
        fail("Not yet implemented");
    }

    @Disabled
    @Test
    void main() {
        fail("Not yet implemented");
    }
}