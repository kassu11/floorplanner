package view.GUIElements.canvas;

import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import model.shapes.Shape;
import view.SettingsSingleton;
public class GridCanvas extends CustomCanvas {
    private final SettingsSingleton settings = SettingsSingleton.getInstance();
    private final double minGapBetweenText = 75;
    private final double maxGapBetweenText = 250;
    private Font rulerFont = new Font("Arial", 10);
    private final Affine original = gc.getTransform();
    private final Affine rotate = new Affine();
    public GridCanvas(double width, double height) {
        super(width, height);
        setLineWidth(.5);
        gc.setFont(rulerFont);
        rotate.appendRotation(-90, 0, 0);
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
            gc.fillRect(0, 0, 20, 20);
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
        gc.setTextAlign(TextAlignment.LEFT);

        double getRulerGapDivider = getRulerGapDivider();
        double gapBetweenText = getRulerGapDivider * zoom;

        double positiveStartValue = Math.max(Math.floor(this.x / gapBetweenText) * gapBetweenText / zoom, 0);
        double positiveOffset = this.x < 0 ? -this.x : -this.x % gapBetweenText;
        for (int i = 0; i < 50; i++) {
            double textPosition = i * gapBetweenText;
            double value = Math.round((textPosition / zoom + positiveStartValue) / getRulerGapDivider) * getRulerGapDivider;
            gc.strokeText(String.valueOf((int) value), positiveOffset + textPosition, 10);
        }

        double negativeStartValue = -this.x > getWidth() ? getWidth() - (this.x + getWidth()) % gapBetweenText : -this.x;
        double negativeOffset = Math.max(Math.floor((-this.x - getWidth()) / gapBetweenText) * gapBetweenText / zoom, 0);
        for (int i = 0; i < 50; i++) {
            double textPosition = i * -gapBetweenText;
            double value = Math.round((textPosition / zoom - negativeOffset) / getRulerGapDivider) * getRulerGapDivider;
            gc.strokeText(String.valueOf((int) value), negativeStartValue + textPosition, 10);
        }
    }

    private double getRulerGapDivider() {
        double minValue = Math.ceil(this.minGapBetweenText / zoom);
        double maxValue = this.maxGapBetweenText / zoom;
        double gridSize = settings.getGridSize();
        Double[] gapDividers = new Double[]{gridSize, 500.0, 100.0, 25.0, 10.0, 5.0};
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
        double gridHeight = settings.getGridHeight();

        gc.setFill(CanvasColors.LIGHT_GRAY);
        gc.fillRect(0, 0, 20, getHeight());
        gc.setFill(CanvasColors.DARK_GRAY);
        gc.fillRect(0, -this.y + 0 * zoom, 20, gridHeight * zoom);
        gc.setStroke(CanvasColors.WHITE);
        gc.setTextAlign(TextAlignment.RIGHT);

        double getRulerGapDivider = getRulerGapDivider();
        double gapBetweenText = getRulerGapDivider * zoom;

        double positiveStartValue = Math.max(Math.floor(this.y / gapBetweenText) * gapBetweenText / zoom, 0);
        double positiveOffset = this.y < 0 ? this.y : this.y % gapBetweenText;

//        Affine original = gc.getTransform();
//        Affine affine = new Affine();
//        affine.appendRotation(-90, 0, 0);
//        System.out.println(getWidth());
        gc.setTransform(rotate);
        for (int i = 0; i < 50; i++) {
            double textPosition = i * gapBetweenText;
            double value = Math.round((textPosition / zoom + positiveStartValue) / getRulerGapDivider) * getRulerGapDivider;
            gc.strokeText(String.valueOf((int) value), positiveOffset - textPosition, 10);
        }

        double negativeStartValue = this.y > getHeight() ? getHeight() - (-this.y + getHeight()) % gapBetweenText : this.y;
        double negativeOffset = Math.max(Math.floor((this.y - getHeight()) / gapBetweenText) * gapBetweenText / zoom, 0);
        for (int i = 0; i < 50; i++) {
            double textPosition = i * -gapBetweenText;
            double value = Math.round((textPosition / zoom - negativeOffset) / getRulerGapDivider) * getRulerGapDivider;
            gc.strokeText(String.valueOf((int) value), negativeStartValue - textPosition, 10);
        }
        gc.setTransform(original);
    }

    @Override
    public void updateCanvasColors(Shape shape) {
        // This canvas should never render shapes, so if it does the color will be turned to purple
        setStrokeColor(CanvasColors.PURPLE);
        setFillColor(CanvasColors.PURPLE);
    }
}
