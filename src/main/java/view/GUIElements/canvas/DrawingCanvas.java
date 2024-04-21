package view.GUIElements.canvas;

import controller.Controller;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import view.SettingsSingleton;

public class DrawingCanvas extends Canvas implements CustomCanvas {
    private double width, height;
    private double x, y, zoom;
    private GraphicsContext gc;
    private Font font = new Font("Arial", 12);
    private CanvasGrid grid;

    private SettingsSingleton settings = SettingsSingleton.getInstance();

    public DrawingCanvas(double width, double height) {
        super(width, height);
        this.width = width;
        this.height = height;
        this.x = 0;
        this.y = 0;
        this.zoom = 1;
        gc = getGraphicsContext2D();
        grid = new CanvasGrid(this);
    }


    public void resizeCanvas(double width, double height) {
        this.width = width;
        this.height = height;
        super.setWidth(width);
        super.setHeight(height);
    }

    public void clear() {
        gc.clearRect(0, 0, width, height);
    }

    public void moveTo(double x, double y) {
        gc.moveTo(-this.x + x * zoom, -this.y + y * zoom);
    }

    public void lineTo(double x, double y) {
        gc.lineTo(-this.x + x * zoom, -this.y + y * zoom);
    }

    public void rect(double x, double y, double width, double height) {
        gc.strokeRect(-this.x + x * zoom, -this.y + y * zoom, width * zoom, height * zoom);
    }

    public void fillOval(double x, double y, double width, double height) {
        gc.fillOval(-this.x + x * zoom, -this.y + y * zoom, width * zoom, height * zoom);
    }

    public void fillOvalWithOutScaling(double x, double y, double width, double height) {
        gc.fillOval(-this.x + x * zoom - width / 2, -this.y + y * zoom - height / 2, width, height);
    }



    public void arc(double x, double y, double radiusX, double radiusY, double startAngle, double length) {
        gc.arc(-this.x + x * zoom, -this.y + y * zoom, radiusX * zoom, radiusY * zoom, startAngle, length);
    }

    public void setLineWidth(double width) {
        gc.setLineWidth(width);
    }

    public double getLineWidth() {
        return gc.getLineWidth();
    }

    public void setStrokeColor(String color) {
        gc.setStroke(javafx.scene.paint.Color.web(color));
    }

    public void setFillColor(String color) {
        gc.setFill(javafx.scene.paint.Color.web(color));
    }

    public void setStrokeWidth(int width) {
        gc.setLineWidth(width * zoom);
    }

    public void setFill(Color color) {
        gc.setFill(color);
    }

    public void setStroke(Color color) {
        gc.setStroke(color);
    }

    public void beginPath() {
        gc.beginPath();
    }

    public void stroke() {
        gc.stroke();
    }

    public void strokeArc(double x, double y, double width, double height, double startAngle, double length, ArcType open) {
        gc.strokeArc(-this.x + x * zoom, -this.y + y * zoom, width * zoom, height * zoom, startAngle, length, open);
    }

    public Canvas getCanvas() {
        return this;
    }

    @Override
    public void setLineWidth(int width) {
        gc.setLineWidth(width);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    @Override
    public void fillText(String text, double radians, double x, double y, double textHalfWidth) {
        double flipMultiplier = flipMultiplier(radians);
        double xPadding = Math.sin(radians) * -25 * flipMultiplier;
        double yPadding = Math.cos(radians) * 25 * flipMultiplier;
        gc.fillText(text, -this.x + x * zoom + xPadding - textHalfWidth, -this.y + y * zoom + yPadding);
    }

    @Override
    public Affine getTransform() {
        return gc.getTransform();
    }

    @Override
    public void setTransform(double radians, double x, double y) {
        double flipMultiplier = flipMultiplier(radians);
        Affine affine = new Affine();
        double xPadding = Math.sin(radians) * -25 * flipMultiplier;
        double yPadding = Math.cos(radians) * 25 * flipMultiplier;
        affine.appendRotation((flipMultiplier == 1 ? Math.toDegrees(radians) : Math.toDegrees(radians) + 180), -this.x + x * zoom + xPadding, -this.y + y * zoom + yPadding);

        gc.setTransform(affine);
    }

    public void setTransform(Affine affine) {
        gc.setTransform(affine);
    }

    @Override
    public void rotate(double angle) {
        gc.rotate(angle);
    }

    public int flipMultiplier(double radians) {
        return (radians > Math.PI / 2 || radians < -Math.PI / 2 ? -1 : 1);
    }

    public double getCanvasWidth() {
        return width;
    }

    public double getCanvasHeight() {
        return height;
    }

    public CanvasGrid getGrid() {
        return grid;
    }

    @Override
    public void closePath() {
        gc.closePath();
    }

    @Override
    public void fill() {
        gc.fill();
    }

    public void strokeText(String text, double x, double y) {
        gc.strokeText(text, -this.x + x * zoom, -this.y + y * zoom);
    }

    public void strokeText(String text, double x, double y, double offsetX, double offsetY) {
        gc.strokeText(text, -this.x + x * zoom + offsetX, -this.y + y * zoom + offsetY);
    }

    @Override
    public void drawRulerX() {
        double gridSize = settings.getGridSize();
        double height = settings.getGridHeight();
        for (int i = 0; i <= height / gridSize; i++) {
            gc.setLineWidth(1);
            double value = i * gridSize;
            gc.strokeText(String.valueOf((int) value), 0, i * gridSize * zoom);
        }
    }

    @Override
    public void drawRulerY() {
        double gridSize = settings.getGridSize();
        double width = settings.getGridWidth();
        for (int i = 0; i <= width / gridSize; i++) {
            gc.setLineWidth(1);
            double value = i * gridSize;
            gc.strokeText(String.valueOf((int) value), i * gridSize * zoom, 10);
        }
    }



}
