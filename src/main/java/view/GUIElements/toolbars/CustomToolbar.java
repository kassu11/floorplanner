package view.GUIElements.toolbars;

import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.stage.Stage;

import java.util.HashMap;

abstract class CustomToolbar extends ToolBar {

    private double cursorX, cursorY;
    private Stage stage;
    private HashMap<String, Button> buttons = new HashMap<>();

    public CustomToolbar(Stage stage) {
        super();
        this.stage = stage;
    }

    public void addButton(Button button, String key) {
        this.getItems().add(button);
        this.getItems().add(new javafx.scene.control.Separator());
        buttons.put(key, button);
    }

    public void setCursorCoordinates() {
        this.cursorX = stage.getX() + this.getLayoutX();
        this.cursorY = stage.getY() + this.getLayoutY();
    }

    public double getCursorX() {
        return cursorX;
    }

    public void setCursorX(double cursorX) {
        this.cursorX = cursorX;
    }

    public double getCursorY() {
        return cursorY;
    }

    public void setCursorY(double cursorY) {
        this.cursorY = cursorY;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public HashMap<String, Button> getButtons() {
        return buttons;
    }

    public void setButtons(HashMap<String, Button> buttons) {
        this.buttons = buttons;
    }

    public Button getButton(String key) {
        return buttons.get(key);
    }
}
