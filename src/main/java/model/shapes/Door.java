package model.shapes;

import view.GUIElements.canvas.CustomCanvas;

public class Door extends Line {
    private boolean flipped = true;
    /**
     * Constructor for the line with a list of points
     *
     * @param pointA first point
     * @param pointB end point
     */
    public Door(Point pointA, Point pointB) {
        super(pointA, pointB);
    }

    public void draw(CustomCanvas gc) {
        super.draw(gc);
        Point hingePoint = this.getPoints().getLast();
        Point endPoint = this.getPoints().getFirst();
        gc.beginPath();
        double distance = Math.hypot(hingePoint.getX() - endPoint.getX(), hingePoint.getY() - endPoint.getY());
        double angle = Math.atan2(endPoint.getY() - hingePoint.getY(), endPoint.getX() - hingePoint.getX()) / Math.PI * 180;
        double startAngle = (360 - angle) % 360 + (flipped ? 270 : 0);
//        System.out.println((360 - Math.atan2(endPoint.getY() - hingePoint.getY(), endPoint.getX() - hingePoint.getX()) / Math.PI * 180) % 360);
//        System.out.println(Math.abs((Math.atan2(endPoint.getY() - hingePoint.getY(), endPoint.getX() - hingePoint.getX()) / Math.PI * 180) - 180) % 360);
        gc.arc(hingePoint.getX(), hingePoint.getY(), distance, distance, startAngle, 90);
        gc.stroke();
//        gc.setStroke(Color.BLACK);
//        gc.setLineWidth(2);
//        gc.strokeLine(getPointA().getX(), getPointA().getY(), getPointB().getX(), getPointB().getY());
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }
}
