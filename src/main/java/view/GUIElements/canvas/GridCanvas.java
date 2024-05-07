package view.GUIElements.canvas;

import model.shapes.Shape;
import view.SettingsSingleton;
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
        double gridWidth = settings.getGridWidth();
        double minValue = 1;
        double minGapBetweenText = 75;
        gc.setFill(CanvasColors.GRAY);
        gc.fillRect(0, 0, getWidth(), 20);
        gc.setFill(CanvasColors.BLACK);
        gc.fillRect(-this.x + 0 * zoom, 0, gridWidth * zoom, 20);
        gc.setStroke(CanvasColors.GREEN);

        double startValue = Math.max(Math.floor(this.x / minGapBetweenText) * minGapBetweenText / zoom, 0);
        double offset = this.x < 0 ? -this.x : -this.x % minGapBetweenText;
        for (int i = 0; i <= 50; i++) {
            double textPosition = i * minGapBetweenText;
            double value = textPosition / zoom + startValue;
            gc.strokeText(String.valueOf((int) value), offset + textPosition, 20);
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
//            gc.strokeText(String.valueOf((int) value), i * gridSize * zoom, 10);
        }
    }

    @Override
    public void updateCanvasColors(Shape shape) {
        // This canvas should never render shapes, so if it does the color will be turned to purple
        setStrokeColor(CanvasColors.PURPLE);
        setFillColor(CanvasColors.PURPLE);
    }
}
