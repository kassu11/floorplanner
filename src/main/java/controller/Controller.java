package controller;

import model.Line;
import model.Shape;
import view.GUI;

import java.util.ArrayList;
import java.util.List;

public class Controller {


    public enum ShapeType {
        LINE, RECTANGLE, CIRCLE, TRIANGLE
    }

    private GUI gui;

    private List<Shape> shapes = new ArrayList<>();
    public Controller(GUI gui) {
        this.gui = gui;
    }

    public void addLine(double x, double y, double x1, double y1) {
        Shape line = new Line(x, y, x1, y1);
        shapes.add(line);
        System.out.println("Line added");
    }


    public static void main(String[] args) {
        GUI.launch(GUI.class);
    }
}
