package model.shapes;

import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import view.GUIElements.canvas.CustomCanvas;
import view.types.ShapeType;

public class Door extends AbstractShape{
    public Door(Point pointA, Point pointB) {
        super(pointA, pointB);
    }

    public void draw(CustomCanvas gc) {
        gc.updateCanvasColors(this);
        Point pointA = this.getPoints().get(0);
        Point pointB = this.getPoints().get(1);
        double distanceBetweenPoints = Math.abs(pointA.getX() - pointB.getX());

        gc.beginPath();
        gc.moveTo(pointA.getX(), pointA.getY());
        gc.lineTo(pointB.getX(), pointB.getY());
        gc.stroke();
        gc.strokeArc(pointA.getX(), pointA.getY() + distanceBetweenPoints, distanceBetweenPoints*2, distanceBetweenPoints , 0, 90, ArcType.OPEN);
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
