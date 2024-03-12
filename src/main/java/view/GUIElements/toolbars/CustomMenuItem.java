package view.GUIElements.toolbars;

import javafx.scene.control.MenuItem;
import view.types.ShapeType;

public class CustomMenuItem extends MenuItem {

    private ShapeType shapeType;

    public CustomMenuItem(String text) {
        super(text);
    }

    public CustomMenuItem(String text, ShapeType shapeType) {
        super(text);
        this.shapeType = shapeType;

    }

    public ShapeType getShapeType() {
        return shapeType;
    }
}