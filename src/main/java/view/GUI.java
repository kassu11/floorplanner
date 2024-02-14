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

    Controller Crontroller;

    @Override
    public void init() {
        Crontroller = new Controller(this);
    }

    @Override
    public void start(Stage stage){

        GridPane canvasContainer = new GridPane();

        Canvas canvas = new Canvas(500, 500);
        Canvas previewCanvas = new Canvas(500, 500);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        GraphicsContext previewGc = previewCanvas.getGraphicsContext2D();



        canvasContainer.setOnMouseClicked(event -> {
            if (clicks % 2 == 0){
                gc.moveTo(event.getX(), event.getY());
                x = event.getX();
                y = event.getY();
            }
            else {
                previewGc.clearRect(0, 0, 500, 500);
                gc.lineTo(event.getX(), event.getY());
                gc.stroke();
                gc.beginPath();
            }
            clicks++;
        });
        canvasContainer.setOnMouseMoved(event -> {
            if (clicks % 2 == 0) return;
            previewGc.clearRect(0, 0, 500, 500);
            previewGc.beginPath();
            previewGc.moveTo(x, y);
            previewGc.lineTo(event.getX(), event.getY());
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
