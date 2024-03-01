package controller;

import model.Point;
import model.ShapesSingleton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import view.GUI;
import view.ShapeType;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    private Controller controller;

    @BeforeEach
    void clearShapes() {
        ShapesSingleton.clearShapes();
    }

    @BeforeEach
    void setUp() {
        GUI gui = new GUI();
        controller = new Controller(gui);
    }

    @Test
    void addLineShape() {
        controller.addShape(0, 234, 23, 65, ShapeType.LINE);
        assertEquals(3, ShapesSingleton.getShapes().size(), "Should have 3 shapes");
    }

    @Test
    void addMultipleLineShapes() {
        controller.addShape(0, 12, 43, 54, ShapeType.LINE);
        controller.addShape(123, 2, 3, 45, ShapeType.LINE);
        assertEquals(6, ShapesSingleton.getShapes().size(), "Should have 6 shapes");
    }

    @Test
    void linkLineToPoint() {
        Point pointA = controller.createAbsolutePoint(-23, 23);
        controller.addShape(pointA, 3, 3, ShapeType.LINE);
        assertEquals(3, ShapesSingleton.getShapes().size(), "Should have 3 shapes");
    }

    @Test
    void linkLinesToPoints() {
        Point pointA = controller.createAbsolutePoint(0, 0);
        Point pointB = controller.createAbsolutePoint(5, 3);
        controller.addShape(pointA, pointB, ShapeType.LINE);
        assertEquals(3, ShapesSingleton.getShapes().size(), "Should have 3 shapes");
    }

    @Test
    void linkMultipleLinesToPoints() {
        Point pointA = controller.createAbsolutePoint(123, -45);
        Point pointB = controller.createAbsolutePoint(5, -324);
        Point pointC = controller.createAbsolutePoint(-23, 54);
        Point pointD = controller.createAbsolutePoint(-677, 23);
        controller.addShape(pointA, pointB, ShapeType.LINE);
        controller.addShape(pointB, pointC, ShapeType.LINE);
        controller.addShape(pointC, pointD, ShapeType.LINE);
        controller.addShape(pointD, pointA, ShapeType.LINE);
        assertEquals(8, ShapesSingleton.getShapes().size(), "Should have 8 shapes");
    }

    @Disabled
    @Test
    void testAddShape() {
        fail("Not yet implemented");
    }

    @Disabled
    @Test
    void testAddShape1() {
        fail("Not yet implemented");
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