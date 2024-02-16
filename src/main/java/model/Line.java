package model;

import javafx.scene.canvas.GraphicsContext;

public class Line extends AbstractShape{


    public Line(Point pointA, Point pointB) {
        super(pointA, pointB);
        ShapesSingleton.addShape(this);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.beginPath();
        gc.moveTo(this.getPoints().get(0).getX(), this.getPoints().get(0).getY());
        gc.lineTo(this.getPoints().get(1).getX(), this.getPoints().get(1).getY());
        gc.stroke();
    }
}
