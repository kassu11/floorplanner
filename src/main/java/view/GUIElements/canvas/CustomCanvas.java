package view.GUIElements.canvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import javafx.scene.transform.Affine;
import model.shapes.Shape;
import view.SettingsSingleton;
/**
 * Drawing canvas class
 */
public abstract class CustomCanvas extends Canvas {
    /**
     * Width
     * Height
     */
    private double width, height;
    /**
     * X coordinate
     * Y coordinate
     * Zoom level
     */
    protected double x, y, zoom;
    /**
     * Graphics context
     */
    protected GraphicsContext gc;
    /**
     * Last fill color used
     */
    private String lastFillColor = null;
    /**
     * Settings singleton
     */
    private SettingsSingleton settings = SettingsSingleton.getInstance();
    /**
     * Constructor for the drawing canvas
     * @param width width
     * @param height height
     */
    public CustomCanvas(double width, double height) {
        super(width, height);
        this.width = width;
        this.height = height;
        this.x = 0;
        this.y = 0;
        this.zoom = 1;
        gc = getGraphicsContext2D();
    }
    /**
     * Resizes the canvas
     * @param width width
     * @param height height
     */
    public void resizeCanvas(double width, double height) {
        this.width = width;
        this.height = height;
        super.setWidth(width);
        super.setHeight(height);
    }
    /**
     * Clears the canvas
     */
    public void clear() {
        gc.clearRect(0, 0, width, height);
    }

    /**
     * Draws a line
     * @param x
     * @param y
     */
    public void moveTo(double x, double y) {
        gc.moveTo(-this.x + x * zoom, -this.y + y * zoom);
    }
    /**
     * Draws a line to
     * @param x
     * @param y
     */
    public void lineTo(double x, double y) {
        gc.lineTo(-this.x + x * zoom, -this.y + y * zoom);
    }
    /**
     * Draws a rectangle
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void rect(double x, double y, double width, double height) {
        gc.strokeRect(-this.x + x * zoom, -this.y + y * zoom, width * zoom, height * zoom);
    }
    /**
     * Draws a filled oval
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void fillOval(double x, double y, double width, double height) {
        gc.fillOval(-this.x + x * zoom, -this.y + y * zoom, width * zoom, height * zoom);
    }
    /**
     * Draws a filled oval without scaling
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void fillOvalWithOutScaling(double x, double y, double width, double height) {
        gc.fillOval(-this.x + x * zoom - width / 2, -this.y + y * zoom - height / 2, width, height);
    }
    /**
     * Draws an arc
     * @param x
     * @param y
     * @param radiusX
     * @param radiusY
     * @param startAngle
     * @param length
     */
    public void arc(double x, double y, double radiusX, double radiusY, double startAngle, double length) {
        gc.arc(-this.x + x * zoom, -this.y + y * zoom, radiusX * zoom, radiusY * zoom, startAngle, length);
    }
    /**
     * Sets the line width
     * @param width
     */
    public void setLineWidth(double width) {
        gc.setLineWidth(width);
    }
    /**
     * Gets the line width
     * @return line width
     */
    public double getLineWidth() {
        return gc.getLineWidth();
    }
    /**
     * Sets the stroke color
     * @param color
     */
    public void setStrokeColor(String color) {
        gc.setStroke(javafx.scene.paint.Color.web(color));
    }

    /**
     * Sets the stroke color
     * @param color
     */
    public void setStrokeColor(Paint color) {
        gc.setStroke(color);
    }
    /**
     * Sets the fill color with a string
     * @param color
     */
    public void setFillColor(String color) {
        if (color.equals(lastFillColor)) return;
        lastFillColor = color;
        gc.setFill(javafx.scene.paint.Color.web(color));
    }

    /**
     * Sets the fill color
     * @param color
     */
    public void setFillColor(Paint color) {
        gc.setFill(color);
        lastFillColor = null;
    }

    /**
     * Gets the fill color
     * @return fill color
     */
    public Paint getFillColor() {
        return gc.getFill();
    }
    /**
     * Sets the stroke width
     * @param width
     */
    public void setStrokeWidth(int width) {
        gc.setLineWidth(width * zoom);
    }
    /**
     * Sets the fill color
     * @param color
     */
    public void setFill(Color color) {
        gc.setFill(color);
    }
    /**
     * Sets the stroke color
     * @param color
     */
    public void setStroke(Color color) {
        gc.setStroke(color);
    }
    /**
     * Begins the path
     */
    public void beginPath() {
        gc.beginPath();
    }
    /**
     * Strokes the path
     */
    public void stroke() {
        gc.stroke();
    }
    /**
     * Strokes an arc
     * @param x
     * @param y
     * @param width
     * @param height
     * @param startAngle
     * @param length
     * @param open
     */
    public void strokeArc(double x, double y, double width, double height, double startAngle, double length, ArcType open) {
        gc.strokeArc(-this.x + x * zoom, -this.y + y * zoom, width * zoom, height * zoom, startAngle, length, open);
    }
    /**
     * Gets the canvas
     * @return canvas
     */
    public Canvas getCanvas() {
        return this;
    }
    /**
     * Sets the line width
     * @param width
     */
    public void setLineWidth(int width) {
        gc.setLineWidth(width);
    }
    /**
     * Sets the X coordinate
     * @param x
     */
    public void setX(double x) {
        this.x = x;
    }
    /**
     * Sets the Y coordinate
     * @param y
     */
    public void setY(double y) {
        this.y = y;
    }
    /**
     * Sets the zoom level
     * @param zoom
     */
    public void setZoom(double zoom) {
        this.zoom = zoom;
    }
    /**
     * Fills the text
     * @param text
     * @param radians
     * @param x
     * @param y
     * @param textHalfWidth
     */
    public void fillText(String text, double radians, double x, double y, double textHalfWidth) {
        double flipMultiplier = flipMultiplier(radians);
        double xPadding = Math.sin(radians) * -25 * flipMultiplier;
        double yPadding = Math.cos(radians) * 25 * flipMultiplier;
        gc.fillText(text, -this.x + x * zoom + xPadding - textHalfWidth, -this.y + y * zoom + yPadding);
    }
    /**
     * Gets the transform
     * @return transform
     */
    public Affine getTransform() {
        return gc.getTransform();
    }
    /**
     * Sets the transform
     * @param radians
     * @param x
     * @param y
     */
    public void setTransform(double radians, double x, double y) {
        double flipMultiplier = flipMultiplier(radians);
        Affine affine = new Affine();
        double xPadding = Math.sin(radians) * -25 * flipMultiplier;
        double yPadding = Math.cos(radians) * 25 * flipMultiplier;
        affine.appendRotation((flipMultiplier == 1 ? Math.toDegrees(radians) : Math.toDegrees(radians) + 180), -this.x + x * zoom + xPadding, -this.y + y * zoom + yPadding);

        gc.setTransform(affine);
    }
    /**
     * Sets the transform
     * @param affine
     */
    public void setTransform(Affine affine) {
        gc.setTransform(affine);
    }
    /**
     * Rotates the canvas
     * @param angle
     */
    public void rotate(double angle) {
        gc.rotate(angle);
    }
    /**
     * Flips the multiplier
     * @param radians
     * @return flip multiplier
     */
    public int flipMultiplier(double radians) {
        return (radians > Math.PI / 2 || radians < -Math.PI / 2 ? -1 : 1);
    }
    /**
     * Gets the canvas width
     * @return canvas width
     */
    public double getCanvasWidth() {
        return width;
    }
    /**
     * Gets the canvas height
     * @return canvas height
     */
    public double getCanvasHeight() {
        return height;
    }

    /**
     * Closes the path
     */
    public void closePath() {
        gc.closePath();
    }
    /**
     * Fills the path
     */
    public void fill() {
        gc.fill();
    }
    /**
     * Strokes the text
     * @param text
     * @param x
     * @param y
     */
    public void strokeText(String text, double x, double y) {
        gc.strokeText(text, -this.x + x * zoom, -this.y + y * zoom);
    }
    /**
     * Strokes the text
     * @param text
     * @param x
     * @param y
     * @param offsetX
     * @param offsetY
     */
    public void strokeText(String text, double x, double y, double offsetX, double offsetY) {
        gc.strokeText(text, -this.x + x * zoom + offsetX, -this.y + y * zoom + offsetY);
    }

    public void fillRect(double x, double y, double width, double height) {
        gc.fillRect(-this.x + x * zoom, -this.y + y * zoom, width * zoom, height * zoom);
    }

    /**
     * Updates the canvas colors based on the shape
     * @param shape
     */
    public abstract void updateCanvasColors(Shape shape);
}
