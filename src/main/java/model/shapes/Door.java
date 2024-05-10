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
    /**
     * Draws the door and all shapes related to it
     * @param gc custom canvas
     */
    public void draw(CustomCanvas gc) {
        gc.setLineDashes(10, 10);
        super.draw(gc);
        Point hingePoint = this.getPoints().getLast();
        Point endPoint = this.getPoints().getFirst();
        gc.beginPath();
        double deltaY = hingePoint.getY() - endPoint.getY();
        double deltaX = hingePoint.getX() - endPoint.getX();
        double distance = Math.hypot(deltaX, deltaY);
        double angle = Math.atan2(endPoint.getY() - hingePoint.getY(), endPoint.getX() - hingePoint.getX()) / Math.PI * 180;
        double startAngle = (360 - angle) % 360 + (flipped ? 270 : 0);

        gc.arc(hingePoint.getX(), hingePoint.getY(), distance, distance, startAngle, 90);
        double endLineX = flipped ? hingePoint.getX() + (deltaY) : hingePoint.getX() - (deltaY);
        double endLineY = flipped ? hingePoint.getY() - hingePoint.getX() + endPoint.getX() : hingePoint.getY() + deltaX;
        gc.stroke();
        gc.beginPath();
        gc.setLineDashes();
        gc.moveTo(hingePoint.getX(), hingePoint.getY());
        gc.lineTo(endLineX, endLineY);
        gc.stroke();

    }
    /**
     * Returns if the door is flipped
     * @return if the door is flipped
     */
    public boolean isFlipped() {
        return flipped;
    }
    /**
     * Sets if the door is flipped
     * @param flipped if the door is flipped
     */
    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }
}
