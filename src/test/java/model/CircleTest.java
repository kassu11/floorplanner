package model;

import controller.Controller;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import view.GUI;
import view.ShapeType;

import static org.junit.jupiter.api.Assertions.*;

class CircleTest {

    private static Controller controller;


    @BeforeAll
    static void setUp() {
        GUI gui = new GUI();
        controller = new Controller(gui);
    }

    @BeforeEach
    void clearShapes() {
        controller.getShapeContainer(Controller.SingletonType.FINAL).clearShapes();
    }

    @Test
    void calculateShapeLength() {
        Point pointA = controller.createAbsolutePoint(67, 90, Controller.SingletonType.FINAL);
        Point pointB = controller.createAbsolutePoint(65, 87, Controller.SingletonType.FINAL);
        Shape shape = controller.createShape(pointA, pointB, ShapeType.CIRCLE, Controller.SingletonType.FINAL);
        assertEquals(7.9, shape.calculateShapeLength(), 0.1, "Should return 7.9");
    }

    @Test
    void calculateShapeArea() {
        Point pointA = controller.createAbsolutePoint(23, 54, Controller.SingletonType.FINAL);
        Point pointB = controller.createAbsolutePoint(65, 23, Controller.SingletonType.FINAL);
        Shape shape = controller.createShape(pointA, pointB, ShapeType.CIRCLE, Controller.SingletonType.FINAL);
        assertEquals(1022.6, shape.calculateShapeArea(), 0.1 ,"Should be 1022.6 ");
    }

    @Disabled
    @Test
    void draw() {
        fail("Not yet implemented");
    }

    @Test
    void calculateDistanceFromMouse() {
        Point pointA = controller.createAbsolutePoint(20, 50, Controller.SingletonType.FINAL);
        Point pointB = controller.createAbsolutePoint(23, 67, Controller.SingletonType.FINAL);
        Shape shape = controller.createShape(pointA, pointB, ShapeType.CIRCLE, Controller.SingletonType.FINAL);
        assertEquals(62.3, shape.calculateDistanceFromMouse(0, 0), 0.1, "Should return 62.3");
    }
}