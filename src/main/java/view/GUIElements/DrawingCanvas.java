package view.GUIElements;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class DrawingCanvas extends Canvas implements CustomCanvas {
  private double width, height;
  private double x, y, zoom;
  private GraphicsContext gc;

  public DrawingCanvas(double width, double height) {
    super(width, height);
    this.width = width;
    this.height = height;
    this.x = 0;
    this.y = 0;
    this.zoom = 1;
    gc = getGraphicsContext2D();
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

  public void arc(double x, double y, double radiusX, double radiusY, double startAngle, double length) {
    gc.arc(-this.x + x * zoom, -this.y + y * zoom, radiusX * zoom, radiusY * zoom, startAngle, length);
  }

  public void setLineWidth(int width) {
    gc.setLineWidth(width);
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

  public void strokeArc(double x, double y, double width, double height, double startAngle, double length,
      ArcType open) {
    gc.strokeArc(-this.x + x * zoom, -this.y + y * zoom, width * zoom, height * zoom, startAngle, length, open);
  }

  public Canvas getCanvas() {
    return this;
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
}
