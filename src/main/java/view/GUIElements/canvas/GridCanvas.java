package view.GUIElements.canvas;

import model.shapes.Shape;
import view.SettingsSingleton;

import static javafx.scene.paint.Color.LIGHTGRAY;

public class GridCanvas extends CustomCanvas {
    private final SettingsSingleton settings = SettingsSingleton.getInstance();
    public GridCanvas(double width, double height) {
        super(width, height);
        setLineWidth(1);
    }

    public void drawGrid() {
        clear();
        if (settings.isGridEnabled()) {
            beginPath();
            gc.setStroke(CanvasColors.GRID_LINE);
            double width = settings.getGridWidth();
            double height = settings.getGridHeight();
            int gridSize = settings.getGridSize();
            for (int i = 0; i < width; i += gridSize) {
                moveTo(i, 0);
                lineTo(i, height);
            }
            for (int i = 0; i < height; i += gridSize) {
                moveTo(0, i);
                lineTo(width, i);
            }
            stroke();
        }

        if (settings.isUnitsVisible()) {
            beginPath();
            gc.setStroke(CanvasColors.RULER_TEXT);
            drawRulerX();
            drawRulerY();
        }

    }

    /**
     * Draws the vertical ruler numbers
     */
    void drawRulerX() {
        double gridSize = settings.getGridSize();
        double gridHeight = settings.getGridHeight();
        for (int i = 0; i <= gridHeight / gridSize; i++) {
            double value = i * gridSize;
            gc.strokeText(String.valueOf((int) value), 0, i * gridSize * zoom);
        }
    }
    /**
     * Draws the horizontal ruler numbers
     */
    void drawRulerY() {
        double gridSize = settings.getGridSize();
        double gridWidth = settings.getGridWidth();
        for (int i = 0; i <= gridWidth / gridSize; i++) {
            double value = i * gridSize;
            gc.strokeText(String.valueOf((int) value), i * gridSize * zoom, 10);
        }
    }

    @Override
    public void updateCanvasColors(Shape shape) {
        // This canvas should never render shapes, so if it does the color will be turned to purple
        setStrokeColor(CanvasColors.PURPLE);
        setFillColor(CanvasColors.PURPLE);
    }
}
