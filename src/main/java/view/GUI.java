package view;

import controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Line;
import model.Point;
import model.Shape;
import model.ShapesSingleton;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.paint.Color.*;

public class GUI extends Application {

    private int clicks;

    Controller controller;
    Point lastPoint;
    private Point hoveredPoint;

    private ShapeType currentShape = ShapeType.LINE;

    @Override
    public void init() {
        controller = new Controller(this);
    }

    @Override
    public void start(Stage stage) {

        GridPane canvasContainer = new GridPane();
        Canvas canvas = new Canvas(500, 500);
        Canvas previewCanvas = new Canvas(500, 500);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        GraphicsContext previewGc = previewCanvas.getGraphicsContext2D();

        gc.setLineWidth(5);
        previewGc.setLineWidth(5);

        canvasContainer.setOnMouseClicked(event -> {
            Point endPoint = hoveredPoint;
            if (clicks % 2 == 0) {
                if (hoveredPoint != null) lastPoint = hoveredPoint;
                else lastPoint = controller.createPoint(event.getX(), event.getY());
            } else {
                previewGc.clearRect(0, 0, 500, 500);

                if (hoveredPoint == null) endPoint = controller.createPoint(event.getX(), event.getY());

                Shape newShape = controller.addShape(lastPoint, endPoint, currentShape);
                newShape.draw(gc);
                gc.clearRect(0, 0, 500, 500);

                for (Shape shape : ShapesSingleton.getShapes()) shape.draw(gc);
                lastPoint = null;
            }
            clicks++;
        });
        // This is the preview drawing
        canvasContainer.setOnMouseMoved(event -> {
            previewGc.clearRect(0, 0, 500, 500);
            Shape hoveredShape = null;
            hoveredPoint = null;
            double minDistance = 15;

            for (Shape shape : ShapesSingleton.getShapes()) {
//             ***   if (contains(event.getX(), event.getY(), shape)) {

                if (shape.getClass().equals(Line.class)) {

                    double x1 = shape.getPoints().get(1).getX();
                    double x2 = shape.getPoints().get(0).getX();
                    double y1 = shape.getPoints().get(1).getY();
                    double y2 = shape.getPoints().get(0).getY();

                    if (x1 == x2) {
                        // System.out.println("Vertical line detected!");
                        if (betweenLinesWithoutSlope(event.getX(), event.getY(), x1, x2, y1, y2)) {
                            double distance = getDistanceWithoutSlope(event.getX(), x1);
                            if (distance < minDistance) {
                                hoveredShape = shape;
                            }
                        }
                    } else {
                        double slope = (y2 - y1) / (x2 - x1);
                        if (betweenLines(event.getX(), event.getY(), x1, x2, y1, y2, slope)) {
                            double distance = getDistance(event.getX(), event.getY(), x1, y1, slope);
                            // double distance = Math.sqrt(Math.pow(event.getX() - shape.getX(), 2) + Math.pow(event.getY() - shape.getY(), 2));
                            if (distance < minDistance) {
                                hoveredShape = shape;
                            }
                        }
                    }

                } else {
                    double distance = Math.sqrt(Math.pow(event.getX() - shape.getX(), 2) + Math.pow(event.getY() - shape.getY(), 2));
                    if (distance < minDistance) {
                        hoveredShape = shape;
                        if (shape.getClass().equals(Point.class)) {
                            hoveredPoint = (Point) shape;
                        }
                    }
                }
//              ***  }
            }

            if (hoveredShape != null) {
                previewGc.setFill(RED);
                previewGc.setStroke(RED);
                hoveredShape.draw(previewGc);
            }


            if (clicks % 2 == 0) return;

            previewGc.beginPath();
            double x = lastPoint.getX();
            double y = lastPoint.getY();
            switch (currentShape) {
                case LINE -> {
                    previewGc.moveTo(x, y);
                    previewGc.lineTo(event.getX(), event.getY());
                }
                case RECTANGLE -> previewGc.rect(x, y, event.getX() - x, event.getY() - y);
                case CIRCLE -> previewGc.arc(x, y, Math.abs(event.getX() - x), Math.abs(event.getY() - y), 0, 360);
            }
            previewGc.stroke();
        });

        canvasContainer.add(canvas, 0, 0);
        canvasContainer.add(previewCanvas, 0, 0);

        Scene view = new Scene(canvasContainer, 500, 500);
        stage.setTitle("view.FPGUI");
        stage.setScene(view);
        stage.show();
    }


    // This method only erases the line from the canvas and not the shape from the list
    public void eraseSingleLine(GraphicsContext gc, double x, double y, double x1, double y1) {
        gc.setStroke(WHITE);
        gc.setLineWidth(2);
        gc.strokeLine(x, y, x1, y1);
        gc.setLineWidth(1);
        gc.setStroke(BLACK);
    }

    private boolean contains(double x, double y, Shape shape) {
        return !(x < shape.getX() || y < shape.getY() || x > shape.getX() + shape.getWidth() || y > shape.getY() + shape.getHeight());
    }

    private boolean betweenLines(double mouseX, double mouseY, double x1, double x2, double y1, double y2, double slope) {
        double lineLength = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        double perpendicularSlope;

        if (slope == 0) {
            // System.out.println("Horizontal line detected!");
            return !(getDistanceWithoutSlope(mouseX, x1) > lineLength || getDistanceWithoutSlope(mouseX, x2) > lineLength);
        }

        perpendicularSlope = -1 / slope;
        return !(getDistance(mouseX, mouseY, x1, y1, perpendicularSlope) > lineLength || getDistance(mouseX, mouseY, x2, y2, perpendicularSlope) > lineLength);
    }

    private boolean betweenLinesWithoutSlope(double mouseX, double mouseY, double x1, double x2, double y1, double y2) {
        double lineLength = Math.abs(y2 - y1);
        return !(getDistance(mouseX, mouseY, x1, y1, 0) > lineLength || getDistance(mouseX, mouseY, x2, y2, 0) > lineLength);
    }

    private static double getDistance(double mouseX, double mouseY, double x1, double y1, double slope) {

//      System.out.println(x1 + " " + x2 + " " + y1 + " " + y2);
        double b = y1 - slope * x1;

//      System.out.println(slope + " " + b);
        return Math.abs(slope * mouseX - mouseY + b) / Math.sqrt(Math.pow(slope, 2) + 1);
    }

    private static double getDistanceWithoutSlope(double mouseX, double x1) {
        return Math.abs(mouseX - x1);
    }
}
