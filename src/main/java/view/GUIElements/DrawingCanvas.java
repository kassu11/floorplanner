package view.GUIElements;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class DrawingCanvas extends Canvas implements CustomCanvas {
  private double width, height;
  private double x, y;
  private GraphicsContext gc;

  public DrawingCanvas(double width, double height) {
    super(width, height);
    this.width = width;
    this.height = height;
    this.x = 0;
    this.y = 0;
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
    gc.moveTo(x + this.x, y + this.y);
  }

  public void lineTo(double x, double y) {
    gc.lineTo(x + this.x, y + this.y);
  }

  public void rect(double x, double y, double width, double height) {
    gc.strokeRect(x + this.x, y + this.y, width, height);
  }

  public void fillOval(double x, double y, double width, double height) {
    gc.fillOval(x + this.x, y + this.y, width, height);
  }

  public void arc(double x, double y, double radiusX, double radiusY, double startAngle, double length) {
    gc.arc(x + this.x, y + this.y, radiusX, radiusY, startAngle, length);
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
    gc.setLineWidth(width);
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
    gc.strokeArc(x + this.x, y + this.y, width, height, startAngle, length, open);
  }

  public Canvas getCanvas() {
    return this;
  }
}
