package view;

import controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
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

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.paint.Color.*;

public class GUI extends Application {

    Controller controller;
    Point lastPoint;
    private Point hoveredPoint;
    private Shape selectedShape;
    private CanvasContainer canvasContainer;
    private int canvasWidth = 750;
    private int canvasHeight = 750;
    private double middleX, middleY;

    @Override
    public void init() {
        canvasContainer = new CanvasContainer(canvasWidth, canvasHeight);
        controller = new Controller(this);
    }

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        CustomCanvas gc = canvasContainer.getLayer(0);
        CustomCanvas previewGc = canvasContainer.getLayer(1);

        gc.setLineWidth(5);
        previewGc.setLineWidth(5);

        canvasContainer.setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.PRIMARY)
                return;
            Point endPoint = hoveredPoint;
            double mouseX = controller.getCanvasMath().relativeXtoAbsoluteX(event.getX());
            double mouseY = controller.getCanvasMath().relativeYtoAbsoluteY(event.getY());

            switch (SettingsSingleton.getCurrentMode()) {
                case DRAW -> {
                    if (lastPoint == null) {
                        if (hoveredPoint != null)
                            lastPoint = hoveredPoint;
                        else
                            lastPoint = controller.createAbsolutePoint(mouseX, mouseY);
                    } else {
                        previewGc.clear();

                        if (endPoint == null)
                            endPoint = controller.createAbsolutePoint(mouseX, mouseY);

                        Shape newShape = controller.addShape(lastPoint, endPoint, SettingsSingleton.getCurrentShape());
                        newShape.draw(gc);
                        newShape.calculateShapeArea();
                        gc.clear();

                        for (Shape shape : ShapesSingleton.getShapes())
                            shape.draw(gc);

                        if (SettingsSingleton.isShapeType(ShapeType.MULTILINE))
                            lastPoint = endPoint;
                        else
                            lastPoint = null;
                    }
                }
                case SELECT -> {
                    if (hoveredPoint != null && selectedShape == null) {
                        selectedShape = hoveredPoint;
                    } else if (selectedShape != null) {
                        gc.clear();
                        selectedShape.setCoordinates(mouseX, mouseY);
                        for (Shape shape : selectedShape.getChildren()) {
                            if (hoveredPoint != null) {
                                for (Point point : shape.getPoints()) {
                                    if (point.equals(selectedShape)) {
                                        shape.getPoints().set(shape.getPoints().indexOf(point), hoveredPoint);
                                        hoveredPoint.addChild(shape);
                                        ShapesSingleton.getShapes().remove(point);
                                        break;
                                    }
                                }
                            }
                            shape.setCoordinates(shape.getX() + mouseX - selectedShape.getX(),
                                    shape.getY() + mouseY - selectedShape.getY());
                            shape.draw(gc);
                        }
                        selectedShape = null;
                        controller.drawAllShapes(gc);
                        previewGc.clear();
                    }
                }

            }
        });
        // This is the preview drawing
        canvasContainer.setOnMouseMoved(event -> {
            previewGc.clear();
            Shape hoveredShape = null;
            List<Shape> startingPoints = new ArrayList<>();
            hoveredPoint = null;
            double distanceCutOff = 15;
            double lowestDistance = distanceCutOff;

            double mouseX = controller.getCanvasMath().relativeXtoAbsoluteX(event.getX());
            double mouseY = controller.getCanvasMath().relativeYtoAbsoluteY(event.getY());

            for (Shape shape : ShapesSingleton.getShapes()) {

                double distance = shape.calculateDistanceFromMouse(mouseX, mouseY);
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

            if (selectedShape != null) {
                previewGc.setFill(BLUE);
                previewGc.setStroke(BLUE);
                selectedShape.draw(previewGc);
                for (Shape shape : selectedShape.getChildren()) {
                    for (Point point : shape.getPoints()) {
                        if (!point.equals(selectedShape)) {
                            startingPoints.add(point);
                        }
                    }
                }
            }

            previewGc.beginPath();

            if (SettingsSingleton.getCurrentMode() == ModeType.DRAW) {
                if (lastPoint == null)
                    return;
                double x = lastPoint.getX();
                double y = lastPoint.getY();
                switch (SettingsSingleton.getCurrentShape()) {
                    case LINE, MULTILINE -> {
                        previewGc.moveTo(x, y);
                        previewGc.lineTo(mouseX, mouseY);
                    }
                    case RECTANGLE -> previewGc.rect(x, y, mouseX - x, mouseY - y);
                    case CIRCLE -> previewGc.arc(x, y, Math.abs(mouseX - x), Math.abs(mouseY - y), 0, 360);
                    case TRIANGLE -> {
                    }
                }
            } else if (!startingPoints.isEmpty()) {
                for (Shape startingPoint : startingPoints) {
                    previewGc.moveTo(startingPoint.getX(), startingPoint.getY());
                    previewGc.lineTo(mouseX, mouseY);
                }
            }

            previewGc.stroke();
        });

        canvasContainer.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.MIDDLE) {
                middleX = canvasContainer.getX() + event.getX();
                middleY = canvasContainer.getY() + event.getY();
            }
        });

        canvasContainer.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.MIDDLE) {
                canvasContainer.setX(middleX - event.getX());
                canvasContainer.setY(middleY - event.getY());
                canvasContainer.clear();
                for (Shape shape : ShapesSingleton.getShapes())
                    shape.draw(gc);
            }
        });

        DrawingToolbar drawToolbar = new DrawingToolbar(stage);
        drawToolbar.getButtons().get("Mode").setOnAction(event -> drawToolbar.changeMode(ModeType.DRAW));
        drawToolbar.getButtons().get("Select").setOnAction(event -> drawToolbar.changeMode(ModeType.SELECT));
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
                case DIGIT1 -> SettingsSingleton.setCurrentShape(ShapeType.LINE);
                case DIGIT2 -> SettingsSingleton.setCurrentShape(ShapeType.RECTANGLE);
                case DIGIT3 -> SettingsSingleton.setCurrentShape(ShapeType.CIRCLE);
                case DIGIT4 -> SettingsSingleton.setCurrentShape(ShapeType.MULTILINE);
                case ESCAPE -> {
                    lastPoint = null;
                    previewGc.clear();
                }
                default -> {
                }
            }
        });
    }

    public CanvasContainer getCanvasContainer() {
        System.out.println(canvasContainer);
        return canvasContainer;
    }
}
