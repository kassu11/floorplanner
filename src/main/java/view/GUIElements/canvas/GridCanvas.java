package view.GUIElements.canvas;

import javafx.scene.text.Font;
import model.shapes.Shape;
import view.SettingsSingleton;
public class GridCanvas extends CustomCanvas {
    private final SettingsSingleton settings = SettingsSingleton.getInstance();
    private final double minGapBetweenText = 75;
    private final double maxGapBetweenText = 150;
    private Font rulerFont = new Font("Arial", 10);
    public GridCanvas(double width, double height) {
        super(width, height);
        setLineWidth(.5);
        gc.setFont(rulerFont);
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
        double gridWidth = settings.getGridWidth();
        gc.setFill(CanvasColors.LIGHT_GRAY);
        gc.fillRect(0, 0, getWidth(), 20);
        gc.setFill(CanvasColors.DARK_GRAY);
        gc.fillRect(-this.x + 0 * zoom, 0, gridWidth * zoom, 20);
        gc.setStroke(CanvasColors.WHITE);

        double getRulerGapDivider = getRulerGapDivider();
        double gapBetweenText = Math.round(getRulerGapDivider * zoom);
//        System.out.println(gapBetweenText);

        double positiveStartValue = Math.max(Math.floor(this.x / gapBetweenText) * gapBetweenText / zoom, 0);
        double negativeStartValue = Math.min(Math.floor((this.x + getWidth()) / gapBetweenText) * gapBetweenText / zoom, 0);
        double offset = this.x < 0 ? -this.x : -this.x % gapBetweenText;
        double negOffset = offset > getWidth() ? getWidth() + offset % getWidth() : offset;
//        System.out.println(positiveStartValue);
        for (int i = 0; i < 50; i++) {
            double textPosition = i * gapBetweenText;
            double value = Math.round((textPosition / zoom + positiveStartValue) / getRulerGapDivider) * getRulerGapDivider;
            gc.strokeText(String.valueOf((int) value), offset + textPosition, 10);
        }
        for (int i = 0; i < 50; i++) {
            double textPosition = i * -gapBetweenText;
            double value = Math.round((textPosition / zoom + negativeStartValue) / getRulerGapDivider) * getRulerGapDivider;
            if (i == 0) {
                System.out.println(value + "    " + (negOffset));
            }
//            System.out.println(offset + textPosition);
            gc.strokeText(String.valueOf((int) value), negOffset + textPosition, 10);
        }
    }

    private double getRulerGapDivider() {
        double minValue = Math.ceil(this.minGapBetweenText / zoom);
        double maxValue = this.maxGapBetweenText / zoom;
        double gridSize = settings.getGridSize();
        Double[] gapDividers = new Double[]{gridSize, 500.0, 100.0, 50.0, 25.0, 15.0, 10.0, 5.0, 2.0};
        if (minValue < 1 && maxValue < 1) return 1;

        for(Double divider : gapDividers) {
            double gap = Math.ceil(minValue / divider) * divider;
            if(maxValue / divider >= 1 && gap <= maxValue){
                return gap;
            }
        }

        return minValue;
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
