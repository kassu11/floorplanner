package view.GUIElements.canvas;

import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import model.shapes.Shape;
import view.SettingsSingleton;
/**
 * Ruler canvas class where the rulers are displayed
 */
public class RulerCanvas extends CustomCanvas {
    /**
     * Settings singleton
     */
    private final SettingsSingleton settings = SettingsSingleton.getInstance();
    /**
     * Minimum gap between text
     */
    private final double minGapBetweenText = 75;
    /**
     * Maximum gap between text
     */
    private final double maxGapBetweenText = 250;
    /**
     * Ruler font
     */
    private Font rulerFont = new Font("Arial", 11);
    /**
     * Original affine
     */
    private final Affine original = gc.getTransform();
    /**
     * Rotate affine
     */
    private final Affine rotate = new Affine();
    /**
     * Ruler height
     */
    private final double rulerHeight = 15;
    /**
     * Text padding bottom
     */
    private final double textPaddingBottom = 4;
    /**
     * Text padding left
     */
    private final double textPaddingLeft = 3;
    /**
     * The line height for line l1
     */
    private final double l1LineHeight = 12;
    /**
     * The line height for line l2
     */
    private final double l2LineHeight = 5;
    /**
     * The line height for line l3
     */
    private final double l3LineHeight = 8;
    /**
     * Constructor for the ruler canvas
     * @param width width
     * @param height height
     */
    public RulerCanvas(double width, double height) {
        super(width, height);
        setLineWidth(.5);
        gc.setFont(rulerFont);
        rotate.appendRotation(-90, 0, 0);
        gc.setImageSmoothing(false);
    }
    /**
     * Method that draws both rulers onto the sides of the drawing canvas
     */
    public void drawRuler() {
        clear();

        beginPath();
        drawHorizontalRuler();
        drawVerticalRuler();
        gc.setFill(CanvasColors.RULER_CORNER);
        gc.fillRect(0, 0, rulerHeight, rulerHeight);
    }

    /**
     * Draws the vertical ruler numbers
     */
    void drawHorizontalRuler() {
        double gridWidth = settings.getGridWidth();
        gc.setFill(CanvasColors.RULER_OUTER);
        gc.fillRect(0, 0, getWidth(), rulerHeight);
        gc.setFill(CanvasColors.RULER_INNER);
        gc.fillRect(-this.x + 0 * zoom, 0, gridWidth * zoom, rulerHeight);
        gc.setFill(CanvasColors.RULER_TEXT);
        gc.setTextAlign(TextAlignment.LEFT);

        double getRulerGapDivider = getRulerGapDivider();
        double gapBetweenText = getRulerGapDivider * zoom;
        double oneTenthGapBetweenText = gapBetweenText / 10;

        double positiveStartValue = Math.max(Math.floor(this.x / gapBetweenText) * gapBetweenText / zoom, 0);
        double positiveOffset = this.x < 0 ? -this.x : -this.x % gapBetweenText;
        for (int i = 0; i < 50; i++) {
            double textPosition = i * gapBetweenText;
            double value = Math.round((textPosition / zoom + positiveStartValue) / getRulerGapDivider) * getRulerGapDivider;
            horizontalLines(positiveOffset, textPosition, value, oneTenthGapBetweenText);
        }

        double negativeStartValue = -this.x > getWidth() ? getWidth() - (this.x + getWidth()) % gapBetweenText : -this.x;
        double negativeOffset = Math.max(Math.floor((-this.x - getWidth()) / gapBetweenText) * gapBetweenText / zoom, 0);
        for (int i = 1; i < 50; i++) {
            double textPosition = i * -gapBetweenText;
            double value = Math.round((textPosition / zoom - negativeOffset) / getRulerGapDivider) * getRulerGapDivider;
            horizontalLines(negativeStartValue, textPosition, value, oneTenthGapBetweenText);
        }
    }

    private void horizontalLines(double startValue, double textPosition, double textValue, double oneTenth) {
        for(int j = 1; j < 10; j++) {
            double height = j == 5 ? l3LineHeight : l2LineHeight;
            gc.fillRect(startValue + textPosition - j * oneTenth, rulerHeight - height, 1, height);
        }
        gc.fillRect(startValue + textPosition, rulerHeight - l1LineHeight, 1, l1LineHeight);
        gc.fillText(formatRulerValue(textValue), startValue + textPosition + textPaddingLeft, rulerHeight - textPaddingBottom);
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
    void drawVerticalRuler() {
        double gridHeight = settings.getGridHeight();

        gc.setFill(CanvasColors.LIGHT_GRAY);
        gc.fillRect(0, 0, rulerHeight, getHeight());
        gc.setFill(CanvasColors.DARK_GRAY);
        gc.fillRect(0, -this.y + 0 * zoom, rulerHeight, gridHeight * zoom);
        gc.setFill(CanvasColors.RULER_TEXT);
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.setTransform(rotate);

        double getRulerGapDivider = getRulerGapDivider();
        double gapBetweenText = getRulerGapDivider * zoom;

        double positiveStartValue = Math.max(Math.floor(this.y / gapBetweenText) * gapBetweenText / zoom, 0);
        double positiveOffset = this.y < 0 ? this.y : this.y % gapBetweenText;
        for (int i = 0; i < 50; i++) {
            double textPosition = i * gapBetweenText;
            double value = Math.round((textPosition / zoom + positiveStartValue) / getRulerGapDivider) * getRulerGapDivider;
            verticalLines(gapBetweenText, positiveOffset, textPosition, value);
        }

        double negativeStartValue = Math.max(Math.floor((-this.y - getHeight()) / gapBetweenText) * gapBetweenText / zoom, 0);
        double negativeOffset = -this.y > getHeight() ? -(getHeight() - (this.y + getHeight()) % gapBetweenText) : this.y;
        for (int i = 1; i < 50; i++) {
            double textPosition = i * -gapBetweenText;
            double value = Math.round((textPosition / zoom - negativeStartValue) / getRulerGapDivider) * getRulerGapDivider;
            verticalLines(gapBetweenText, negativeOffset, textPosition, value);
        }
        gc.setTransform(original);
    }
    /**
     * Formats the ruler value
     * @param value value
     * @return formatted value
     */
    private String formatRulerValue(double value) {
        return String.valueOf((int) value);
    }

    private void verticalLines(double gapBetweenText, double negativeOffset, double textPosition, double rulerValue) {
        double oneTenth = gapBetweenText / 10;
        for(int j = 1; j < 10; j++) {
            double height = j == 5 ? l3LineHeight : l2LineHeight;
            gc.fillRect(negativeOffset - textPosition - j * oneTenth, rulerHeight - height, 1, height);
        }
        gc.fillRect(negativeOffset - textPosition, rulerHeight - l1LineHeight, 1, l1LineHeight);
        gc.fillText(formatRulerValue(rulerValue), negativeOffset - textPosition - textPaddingLeft, rulerHeight - textPaddingBottom);
    }
    /**
     * Method to update the canvas colors, but you cannot update the colors of the ruler canvas
     * @param shape shape
     */
    @Override
    public void updateCanvasColors(Shape shape) {
        System.out.println("YOU SHOULD NOT UPDATE THE RULER CANVAS COLORS!");
    }
}
