package model;

import view.GUIElements.CustomCanvas;

public class Rectangle extends AbstractShape {

    double line1, line2, line3, line4;

    public Rectangle(Point pointA, Point pointB, Point pointC, Point pointD) {
        super(pointA, pointB, pointC, pointD);
        addChild(new Line(pointA, pointD));
        addChild(new Line(pointD, pointB));
        addChild(new Line(pointB, pointC));
        addChild(new Line(pointC, pointA));
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

    public void addChild(Shape shape) {
        super.addChild(shape);
    }

    @Override
    public void draw(CustomCanvas gc) {
        gc.beginPath();
        gc.moveTo(this.getPoints().get(0).getX(), this.getPoints().get(0).getY());
        gc.lineTo(this.getPoints().get(1).getX(), this.getPoints().get(1).getY());
        gc.lineTo(this.getPoints().get(2).getX(), this.getPoints().get(2).getY());
        gc.lineTo(this.getPoints().get(3).getX(), this.getPoints().get(3).getY());
        gc.lineTo(this.getPoints().get(0).getX(), this.getPoints().get(0).getY());
        System.out.println("Drawing rectangle");
        gc.stroke();
    }

}
