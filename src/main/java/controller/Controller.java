package controller;

import model.Line;
import model.Rectangle;
import model.Circle;
import model.Shape;
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
        switch (shapeType) {
            case LINE -> {
                Shape line = new Line(x, y, x1, y1);
                shapes.add(line);
            }
            case RECTANGLE -> {
                Shape rectangle = new Rectangle(x, y, x1, y1);
                shapes.add(rectangle);
            }
            case CIRCLE -> {
                Shape circle = new Circle(x, y, x1, y1);
                shapes.add(circle);
            }
        }
        System.out.println(shapes.size());
    }


    public static void main(String[] args) {
        GUI.launch(GUI.class);
    }
}
