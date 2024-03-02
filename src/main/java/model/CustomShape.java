package model;

import javafx.scene.canvas.GraphicsContext;
import view.GUIElements.CustomCanvas;

import java.util.ArrayList;
import java.util.List;


public class CustomShape extends AbstractShape{


    private List<Shape> children;
    public CustomShape(List<Shape> shapes, List<Point> points) {
        super(shapes, points);
        System.out.println("Customshape added");
        for (Shape shape : shapes) {
            shape.addChild(this);
            System.out.println(shape);
        }
        for (Point point: points){
            System.out.println(point);
        }
    }

    @Override
    public void draw(CustomCanvas gc) {

    }

    @Override
    public double calculateShapeLength() {
        return 0;
    }

    @Override
    public double calculateShapeArea() {
        return 0;
    }


}
