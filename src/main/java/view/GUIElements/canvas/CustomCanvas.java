package view.GUIElements.canvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.shape.ArcType;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public interface CustomCanvas {
  public void resize(double width, double height);

  public void moveTo(double x, double y);

  public void arc(double x, double y, double radiusX, double radiusY, double startAngle, double length);

  public void rect(double x, double y, double width, double height);

  public void lineTo(double x, double y);

  public void fillOval(double x, double y, double width, double height);

  public void fillOvalWithOutScaling(double x, double y, double width, double height);

  public void clear();

  public Canvas getCanvas();

  public void setLineWidth(int width);

  public void setStrokeColor(String color);

  public void setFillColor(String color);

  public void setStrokeWidth(int width);

  public void setFill(Color color);

  public void setStroke(Color color);

  public void beginPath();

  public void stroke();

  public void strokeArc(double x, double y, double width, double height, double startAngle, double length, ArcType open);

  public void setX(double x);

  public void setY(double y);

  public void setZoom(double zoom);

  public void fillText(String text, double radians, double x, double y, double textHalfWidth);

  public Affine getTransform();

  public void rotate(double angle);

  public void setTransform(double decrease, double x, double y);

  public void setTransform(Affine affine);

  public double getCanvasWidth();

  public double getCanvasHeight();

  public CanvasGrid getGrid();
}