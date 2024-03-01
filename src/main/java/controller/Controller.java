package controller;

import model.*;
import view.GUI;
import view.ShapeType;

import java.util.ArrayList;
import java.util.List;


public class Controller {
    private GUI gui;

    private List<Shape> customShapes = new ArrayList<>();

    public Controller(GUI gui) {
        this.gui = gui;
    }

    public Shape addShape(double x, double y, double x1, double y1, ShapeType shapeType) {
        Point pointA = new Point(x, y);
        Point pointB = new Point(x1, y1);

        return this.addShape(pointA, pointB, shapeType);
    }

    public Shape addShape(Point pointA, double x1, double y1, ShapeType shapeType) {
        Point pointB = new Point(x1, y1);
        return this.addShape(pointA, pointB, shapeType);
    }

    public Shape addShape(Point pointA, Point pointB, ShapeType shapeType) {
        // TODO: Refactor this to use a factory

        return switch (shapeType) {
            case LINE, MULTILINE -> new Line(pointA, pointB);
            case RECTANGLE -> {
                Point pointC = new Point(pointB.getX(), pointA.getY());
                Point pointD = new Point(pointA.getX(), pointB.getY());
                yield new Rectangle(pointA, pointB, pointC, pointD);
            }
            case CIRCLE -> new Circle(pointA, pointB);
            case TRIANGLE -> null;
        };
    }
    public void addCustomShape(Shape newShape) {
        customShapes.add(newShape);
        System.out.println("Added line to custom shapes!");
        System.out.println("X: " + newShape.getPoints().get(0).getX() + " Y: " + newShape.getPoints().get(0).getY());
        System.out.println("X: " + newShape.getPoints().get(1).getX() + " Y: " + newShape.getPoints().get(1).getY());
    }

    public void checkIfConnected(Shape newShape) {
        if (customShapes.isEmpty()) {
            System.out.println("Custom shapes is empty");
            return;
        }
        Point customPointA = newShape.getPoints().get(0);
        Point customPointB = newShape.getPoints().get(1);
        for (Shape shape : customShapes) {
            Point pointA = shape.getPoints().get(0);
            Point pointB = shape.getPoints().get(1);
            if (pointA.equals(customPointB)) {
                new CustomShape(customShapes);
                customShapes.clear();
                return;
            }
        }
    }

    public Point createPoint(double x, double y) {
        return new Point(x, y);
    }

    public static void main(String[] args) {
        GUI.launch(GUI.class);
    }





}
