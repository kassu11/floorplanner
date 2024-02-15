package model;

import java.util.ArrayList;
import java.util.List;

public class Rectangle extends AbstractShape{

    public Rectangle(double x, double y, double x1, double y1) {
        super(x, y, x1, y1);
    }

    public void addChild(Shape shape) {
        super.addChild(shape);
    }

}
