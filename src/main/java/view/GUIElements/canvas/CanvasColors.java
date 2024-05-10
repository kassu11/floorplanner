package view.GUIElements.canvas;

import javafx.scene.paint.Paint;
import view.types.ModeType;

public final class CanvasColors {
    /**
     * Light blue color
     */
    public static final Paint LIGHT_BLUE = Paint.valueOf("#38d1ff");
    /**
     * Dark light blue color
     */
    public static final Paint DARK_LIGHT_BLUE = Paint.valueOf("#00b2e8");
    /**
     * Purple color
     */
    public static final Paint PURPLE = Paint.valueOf("#ad03fc");
    /**
     * Red color
     */
    public static final Paint RED = Paint.valueOf("#ff474e");
    /**
     * Dark red color
     */
    public static final Paint DARK_RED = Paint.valueOf("#260001");
    /**
     * Green color
     */
    public static final Paint GREEN = Paint.valueOf("#21ff19");
    /**
     * Dark green color
     */
    public static final Paint DARK_GREEN = Paint.valueOf("#07c400");
    /**
     * Black color
     */
    public static final Paint BLACK = Paint.valueOf("#000000");
    /**
     * Yellow color
     */
    public static final Paint YELLOW = Paint.valueOf("#fcba03");
    /**
     * Gray color
     */
    public static final Paint GRAY = Paint.valueOf("#dbdbdb");
    /**
     * Light gray color
     */
    public static final Paint LIGHT_GRAY = Paint.valueOf("#353535");
    /**
     * Dark gray color
     */
    public static final Paint DARK_GRAY = Paint.valueOf("#1d1e1e");
    /**
     * White color
     */
    public static final Paint WHITE = Paint.valueOf("#ffffff");
    /**
     * Ruler text color
     */
    public static final Paint RULER_TEXT = WHITE;
    /**
     * Ruler corner color
     */
    public static final Paint RULER_CORNER = BLACK;
    /**
     * Ruler outer color
     */
    public static final Paint RULER_OUTER = LIGHT_GRAY;
    /**
     * Ruler inner color
     */
    public static final Paint RULER_INNER = BLACK;
    /**
     * Grid background color
     */
    public static final Paint GRID_BACKGROUND = WHITE;
    /**
     * Grid line color
     */
    public static final Paint GRID_LINE = GRAY;
    /**
     * Ruler color
     */
    public static final Paint RULER = WHITE;
    /**
     * Selected color
     */
    public static Paint SELECTED = LIGHT_BLUE;
    /**
     * Hover color
     */
    public static Paint HOVER = LIGHT_BLUE;
    /**
     * Selected hover color
     */
    public static Paint SELECTED_HOVER = PURPLE;
    /**
     * Final normal color
     */
    public static Paint FINAL_NORMAL = BLACK;
    /**
     * Preview normal color
     */
    public static Paint PREVIEW_NORMAL = BLACK;
    private CanvasColors() {
    }
    /**
     * Updates the colors based on the given mode
     * @param mode mode
     */
    public static void updateColorsByMode(ModeType mode) {
        SELECTED = SELECTED_HOVER = LIGHT_BLUE;
        FINAL_NORMAL = PREVIEW_NORMAL = HOVER = BLACK;
        switch (mode) {
            case SELECT -> HOVER = PREVIEW_NORMAL = LIGHT_BLUE;
            case DRAW -> PREVIEW_NORMAL = GREEN;
            case DELETE -> {
                HOVER = PREVIEW_NORMAL = RED;
                FINAL_NORMAL = DARK_RED;
            }
            case ROTATE -> HOVER = SELECTED_HOVER = SELECTED = PREVIEW_NORMAL = YELLOW;
            case AREA -> {
                SELECTED = RED;
                HOVER = PURPLE;
                SELECTED_HOVER = PURPLE;
                PREVIEW_NORMAL = PURPLE;
            }
            default -> {
            }
        }
    }
}
