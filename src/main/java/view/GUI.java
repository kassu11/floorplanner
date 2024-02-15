package view;

import controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

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
            if (clicks % 2 == 0) {
                gc.moveTo(event.getX(), event.getY());
                x = event.getX();
                y = event.getY();
            } else {
                previewGc.clearRect(0, 0, 500, 500);

                switch (currentShape) {
                    case LINE -> gc.lineTo(event.getX(), event.getY());
                    case RECTANGLE -> {
                        gc.lineTo(event.getX(), y);
                        gc.lineTo(event.getX(), event.getY());
                        gc.lineTo(x, event.getY());
                        gc.lineTo(x, y);
                    }
                }
                gc.stroke();
                gc.beginPath();
                controller.addShape(x, y, event.getX(), event.getY(), currentShape);
            }
            clicks++;
        });

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
}
