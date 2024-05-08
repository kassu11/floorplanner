package view.GUIElements.canvas;

import model.shapes.Shape;

public class RulerHandsCanvas extends CustomCanvas {
    public RulerHandsCanvas(double width, double height) {
        super(width, height);
        setLineWidth(1);
        setStrokeColor(CanvasColors.RULER);
    }

    /**
     * Draws the ruler hands
     * @param x - mouse x position
     * @param y - mouse y position
     */
    public void drawRulerHands(double x, double y) {
        clear();
        gc.strokeLine(0, y, 20, y); // Draws a horizontal line at y
        gc.strokeLine(x, 0, x, 20); // Draws a vertical line at x
    }

    @Override
    public void updateCanvasColors(Shape shape) {
        System.out.println("YOU SHOULD NOT UPDATE THE RULER HANDS CANVAS COLORS!");
    }
}
