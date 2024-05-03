package model.shapes;

import view.GUIElements.canvas.CustomCanvas;
import view.types.ShapeType;

public class Rectangle extends AbstractShape {
    /**
     * All of the four lines of the rectangle
     */
    double line1, line2, line3, line4;
    /**
     * Constructor for the rectangle with 4 points
     * @param pointA point A
     * @param pointB point B
     * @param pointC point C
     * @param pointD point D
     */
    public Rectangle(Point pointA, Point pointB, Point pointC, Point pointD) {
        super(pointA, pointB, pointC, pointD);
        addChild(new Line(pointA, pointD));
        addChild(new Line(pointD, pointB));
        addChild(new Line(pointB, pointC));
        addChild(new Line(pointC, pointA));
        this.getPoints().forEach(point -> point.setParentShape(this));
    }
    /**
     * Calculates the length of the rectangle
     * @return length of the rectangle
     */
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
    /**
     * Calculates the area of the rectangle
     * @return area of the rectangle
     */
    @Override
    public double calculateShapeArea() {
        this.calculateShapeLength();
        double s = (line1 + line2 + line3 + line4) / 2;
        double area = Math.sqrt((s - line1) * (s - line2) * (s - line3) * (s - line4));
        System.out.println("Area of rectangle: " + area);
        return area;
    }
    /**
     * Returns the type of the shape
     */
    @Override
    public ShapeType getType() {
        return null;
    }
    /**
     * Draws the rectangle
     * @param gc custom canvas
     */
    @Override
    public void draw(CustomCanvas gc) {
        for (Shape shape : this.getChildren()) {
            shape.draw(gc);
        }
    }

}
