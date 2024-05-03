package view.GUIElements.toolbars;

import javafx.scene.control.MenuItem;
import view.types.ShapeType;
/**
 * Custom menu item class
 */
public class CustomMenuItem extends MenuItem {
    /**
     * Shape type
     */
    private ShapeType shapeType;
    /**
     * Constructor for the custom menu item
     * @param text text
     */
    public CustomMenuItem(String text) {
        super(text);
    }
    /**
     * Constructor for the custom menu item
     * @param text text
     * @param shapeType shape type
     */
    public CustomMenuItem(String text, ShapeType shapeType) {
        super(text);
        this.shapeType = shapeType;

    }
    /**
     * Returns the shape type
     * @return shape type
     */
    public ShapeType getShapeType() {
        return shapeType;
    }
}
