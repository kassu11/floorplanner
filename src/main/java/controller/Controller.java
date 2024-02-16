package controller;

import model.*;
import view.GUI;
import view.ShapeType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller {

    private GUI gui;

    private final List<Shape> shapes = new ArrayList<>();

    public Controller(GUI gui) {
        this.gui = gui;
    }

    public void addShape(double x, double y, double x1, double y1, ShapeType shapeType) {
        Point pointA = new Point(x, y);
        Point pointB = new Point(x1, y1);

        this.addShape(pointA, pointB, shapeType);
    }

    public void addShape(Point pointA, double x1, double y1, ShapeType shapeType) {
        Point pointB = new Point(x1, y1);
        this.addShape(pointA, pointB, shapeType);
    }

    public void addShape(Point pointA, Point pointB, ShapeType shapeType) {
        // TODO: Refactor this to use a factory
        Shape shape = null;

        switch (shapeType) {
            case LINE -> shape = new Line(pointA, pointB);
            case RECTANGLE -> shape = new Rectangle(pointA, pointB);
            case CIRCLE -> shape = new Circle(pointA, pointB);
        }

        shapes.add(shape);
        System.out.println(shapes.size());
    }


    public static void main(String[] args) {
        GUI.launch(GUI.class);
    }
}
