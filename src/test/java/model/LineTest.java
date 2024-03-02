package model;


import org.junit.jupiter.api.Test;

import controller.Controller;
import org.junit.jupiter.api.*;
import view.GUI;
import view.SettingsSingleton;
import view.ShapeType;

import static org.junit.jupiter.api.Assertions.*;

class LineTest {

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
    void linkLinesToPoints() {
        Point pointA = controller.createAbsolutePoint(0, 0, Controller.SingletonType.FINAL);
        Point pointB = controller.createAbsolutePoint(5, 3, Controller.SingletonType.FINAL);
        controller.createShape(pointA, pointB, ShapeType.LINE, Controller.SingletonType.FINAL);
        assertEquals(3, controller.getShapeContainer(Controller.SingletonType.FINAL).getShapes().size(), "Should have 3 shapes");
    }

    @Test
    void calculateShapeLength() {
        Point pointA = controller.createAbsolutePoint(20, 50, Controller.SingletonType.FINAL);
        Point pointB = controller.createAbsolutePoint(23, 67, Controller.SingletonType.FINAL);
        Shape shape = controller.createShape(pointA, pointB, ShapeType.LINE, Controller.SingletonType.FINAL);
        assertEquals(17.3, shape.calculateShapeLength(), 0.1, "Should return 17.2");
    }

    @Test
    void calculateShapeArea() {
        Point pointA = controller.createAbsolutePoint(20, 50, Controller.SingletonType.FINAL);
        Point pointB = controller.createAbsolutePoint(23, 67, Controller.SingletonType.FINAL);
        Shape shape = controller.createShape(pointA, pointB, ShapeType.LINE, Controller.SingletonType.FINAL);
        assertEquals(0, shape.calculateShapeArea(), "Should return 0");
    }

    @Disabled
    @Test
    void draw() {
    }

    @Test
    void calculateDistanceFromMouse() {
        Point pointA = controller.createAbsolutePoint(20, 50, Controller.SingletonType.FINAL);
        Point pointB = controller.createAbsolutePoint(23, 67, Controller.SingletonType.FINAL);
        Shape shape = controller.createShape(pointA, pointB, ShapeType.LINE, Controller.SingletonType.FINAL);
        assertEquals(1000, shape.calculateDistanceFromMouse(0, 0), "Should return 1000 as default value");
    }
}