package model.shapes;

import view.GUIElements.canvas.CustomCanvas;
import view.types.ShapeType;

public class Door extends AbstractShape{
    public Door(Point pointA, Point pointB) {
        super(pointA, pointB);
    }
    private int startAngle = 0;

    public void draw(CustomCanvas gc) {
        gc.updateCanvasColors(this);
        Point pointA = this.getPoints().get(0);
        Point pointB = this.getPoints().get(1);
        double distanceBetweenPoints = Math.abs(Math.max(pointA.getX(), pointB.getX()) - Math.min(pointA.getX(), pointB.getX()));
        double lengthOfAngle;
        Point pointC = new Point(pointA.getX(), pointA.getY() - distanceBetweenPoints);

        gc.beginPath();
        gc.moveTo(pointA.getX(), pointA.getY());
        gc.lineTo(pointB.getX(), pointB.getY());
        gc.moveTo(pointA.getX(), pointA.getY());
        gc.lineTo(pointC.getX(), pointC.getY());
        if (pointB.getX() > pointA.getX()) {
            this.startAngle = 0;
        } else {
            this.startAngle = 90;
        }
        gc.arc(pointA.getX(), pointA.getY(), distanceBetweenPoints, distanceBetweenPoints, startAngle, 90);

        gc.stroke();
    }

    @Override
    public double calculateShapeLength() {
        int result = 0;
        for (Point point : this.getPoints()) {
            result += point.getX();
        }
        return result;
    }

    @Override
    public double calculateShapeArea() {
        return 0;
    }

    @Override
    public ShapeType getType() {
        return ShapeType.DOOR;
    }
}
