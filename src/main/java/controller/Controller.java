package controller;

import model.Line;
import model.Shape;
import view.GUI;
import view.ShapeType;

import java.util.ArrayList;
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
                Shape line1 = new Line(x, y, x1, y);
                Shape line2 = new Line(x1, y, x1, y1);
                Shape line3 = new Line(x1, y1, x, y1);
                Shape line4 = new Line(x, y1, x, y);
                shapes.add(line1);
                shapes.add(line2);
                shapes.add(line3);
                shapes.add(line4);
            }
        }
        System.out.println(shapes.size());
    }


    public static void main(String[] args) {
        GUI.launch(GUI.class);
    }
}
