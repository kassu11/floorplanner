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
            }
        }
        System.out.println("Line added");
    }


    public static void main(String[] args) {
        GUI.launch(GUI.class);
    }
}
