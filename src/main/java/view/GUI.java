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

import static javafx.scene.paint.Color.*;

public class GUI extends Application {

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
            if (lastPoint == null) {
                if (hoveredPoint != null) lastPoint = hoveredPoint;
                else lastPoint = controller.createPoint(event.getX(), event.getY());
            } else {
                previewGc.clearRect(0, 0, 500, 500);

                if (endPoint == null) endPoint = controller.createPoint(event.getX(), event.getY());

                Shape newShape = controller.addShape(lastPoint, endPoint, currentShape);
                newShape.draw(gc);
                newShape.calculateShapeArea();
                gc.clearRect(0, 0, 500, 500);

                for (Shape shape : ShapesSingleton.getShapes()) shape.draw(gc);
                lastPoint = null;
            }
        });
        // This is the preview drawing
        canvasContainer.setOnMouseMoved(event -> {
            previewGc.clearRect(0, 0, 500, 500);
            Shape hoveredShape = null;
            hoveredPoint = null;
            double distanceCutOff = 15;
            double lowestDistance = distanceCutOff;

            for (Shape shape : ShapesSingleton.getShapes()) {
                double distance = shape.calculateDistanceFromMouse(event.getX(), event.getY());
                int bestPriority = hoveredShape != null ? hoveredShape.getPriority() : 0;
                if ((distance < lowestDistance && shape.getPriority() >= bestPriority) || (distance < distanceCutOff && bestPriority < shape.getPriority())) {
                    if (shape == lastPoint) continue;
                    lowestDistance = distance;
                    hoveredShape = shape;
                    if (shape.getClass().equals(Point.class)) hoveredPoint = (Point) shape;
                }
            }

            if (hoveredShape != null) {
                previewGc.setFill(RED);
                previewGc.setStroke(RED);
                hoveredShape.draw(previewGc);
            }


            if (lastPoint == null) return;

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
}
