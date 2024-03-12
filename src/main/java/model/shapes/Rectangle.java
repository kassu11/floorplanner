package model.shapes;

import view.GUIElements.canvas.CustomCanvas;
import view.types.ShapeType;

public class Rectangle extends AbstractShape {

    double line1, line2, line3, line4;

    public Rectangle(Point pointA, Point pointB, Point pointC, Point pointD) {
        super(pointA, pointB, pointC, pointD);
        addChild(new Line(pointA, pointD));
        addChild(new Line(pointD, pointB));
        addChild(new Line(pointB, pointC));
        addChild(new Line(pointC, pointA));
        this.getPoints().forEach(point -> point.setParentShape(this));
    }

    @Override
    public double calculateShapeLength() {
        line1 = this.getChildren().get(0).calculateShapeLength();
        line2 = this.getChildren().get(1).calculateShapeLength();
        line3 = this.getChildren().get(2).calculateShapeLength();
        line4 = this.getChildren().get(3).calculateShapeLength();
        double perimeter = line1 + line2 + line3 + line4;
        System.out.println("Perimeter of rectangle: " + perimeter);
        return perimeter;
    }

    @Override
    public double calculateShapeArea() {
        this.calculateShapeLength();
        double s = (line1 + line2 + line3 + line4) / 2;
        double area = Math.sqrt((s - line1) * (s - line2) * (s - line3) * (s - line4));
        System.out.println("Area of rectangle: " + area);
        return area;
    }

    @Override
    public ShapeType getType() {
        return null;
    }

    public void addChild(Shape shape) {
        super.addChild(shape);
    }

    @Override
    public void draw(CustomCanvas gc) {
        for (Shape shape : this.getChildren()) {
            shape.draw(gc);
        }
    }

}
