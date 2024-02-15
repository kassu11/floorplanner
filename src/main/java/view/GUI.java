package view;

import controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;

import static javafx.scene.paint.Color.*;

public class GUI extends Application {

    private int clicks;

    private double x, y;

    Controller controller;

    private ShapeType currentShape = ShapeType.RECTANGLE;

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

        canvasContainer.setOnMouseClicked(event -> {
            // This is the actual drawing
            if (clicks % 2 == 0) {
                gc.moveTo(event.getX(), event.getY());
                x = event.getX();
                y = event.getY();
            } else {
                previewGc.clearRect(0, 0, 500, 500);
                gc.beginPath();

                switch (currentShape) {
                    case LINE -> gc.lineTo(event.getX(), event.getY());
                    case RECTANGLE -> gc.rect(x, y, event.getX() - x, event.getY() - y);
                    case CIRCLE -> gc.arc(x, y, Math.abs(event.getX() - x), Math.abs(event.getY() - y), 0, 360);
                }
                gc.stroke();
                controller.addShape(x, y, event.getX(), event.getY(), currentShape);
            }
            clicks++;
        });
        // This is the preview drawing
        canvasContainer.setOnMouseMoved(event -> {
            if (clicks % 2 == 0) return;
            previewGc.clearRect(0, 0, 500, 500);
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
    public void eraseSingleLine(GraphicsContext gc , double x, double y, double x1, double y1){
        gc.setStroke(WHITE);
        gc.setLineWidth(2);
        gc.strokeLine(x, y, x1, y1);
        gc.setLineWidth(1);
        gc.setStroke(BLACK);
    }
}
