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

    private double x, y;

    Controller controller;
    List<Shape> hoveredPoints = new ArrayList<>();

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
            // This is the actual drawing
            if (clicks % 2 == 0) {
                if (currentShape.equals(ShapeType.LINE)) gc.moveTo(event.getX(), event.getY());
                x = event.getX();
                y = event.getY();
            } else {
                previewGc.clearRect(0, 0, 500, 500);

                switch (currentShape) {
                    case LINE -> gc.lineTo(event.getX(), event.getY());
                    case RECTANGLE -> gc.rect(x, y, event.getX() - x, event.getY() - y);
                    case CIRCLE -> gc.arc(x, y, Math.abs(event.getX() - x), Math.abs(event.getY() - y), 0, 360);
                }
                gc.stroke();
                gc.beginPath();
                controller.addShape(x, y, event.getX(), event.getY(), currentShape);
                gc.clearRect(0, 0, 500, 500);


                for (Shape shape : ShapesSingleton.getShapes()) {
                    gc.beginPath();
                    if (shape.getClass().equals(Point.class)) {
                        Point point = (Point) shape;
                        gc.fillOval(point.getX() - point.getHeight() / 2, point.getY() - point.getWidth() / 2, point.getWidth(), point.getHeight());
                        continue;
                    }
                    switch (currentShape) {
                        case LINE -> {
                            gc.moveTo(shape.getPoints().get(0).getX(), shape.getPoints().get(0).getY());
                            gc.lineTo(shape.getPoints().get(1).getX(), shape.getPoints().get(1).getY());
                        }
                        case RECTANGLE -> gc.rect(x, y, shape.getX() - x, shape.getY() - y);
                        case CIRCLE -> gc.arc(x, y, Math.abs(shape.getX() - x), Math.abs(shape.getY() - y), 0, 360);
                    }
                    gc.stroke();
                }
            }
            clicks++;
        });
        // This is the preview drawing
        canvasContainer.setOnMouseMoved(event -> {
            previewGc.clearRect(0, 0, 500, 500);
            Shape hoveredShape = null;
//            hoveredPoint = null;
            double minDistance = 15;

            for (Shape shape : ShapesSingleton.getShapes()) {
                if (contains(event.getX(), event.getY(), shape)) {

                    if (shape.getClass().equals(Line.class)) {
                        double x1 = shape.getPoints().get(1).getX();
                        double x2 = shape.getPoints().get(0).getX();
                        double y1 = shape.getPoints().get(1).getY();
                        double y2 = shape.getPoints().get(0).getY();

                        System.out.println(x1 + " " + x2 + " " + y1 + " " + y2);

                        double slope = (y2 - y1) / (x2 - x1);

                        double b = y1 - slope * x1;

                        System.out.println(slope + " " + b);

                        double distance = Math.abs(slope * event.getX() - event.getY() + b) / Math.sqrt(Math.pow(slope, 2) + 1);

                        System.out.println(distance);
                        // double distance = Math.sqrt(Math.pow(event.getX() - shape.getX(), 2) + Math.pow(event.getY() - shape.getY(), 2));
                        if (distance < minDistance) {
                            hoveredShape = shape;
                        }
                    }
                }
            }

            if (hoveredShape != null) {
                previewGc.beginPath();

                previewGc.setFill(RED);
                previewGc.setStroke(RED);
                if (hoveredShape.getClass().equals(Point.class)) {
                    previewGc.fillOval(hoveredShape.getX() - hoveredShape.getHeight() / 2, hoveredShape.getY() - hoveredShape.getWidth() / 2, hoveredShape.getWidth(), hoveredShape.getHeight());
//                    hoveredPoint = hoveredShape;
                } else {
                    switch (currentShape) {
                        case LINE -> {
                            previewGc.moveTo(hoveredShape.getPoints().get(0).getX(), hoveredShape.getPoints().get(0).getY());
                            previewGc.lineTo(hoveredShape.getPoints().get(1).getX(), hoveredShape.getPoints().get(1).getY());
                        }
                        case RECTANGLE -> previewGc.rect(x, y, hoveredShape.getX() - x, hoveredShape.getY() - y);
                        case CIRCLE ->
                                previewGc.arc(x, y, Math.abs(hoveredShape.getX() - x), Math.abs(hoveredShape.getY() - y), 0, 360);
                    }
                    previewGc.stroke();
                }
            }
            ;


            if (clicks % 2 == 0) return;

            previewGc.beginPath();
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
}
