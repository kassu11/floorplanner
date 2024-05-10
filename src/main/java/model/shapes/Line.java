package model.shapes;

import javafx.scene.text.Text;
import javafx.scene.transform.Affine;
import model.shapeContainers.ShapeContainer;
import view.GUIElements.canvas.CustomCanvas;
import view.types.ShapeType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * Concrete class for handling lines
 */
public class Line extends AbstractShape {
    /**
     * List of dimensions
     */
    private List<Dimension> dimensions = new ArrayList<>();
    /**
     * Constructor for the line with a list of points
     * @param pointA first point
     * @param pointB end point
     */
    public Line(Point pointA, Point pointB) {
        super(pointA, pointB);
    }
    /**
     * Calculates the length of the line
     * @return length of the line
     */
    @Override
    public double calculateShapeLength() {
        double deltaX = this.getPoints().get(0).getX() - this.getPoints().get(1).getX();
        double deltaY = this.getPoints().get(0).getY() - this.getPoints().get(1).getY();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
    /**
     * Calculates the area of the line
     * @return area of the line
     */
    @Override
    public double calculateShapeArea() {
        System.out.println("Line has no area");
        return 0;
    }
    /**
     * Draws the line
     * @param gc custom canvas
     */
    @Override
    public void draw(CustomCanvas gc) {
        gc.updateCanvasColors(this);
        Point pointA = this.getPoints().get(0);
        Point pointB = this.getPoints().get(1);
        gc.beginPath();
        gc.moveTo(pointA.getX(), pointA.getY());
        gc.lineTo(pointB.getX(), pointB.getY());
        gc.stroke();

        dimensions.forEach(dimension -> dimension.draw(gc));
    }
    /**
     * Calculates the distance from the mouse to the line
     * @param x X coordinate of the mouse
     * @param y Y coordinate of the mouse
     * @return distance from the mouse to the line
     */
    @Override
    public double calculateDistanceFromMouse(double x, double y) {
        double x1 = this.getPoints().get(1).getX();
        double x2 = this.getPoints().get(0).getX();
        double y1 = this.getPoints().get(1).getY();
        double y2 = this.getPoints().get(0).getY();

        if (x1 == x2) {
            if (betweenLinesWithoutSlope(x, y, x1, x2, y1, y2)) return getDistanceWithoutSlope(x, x1);
        } else {
            double slope = (y2 - y1) / (x2 - x1);
            if (betweenLines(x, y, x1, x2, y1, y2, slope)) return getDistance(x, y, x1, y1, slope);
        }

        return 1000; // High default value
    }
    /**
     * Returns the type of the shape
     * @return type of the shape
     */
    @Override
    public ShapeType getType() {
        return ShapeType.LINE;
    }
    /**
     * Returns the distance of the shape
     * @return distance of the shape
     */
    private static double getDistance(double mouseX, double mouseY, double x1, double y1, double slope) {
        double b = y1 - slope * x1;
        return Math.abs(slope * mouseX - mouseY + b) / Math.sqrt(Math.pow(slope, 2) + 1);
    }
    /**
     * Returns the distance of the shape without slope
     * @return distance of the shape without slope
     */
    private static double getDistanceWithoutSlope(double mouseX, double x1) {
        return Math.abs(mouseX - x1);
    }
    /**
     * Checks if the mouse is between the lines
     * @return true if the mouse is between the lines
     */
    private boolean betweenLines(double mouseX, double mouseY, double x1, double x2, double y1, double y2, double slope) {
        double lineLength = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        double perpendicularSlope;

        if (slope == 0) {
            return !(getDistanceWithoutSlope(mouseX, x1) > lineLength || getDistanceWithoutSlope(mouseX, x2) > lineLength);
        }

        perpendicularSlope = -1 / slope;
        return !(getDistance(mouseX, mouseY, x1, y1, perpendicularSlope) > lineLength || getDistance(mouseX, mouseY, x2, y2, perpendicularSlope) > lineLength);
    }
    /**
     * Checks if the mouse is between the lines without slope
     * @return true if the mouse is between the lines without slope
     */
    private boolean betweenLinesWithoutSlope(double mouseX, double mouseY, double x1, double x2, double y1, double y2) {
        double lineLength = Math.abs(y2 - y1);
        return !(getDistance(mouseX, mouseY, x1, y1, 0) > lineLength || getDistance(mouseX, mouseY, x2, y2, 0) > lineLength);
    }
    /**
     * Deletes the line from the shape container
     * @param shapeContainer shape container
     */
    @Override
    public void delete(ShapeContainer shapeContainer) {
        if (shapeContainer != null) shapeContainer.getShapes().remove(this);
        Iterator<Point> iterator = getPoints().iterator();
        while (iterator.hasNext()) {
            Point point = iterator.next();
            point.removeChild(this);
            if(point.getChildren().isEmpty() && shapeContainer != null) shapeContainer.getShapes().remove(point);
            iterator.remove();
        }
    }
    /**
     * Draws the length of the line
     * @param gc custom canvas
     * @param unit unit of the length
     * @param modifier modifier of the length
     */
    @Override
    public void drawLength(CustomCanvas gc, String unit, double modifier) {

        Point pointA = this.getPoints().get(0);
        Point pointB = this.getPoints().get(1);

        String text = String.format("%.2f "+ unit, calculateShapeLength() * modifier);
        final Text textElement = new Text(text);

        double textOffset = textElement.getLayoutBounds().getWidth() / 2;
        Affine original = gc.getTransform();
        double deltaX = pointA.getX() - pointB.getX();
        double deltaY = pointA.getY() - pointB.getY();
        double radians = Math.atan2(deltaY, deltaX);
        this.calculateCentroid();

        gc.setTransform(radians, this.getCentroidX(), this.getCentroidY());
        gc.fillText(text, radians, this.getCentroidX(), this.getCentroidY(), textOffset);
        gc.setTransform(original);
    }
    /**
     * Adds a dimension to the line
     * @param dimension dimension to be added
     */
    public void addDimension(Dimension dimension) {
        dimensions.add(dimension);
    }
    /**
     * The line to match dimensions
     */
    public void resizeToDimensions() {
        dimensions.forEach(Dimension::resize);
    }
}
