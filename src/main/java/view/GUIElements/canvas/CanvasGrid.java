package view.GUIElements.canvas;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.LIGHTGRAY;

import view.SettingsSingleton;
/**
 * Class for handling the canvas grid
 */
public class CanvasGrid {
    /**
     * Custom canvas
     */
    private CustomCanvas canvas;
    /**
     * Settings singleton
     */
    private SettingsSingleton settings = SettingsSingleton.getInstance();
    /**
     * Constructor for the canvas grid
     * @param canvas custom canvas
     */
    public CanvasGrid(CustomCanvas canvas) {
        this.canvas = canvas;
    }
    /**
     * Draws the grid
     */
    public void drawGrid() {
        canvas.beginPath();
        double width = settings.getGridWidth();
        double height = settings.getGridHeight();
        int gridSize = settings.getGridSize();
        canvas.setLineWidth(1);
        for (int i = 0; i < width; i += gridSize) {
            canvas.setStroke(LIGHTGRAY);
            canvas.moveTo(i, 0);
            canvas.lineTo(i, height);
        }
        for (int i = 0; i < height; i += gridSize) {
            canvas.setStroke(LIGHTGRAY);
            canvas.moveTo(0, i);
            canvas.lineTo(width, i);
        }
        canvas.stroke();
        canvas.setFill(BLACK);
        canvas.setStroke(BLACK);
        canvas.setLineWidth(5);
    }
    /**
     * Sets the canvas
     * @param canvas custom canvas
     */
    public void setCanvas(CustomCanvas canvas) {
        this.canvas = canvas;
    }
}
