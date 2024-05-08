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
    private final double rulerHeight = 15;
    private final double textPaddingBottom = 4;
    private final double textPaddingLeft = 3;
    private final double l1LineHeight = 10;
    private final double l2LineHeight = 3;
    private final double l3LineHeight = 6;
    public GridCanvas(double width, double height) {
        super(width, height);
        setLineWidth(.5);
        gc.setFont(rulerFont);
        rotate.appendRotation(-90, 0, 0);
        gc.setImageSmoothing(false);
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
            gc.fillRect(0, 0, rulerHeight, rulerHeight);
        }

    }

    /**
     * Draws the vertical ruler numbers
     */
    void drawRulerX() {
        double gridWidth = settings.getGridWidth();
        gc.setFill(CanvasColors.LIGHT_GRAY);
        gc.fillRect(0, 0, getWidth(), rulerHeight);
        gc.setFill(CanvasColors.DARK_GRAY);
        gc.fillRect(-this.x + 0 * zoom, 0, gridWidth * zoom, rulerHeight);
        gc.setStroke(CanvasColors.WHITE);
        gc.setFill(CanvasColors.WHITE);
        gc.setTextAlign(TextAlignment.LEFT);

        double getRulerGapDivider = getRulerGapDivider();
        double gapBetweenText = getRulerGapDivider * zoom;

        double positiveStartValue = Math.max(Math.floor(this.x / gapBetweenText) * gapBetweenText / zoom, 0);
        double positiveOffset = this.x < 0 ? -this.x : -this.x % gapBetweenText;
        for (int i = 0; i < 50; i++) {
            double textPosition = i * gapBetweenText;
            double value = Math.round((textPosition / zoom + positiveStartValue) / getRulerGapDivider) * getRulerGapDivider;
            double oneTenth = gapBetweenText / 10;
            for(int j = 1; j < 10; j++) {
                double height = j == 5 ? l3LineHeight : l2LineHeight;
                gc.fillRect(positiveOffset + textPosition - j * oneTenth, rulerHeight - height, 1, height);
            }
//            gc.fillRect(positiveOffset + textPosition - 5 * oneTenth, rulerHeight - l3LineHeight, 1, l3LineHeight);
            gc.fillRect(positiveOffset + textPosition, rulerHeight - l1LineHeight, 1, l1LineHeight);
            gc.strokeText(String.valueOf((int) value), positiveOffset + textPosition + textPaddingLeft, rulerHeight - textPaddingBottom);
        }

        double negativeStartValue = -this.x > getWidth() ? getWidth() - (this.x + getWidth()) % gapBetweenText : -this.x;
        double negativeOffset = Math.max(Math.floor((-this.x - getWidth()) / gapBetweenText) * gapBetweenText / zoom, 0);
        for (int i = 1; i < 50; i++) {
            double textPosition = i * -gapBetweenText;
            double value = Math.round((textPosition / zoom - negativeOffset) / getRulerGapDivider) * getRulerGapDivider;
            double oneTenth = gapBetweenText / 10;
            for(int j = 1; j < 10; j++) {
                double height = j == 5 ? l3LineHeight : l2LineHeight;
                gc.fillRect(negativeStartValue + textPosition - j * oneTenth, height, 1, height);
            }
            gc.fillRect(negativeStartValue + textPosition, rulerHeight - l1LineHeight, 1, l1LineHeight);
            gc.strokeText(String.valueOf((int) value), negativeStartValue + textPosition + textPaddingLeft, rulerHeight - textPaddingBottom);
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
        gc.fillRect(0, 0, rulerHeight, getHeight());
        gc.setFill(CanvasColors.DARK_GRAY);
        gc.fillRect(0, -this.y + 0 * zoom, rulerHeight, gridHeight * zoom);
        gc.setStroke(CanvasColors.WHITE);
        gc.setFill(CanvasColors.WHITE);
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.setTransform(rotate);

        double getRulerGapDivider = getRulerGapDivider();
        double gapBetweenText = getRulerGapDivider * zoom;

        double positiveStartValue = Math.max(Math.floor(this.y / gapBetweenText) * gapBetweenText / zoom, 0);
        double positiveOffset = this.y < 0 ? this.y : this.y % gapBetweenText;
        for (int i = 0; i < 50; i++) {
            double textPosition = i * gapBetweenText;
            double value = Math.round((textPosition / zoom + positiveStartValue) / getRulerGapDivider) * getRulerGapDivider;
            double oneTenth = gapBetweenText / 10;
            for(int j = 1; j < 10; j++) {
                double height = j == 5 ? l3LineHeight : l2LineHeight;
                gc.fillRect(positiveOffset - textPosition - j * oneTenth, rulerHeight - height, 1, height);
            }
            gc.fillRect(positiveOffset - textPosition, rulerHeight - l1LineHeight, 1, l1LineHeight);
            gc.strokeText(String.valueOf((int) value), positiveOffset - textPosition - textPaddingLeft, rulerHeight - textPaddingBottom);
        }

        double negativeStartValue = Math.max(Math.floor((-this.y - getHeight()) / gapBetweenText) * gapBetweenText / zoom, 0);
        double negativeOffset = -this.y > getHeight() ? getHeight() - (this.y + getHeight()) % gapBetweenText : -this.y;
        for (int i = 1; i < 50; i++) {
            double textPosition = i * -gapBetweenText;
            double value = Math.round((textPosition / zoom - negativeStartValue) / getRulerGapDivider) * getRulerGapDivider;
            double oneTenth = gapBetweenText / 10;
            for(int j = 1; j < 10; j++) {
                double height = j == 5 ? l3LineHeight : l2LineHeight;
                gc.fillRect(-negativeOffset - textPosition - j * oneTenth, rulerHeight - height, 1, height);
            }
            gc.fillRect(-negativeOffset - textPosition, rulerHeight - l1LineHeight, 1, l1LineHeight);
            gc.strokeText(String.format("- %d", (int) Math.abs(value)), -negativeOffset - textPosition - textPaddingLeft, rulerHeight - textPaddingBottom);
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
