package view.GUIElements.canvas;

import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import model.shapes.Shape;
import view.SettingsSingleton;
/**
 * Grid canvas class where the grid is displayed
 */
public class GridCanvas extends CustomCanvas {
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
    private final double l1LineHeight = 10;
    /**
     * The line height for line l2
     */
    private final double l2LineHeight = 3;
    /**
     * The line height for line l3
     */
    private final double l3LineHeight = 6;

    /**
     * Constructor for the grid canvas
     * @param width
     * @param height
     */
    public GridCanvas(double width, double height) {
        super(width, height);
        setLineWidth(1);
        gc.setStroke(CanvasColors.GRID_LINE);
        gc.setImageSmoothing(false);
    }
    /**
     * Draws the grid onto the grid canvas
     */
    public void drawGrid() {
        clear();
        gc.setFill(CanvasColors.GRID_BACKGROUND);
        double width = settings.getGridWidth();
        double height = settings.getGridHeight();
        fillRect(0, 0, width, height);
        if (settings.isGridEnabled()) {
            beginPath();
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
    }
    /**
     * Method to update the canvas colors, but you cannot update the grid canvas colors
     */
    @Override
    public void updateCanvasColors(Shape shape) {
        System.out.println("YOU SHOULD NOT UPDATE THE GRID CANVAS COLORS!");
    }
}
