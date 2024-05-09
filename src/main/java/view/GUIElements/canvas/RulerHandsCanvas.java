package view.GUIElements.canvas;

import model.shapes.Shape;

public class RulerHandsCanvas extends CustomCanvas {
    private final double rulerSize = 15;
    private final double arrowSize = 4;
    public RulerHandsCanvas(double width, double height) {
        super(width, height);
        setLineWidth(1);
        setStrokeColor(CanvasColors.RULER);
        setFillColor(CanvasColors.RULER);
    }

    /**
     * Draws the ruler hands
     * @param x - mouse x position
     * @param y - mouse y position
     */
    public void drawRulerHands(double x, double y) {
        clear();
        gc.beginPath();
        gc.moveTo(x, rulerSize - arrowSize);
        gc.lineTo(x - arrowSize, rulerSize);
        gc.lineTo(x + arrowSize, rulerSize);
        gc.lineTo(x, rulerSize - arrowSize);

        gc.moveTo( rulerSize - arrowSize, y);
        gc.lineTo(rulerSize, y - arrowSize);
        gc.lineTo(rulerSize, y + arrowSize);
        gc.lineTo(rulerSize - arrowSize, y);
        gc.fill();
//        gc.strokeLine(0, y, 20, y); // Draws a horizontal line at y
//        gc.strokeLine(x, 0, x, 20); // Draws a vertical line at x
    }

    @Override
    public void updateCanvasColors(Shape shape) {
        System.out.println("YOU SHOULD NOT UPDATE THE RULER HANDS CANVAS COLORS!");
    }
}
