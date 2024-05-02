package view.GUIElements.toolbars;

import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.stage.Stage;

import java.util.HashMap;
/**
 * Abstract Custom toolbar class
 */
abstract class CustomToolbar extends ToolBar {
    /**
     * Cursor x coordinate
     * Cursor y coordinate
     */
    private double cursorX, cursorY;
    /**
     * Stage
     */
    private Stage stage;
    /**
     * List of buttons
     */
    private HashMap<String, Button> buttons = new HashMap<>();
    /**
     * Constructor for the custom toolbar
     * @param stage stage
     */
    public CustomToolbar(Stage stage) {
        super();
        this.stage = stage;
    }
    /**
     * Adds a button to the toolbar
     * @param button button
     * @param key key
     */
    public void addButton(Button button, String key) {
        this.getItems().add(button);
        this.getItems().add(new javafx.scene.control.Separator());
        buttons.put(key, button);
    }
    /**
     * Sets the cursor coordinates
     */
    public void setCursorCoordinates() {
        this.cursorX = stage.getX() + this.getLayoutX();
        this.cursorY = stage.getY() + this.getLayoutY();
    }
    /**
     * Returns the cursor x coordinate
     * @return cursor x coordinate
     */
    public double getCursorX() {
        return cursorX;
    }
    /**
     * Sets the cursor x coordinate
     * @param cursorX cursor x coordinate
     */
    public void setCursorX(double cursorX) {
        this.cursorX = cursorX;
    }
    /**
     * Returns the cursor y coordinate
     * @return cursor y coordinate
     */
    public double getCursorY() {
        return cursorY;
    }
    /**
     * Sets the cursor y coordinate
     * @param cursorY cursor y coordinate
     */
    public void setCursorY(double cursorY) {
        this.cursorY = cursorY;
    }
    /**
     * Returns the stage
     * @return stage
     */
    public Stage getStage() {
        return stage;
    }
    /**
     * Sets the stage
     * @param stage stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    /**
     * Returns the list of buttons
     * @return list of buttons
     */
    public HashMap<String, Button> getButtons() {
        return buttons;
    }
    /**
     * Sets the list of buttons
     * @param buttons list of buttons
     */
    public void setButtons(HashMap<String, Button> buttons) {
        this.buttons = buttons;
    }
    /**
     * Returns the button
     * @param key key
     * @return button
     */
    public Button getButton(String key) {
        return buttons.get(key);
    }
}
