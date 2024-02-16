package view;

import controller.Controller;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Point;
import model.Shape;
import model.ShapesSingleton;
import view.GUIElements.OptionsToolbar;
import view.GUIElements.DrawingToolbar;

import static javafx.scene.paint.Color.*;

public class GUI extends Application {

    private int clicks;

    private double x, y;

    private int canvasWidth = 750;
    private int canvasHeight = 750;

    private Controller controller;

    @Override
    public void init() {
        controller = new Controller(this);
    }

    @Override
    public void start(Stage stage) {

        BorderPane root = new BorderPane();
        GridPane canvasContainer = new GridPane();
        Canvas canvas = new Canvas(canvasWidth, canvasHeight);
        Canvas previewCanvas = new Canvas(canvasWidth, canvasHeight);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        GraphicsContext previewGc = previewCanvas.getGraphicsContext2D();

        canvasContainer.setOnMouseClicked(event -> {
            // This is the actual drawing
            if (clicks % 2 == 0) {
                if (CurrentShapeSingleton.getCurrentShape().equals(ShapeType.LINE)) gc.moveTo(event.getX(), event.getY());
                x = event.getX();
                y = event.getY();
            } else {
                previewGc.clearRect(0, 0, canvasWidth, canvasHeight);

                switch (CurrentShapeSingleton.getCurrentShape()) {
                    case LINE -> gc.lineTo(event.getX(), event.getY());
                    case RECTANGLE -> gc.rect(x, y, event.getX() - x, event.getY() - y);
                    case CIRCLE -> gc.arc(x, y, Math.abs(event.getX() - x), Math.abs(event.getY() - y), 0, 360);
                }
                gc.stroke();
                gc.beginPath();
                controller.addShape(x, y, event.getX(), event.getY(), CurrentShapeSingleton.getCurrentShape());
                gc.clearRect(0, 0, canvasWidth, canvasHeight);


                for(Shape shape : ShapesSingleton.getShapes()){
                    gc.beginPath();
                    if(shape.getClass().equals(Point.class)){
                        Point point = (Point) shape;
                        gc.fillOval(point.getX() - point.getHeight()/2, point.getY() - point.getWidth()/2, point.getWidth(), point.getHeight());
                        System.out.println("Point");
                        continue;
                    }
                    switch (CurrentShapeSingleton.getCurrentShape()) {
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
            if (clicks % 2 == 0) return;
            previewGc.clearRect(0, 0, canvasWidth, canvasHeight);
            previewGc.beginPath();
            switch (CurrentShapeSingleton.getCurrentShape()) {
                case LINE -> {
                    previewGc.moveTo(x, y);
                    previewGc.lineTo(event.getX(), event.getY());
                }
                case RECTANGLE -> previewGc.rect(x, y, event.getX() - x, event.getY() - y);
                case CIRCLE -> previewGc.arc(x, y, Math.abs(event.getX() - x), Math.abs(event.getY() - y), 0, 360);
            }
            previewGc.stroke();
        });

        DrawingToolbar drawToolbar = new DrawingToolbar(stage);
        drawToolbar.getButtons().get("Mode").setOnAction(event -> {
            drawToolbar.changeMode();
        });
        OptionsToolbar optionBar = new OptionsToolbar();


        root.setLeft(drawToolbar);
        root.setTop(optionBar);
        root.setCenter(canvasContainer);

        canvasContainer.add(canvas, 0, 0);
        canvasContainer.add(previewCanvas, 0, 0);

        Scene view = new Scene(root, canvasWidth, canvasHeight);
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
