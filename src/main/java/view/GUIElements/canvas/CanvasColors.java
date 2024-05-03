package view.GUIElements.canvas;

import javafx.scene.paint.Paint;
import view.types.ModeType;

public final class CanvasColors {
    public static final Paint LIGHT_BLUE = Paint.valueOf("#38d1ff");
    public static final Paint DARK_LIGHT_BLUE = Paint.valueOf("#00b2e8");
    public static final Paint PURPLE = Paint.valueOf("#ad03fc");
    public static final Paint RED = Paint.valueOf("#ff474e");
    public static final Paint DARK_RED = Paint.valueOf("#260001");
    public static final Paint GREEN = Paint.valueOf("#21ff19");
    public static final Paint DARK_GREEN = Paint.valueOf("#07c400");
    public static final Paint BLACK = Paint.valueOf("#000000");
    public static final Paint YELLOW = Paint.valueOf("#fcba03");
    public static Paint SELECTED = LIGHT_BLUE;
    public static Paint HOVER = LIGHT_BLUE;
    public static Paint SELECTED_HOVER = PURPLE;
    public static Paint NORMAL = BLACK;
    private CanvasColors() {
    }

    public static void updateColorsByMode(ModeType mode) {
        switch (mode) {
            case SELECT -> {
                SELECTED_HOVER = LIGHT_BLUE;
                SELECTED = LIGHT_BLUE;
                HOVER = DARK_LIGHT_BLUE;
                NORMAL = BLACK;
            }
            case DRAW -> {
                SELECTED = GREEN;
                HOVER = DARK_GREEN;
                SELECTED_HOVER = DARK_GREEN;
                NORMAL = BLACK;
            }
            case DELETE -> {
                HOVER = RED;
                NORMAL = DARK_RED;
            }
            case ROTATE -> {
                SELECTED = YELLOW;
                HOVER = YELLOW;
                SELECTED_HOVER = YELLOW;
                NORMAL = BLACK;
            }
            case AREA -> {
                SELECTED = YELLOW;
                HOVER = YELLOW;
                SELECTED_HOVER = YELLOW;
                NORMAL = BLACK;
            }
        }
    }
}
