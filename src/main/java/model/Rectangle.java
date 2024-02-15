package model;

import java.util.ArrayList;
import java.util.List;

public class Rectangle extends AbstractShape{

    public Rectangle(double x, double y, double x1, double y1) {
        super(x, y, x1, y1);
        addChild(new Line(x, y, x1, y));
        addChild(new Line(x1, y, x1, y1));
        addChild(new Line(x1, y1, x, y1));
        addChild(new Line(x, y1, x, y));
    }

    public void addChild(Shape shape) {
        super.addChild(shape);
    }

}
