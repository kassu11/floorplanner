package controller;

import model.*;
import view.GUI;
import view.ShapeType;


public class Controller {
    private GUI gui;

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

    public Point createPoint(double x, double y) {
        return new Point(x, y);
    }

    public static void main(String[] args) {
        GUI.launch(GUI.class);
    }
}
