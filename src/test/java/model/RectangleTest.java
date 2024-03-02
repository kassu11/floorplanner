package model;

import controller.Controller;
import org.junit.jupiter.api.*;
import view.GUI;
import view.SettingsSingleton;
import view.ShapeType;

import static org.junit.jupiter.api.Assertions.*;

class RectangleTest {

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
    void calculateShapeLength() {
        Point pointA = controller.createAbsolutePoint(20, 50, Controller.SingletonType.FINAL);
        Point pointB = controller.createAbsolutePoint(23, 67, Controller.SingletonType.FINAL);
        Shape shape = controller.createShape(pointA, pointB, ShapeType.RECTANGLE, Controller.SingletonType.FINAL);
        assertEquals(40, shape.calculateShapeLength(), "Should return 80");
    }

    @Test
    void calculateShapeArea() {
        Point pointA = controller.createAbsolutePoint(20, 50, Controller.SingletonType.FINAL);
        Point pointB = controller.createAbsolutePoint(23, 67, Controller.SingletonType.FINAL);
        Shape shape = controller.createShape(pointA, pointB, ShapeType.RECTANGLE, Controller.SingletonType.FINAL);
        assertEquals(51, shape.calculateShapeArea(), "Should be something ? ");
    }

    @Disabled
    @Test
    void addChild() {
        fail("Not yet implemented");
    }
}