package model.shapes;

import model.shapeContainers.ShapeContainer;
import view.GUIElements.canvas.CustomCanvas;
import view.types.ShapeType;

import java.util.ArrayList;
import java.util.List;

public class Point extends AbstractShape {
    private double width = 15, height = 15;
    private int priority = 1;
    private List<Dimension> dimensions = new ArrayList<>();

    public Point(double x, double y) {
        super(x, y);
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    public void draw(CustomCanvas gc) {
        gc.fillOvalWithOutScaling(this.getX(), this.getY(), width, height);
    }

    @Override
    public double calculateShapeLength() {
        return 0;
    }

    @Override
    public double calculateShapeArea() {
        return 0;
    }

    public double calculateDistanceFromMouse(double x, double y) {
        double deltaX = this.getX() - x;
        double deltaY = this.getY() - y;

        return Math.hypot(deltaX, deltaY);
    }

    @Override
    public void setCoordinates(double x, double y) {
        this.setX(x);
        this.setY(y);
    }

    public void setCoordinates(double[] coordinates) {
        this.setX(coordinates[0]);
        this.setY(coordinates[1]);
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public void addToShapeContainer(ShapeContainer shapeContainer) {
        shapeContainer.addShape(this);
    }

    @Override
    public ShapeType getType() {
        return ShapeType.POINT;
    }

    public void delete(ShapeContainer shapeContainer) {
        if (shapeContainer != null) shapeContainer.getShapes().remove(this);

        for (int i = 0; i < getChildren().size(); i++) {
            Shape shape = getChildren().get(i);
            getChildren().remove(i);
            shape.removePoint(this);
            i--;
            shape.delete(shapeContainer);
        }
    }

    public void addDimension(Dimension dimension) {
        dimensions.add(dimension);
    }
}
