package view;

import controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Point;
import model.Shape;
import model.ShapesSingleton;
import view.GUIElements.CanvasContainer;
import view.GUIElements.CustomCanvas;
import view.GUIElements.OptionsToolbar;
import view.GUIElements.DrawingToolbar;

import static javafx.scene.paint.Color.*;

public class GUI extends Application {

    Controller controller;
    Point lastPoint;
    private Point hoveredPoint;
    private int canvasWidth = 750;
    private int canvasHeight = 750;
    private double middleX, middleY;

    @Override
    public void init() {
        controller = new Controller(this);
    }

    @Override
    public void start(Stage stage) {

        CanvasContainer canvasContainer = new CanvasContainer(canvasWidth, canvasHeight);

        BorderPane root = new BorderPane();

        CustomCanvas gc = canvasContainer.getLayer(0);
        CustomCanvas previewGc = canvasContainer.getLayer(1);

        gc.setLineWidth(5);
        previewGc.setLineWidth(5);

        canvasContainer.setOnMouseClicked(event -> {
            if(event.getButton() != MouseButton.PRIMARY) return;
            Point endPoint = hoveredPoint;
            if (lastPoint == null) {
                if (hoveredPoint != null)
                    lastPoint = hoveredPoint;
                else
                    lastPoint = controller.createPoint(event.getX(), event.getY());
            } else {
                previewGc.clear();

                if (endPoint == null)
                    endPoint = controller.createPoint(event.getX(), event.getY());

                Shape newShape = controller.addShape(lastPoint, endPoint, CurrentShapeSingleton.getCurrentShape());
                newShape.draw(gc);
                newShape.calculateShapeArea();
                gc.clear();

                for (Shape shape : ShapesSingleton.getShapes())
                    shape.draw(gc);

                if (CurrentShapeSingleton.isShapeType(ShapeType.MULTILINE))
                    lastPoint = endPoint;
                else
                    lastPoint = null;
            }
        });
        // This is the preview drawing
        canvasContainer.setOnMouseMoved(event -> {
            previewGc.clear();
            Shape hoveredShape = null;
            hoveredPoint = null;
            double distanceCutOff = 15;
            double lowestDistance = distanceCutOff;

            for (Shape shape : ShapesSingleton.getShapes()) {
                double distance = shape.calculateDistanceFromMouse(event.getX(), event.getY());
                int bestPriority = hoveredShape != null ? hoveredShape.getPriority() : 0;
                if ((distance < lowestDistance && shape.getPriority() >= bestPriority)
                        || (distance < distanceCutOff && bestPriority < shape.getPriority())) {
                    if (shape == lastPoint)
                        continue;
                    lowestDistance = distance;
                    hoveredShape = shape;
                    if (shape.getClass().equals(Point.class))
                        hoveredPoint = (Point) shape;
                }
            }

            if (hoveredShape != null) {
                previewGc.setFill(RED);
                previewGc.setStroke(RED);
                hoveredShape.draw(previewGc);
            }

            if (lastPoint == null)
                return;

            previewGc.beginPath();
            double x = lastPoint.getX();
            double y = lastPoint.getY();
            switch (CurrentShapeSingleton.getCurrentShape()) {
                case LINE, MULTILINE -> {
                    previewGc.moveTo(x, y);
                    previewGc.lineTo(event.getX(), event.getY());
                }
                case RECTANGLE -> previewGc.rect(x, y, event.getX() - x, event.getY() - y);
                case CIRCLE -> previewGc.arc(x, y, Math.abs(event.getX() - x), Math.abs(event.getY() - y), 0, 360);
                case TRIANGLE -> {
                }
            }
            previewGc.stroke();
        });


        canvasContainer.setOnMousePressed(event -> {
            if(event.getButton() == MouseButton.MIDDLE) {
                middleX = event.getX() - canvasContainer.getX() ;
                middleY = event.getY() - canvasContainer.getY();
            }
        });

        canvasContainer.setOnMouseDragged(event -> {
            if(event.getButton() == MouseButton.MIDDLE) {
                canvasContainer.setX(event.getX() - middleX);
                canvasContainer.setY(event.getY() - middleY);
                canvasContainer.clear();
                for (Shape shape : ShapesSingleton.getShapes())
                    shape.draw(gc);
            }
        });

        DrawingToolbar drawToolbar = new DrawingToolbar(stage);
        drawToolbar.getButtons().get("Mode").setOnAction(event -> {
            drawToolbar.changeMode();
        });
        OptionsToolbar optionBar = new OptionsToolbar();

        root.setLeft(drawToolbar);
        root.setTop(optionBar);
        root.setCenter(canvasContainer);

        Scene view = new Scene(root, canvasWidth, canvasHeight);
        stage.setTitle("view.FPGUI");
        stage.setScene(view);
        stage.show();

        view.setOnKeyPressed(event -> {
            System.out.println(event.getCode());
            switch (event.getCode()) {
                case DIGIT1 -> CurrentShapeSingleton.setCurrentShape(ShapeType.LINE);
                case DIGIT2 -> CurrentShapeSingleton.setCurrentShape(ShapeType.RECTANGLE);
                case DIGIT3 -> CurrentShapeSingleton.setCurrentShape(ShapeType.CIRCLE);
                case DIGIT4 -> CurrentShapeSingleton.setCurrentShape(ShapeType.MULTILINE);
                case ESCAPE -> {
                    lastPoint = null;
                    previewGc.clear();
                }
                default -> {
                }
            }
        });
    }

    // This method only erases the line from the canvas and not the shape from the
    // list
    public void eraseSingleLine(GraphicsContext gc, double x, double y, double x1, double y1) {
        gc.setStroke(WHITE);
        gc.setLineWidth(2);
        gc.strokeLine(x, y, x1, y1);
        gc.setLineWidth(1);
        gc.setStroke(BLACK);
    }
}
