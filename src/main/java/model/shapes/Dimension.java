package model.shapes;

import view.GUIElements.canvas.CustomCanvas;


public class Dimension {
    private Shape shape;
    private double distance;
    private double height = 15;

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void addShape(Shape shape) {
        this.shape = shape;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void resize() {
        Point movePoint = shape.getPoints().get(0);
        for(Point point : shape.getPoints()) {
            if(point.isSelected()) {
                movePoint = point;
                break;
            }
        }
        Point oppositePoint = shape.getPoints().get(0) == movePoint ? shape.getPoints().get(1) : shape.getPoints().get(0);

        if (oppositePoint.getX() < movePoint.getX()) {
            movePoint.setX(oppositePoint.getX() + distance);
        } else {
            movePoint.setX(oppositePoint.getX() - distance);
        }
    }

    public void draw(CustomCanvas gc) {
        gc.beginPath();
        Point point1 = shape.getPoints().get(0);
        Point point2 = shape.getPoints().get(1);
        double maxY = Math.max(point1.getY(), point2.getY()) + height;
        double lineWidth = gc.getLineWidth();
        gc.setLineWidth(1);
        gc.moveTo(point1.getX(), point1.getY());
        gc.lineTo(point1.getX(), maxY);
        gc.moveTo(point2.getX(), point2.getY());
        gc.lineTo(point2.getX(), maxY);
        gc.lineTo(point1.getX(), maxY);
        gc.stroke();
        gc.setLineWidth(lineWidth);
    }
}
