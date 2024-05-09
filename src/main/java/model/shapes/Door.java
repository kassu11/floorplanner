package model.shapes;

import view.GUIElements.canvas.CustomCanvas;
import view.types.ShapeDataType;
import view.types.ShapeType;

public class Door extends Line{
    private Point arcPoint;
    private CustomArc arc;
    private Line line;
    private int startAngle = 0;
    private double distanceBetweenPoints;
    public Door(Point pointA, Point pointB) {
        super(pointA, pointB);
        this.distanceBetweenPoints = Math.abs(Math.max(pointA.getX(), pointB.getX()) - Math.min(pointA.getX(), pointB.getX()));
        this.arcPoint = new Point(pointA.getX(), pointA.getY() - distanceBetweenPoints);
        this.arc = new CustomArc(pointA, arcPoint, distanceBetweenPoints, startAngle, 90);
        this.line = new Line(pointA, arcPoint);
    }

    public void draw(CustomCanvas gc) {
        super.draw(gc);
        Point pointA = this.getPoints().get(0);
        Point pointB = this.getPoints().get(1);
        double slopeA = calculateSlope(pointA, pointB);
        double slopeB = calculateSlope(pointA, arcPoint);
        double lengthOfAngle = Math.toDegrees(Math.atan((Math.abs(slopeA - slopeB) / (1 + slopeA * slopeB))));
        if(pointB.getX() < pointA.getX() ){
            startAngle = 0;
        }
        if(pointB.getY() < pointA.getY()){
            startAngle = 90;
        }
        arc.setAngleLength((int) lengthOfAngle);
        arc.setHeight(distanceBetweenPoints);
        arc.setRadiusX(distanceBetweenPoints);
        arc.setRadiusY(distanceBetweenPoints);
        arc.setStartAngle(startAngle);

        line.draw(gc);
        arc.draw(gc);
    }

    @Override
    public double calculateShapeLength() {
        return distanceBetweenPoints;
    }

    @Override
    public double calculateShapeArea() {
        return 0;
    }

    @Override
    public ShapeType getType() {
        return ShapeType.DOOR;
    }

    public Point getArcPoint(){
        return arcPoint;
    }

    public CustomArc getArc(){
        return arc;
    }
    public Line getLine(){
        return line;
    }
    public double calculateSlope(Point pointA, Point pointB){
        if(pointB.getX() - pointA.getX() != 0){
            return (pointB.getY() - pointA.getY()) / (pointB.getX() - pointA.getX());
        }
        return Integer.MAX_VALUE;
    }
}
