package view.GUIElements;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.LIGHTGRAY;
import view.SettingsSingleton;

public class CanvasGrid {

    private CustomCanvas canvas;
    private int gridSize = 20;
    private double width, height;

    public CanvasGrid(CustomCanvas canvas) {
        this.canvas = canvas;
    }

    public void drawGrid() {
            canvas.beginPath();
            double width = SettingsSingleton.getGridWidth();
            double height = SettingsSingleton.getGridHeight();
            canvas.setFill(LIGHTGRAY);
            canvas.setStroke(LIGHTGRAY);
            canvas.setLineWidth(1);
            for (int i = 0; i < width; i += gridSize) {
                canvas.moveTo(i, 0);
                canvas.lineTo(i, height);
            }
            for (int i = 0; i < height; i += gridSize) {
                canvas.moveTo(0, i);
                canvas.lineTo(width, i);
            }
            canvas.stroke();
            canvas.setFill(BLACK);
            canvas.setStroke(BLACK);
            canvas.setLineWidth(5);
    }

    public void setCanvas(CustomCanvas canvas) {
        this.canvas = canvas;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

}
