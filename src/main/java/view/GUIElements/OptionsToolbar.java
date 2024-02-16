package view.GUIElements;

import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;

public class OptionsToolbar extends ToolBar {

    public OptionsToolbar() {
        super();
        this.setOrientation(Orientation.HORIZONTAL);
        addButton(new Button("File"));
        addButton(new Button("Settings"));
    }

    public void addButton(Button button) {
        this.getItems().add(button);
        this.getItems().add(new javafx.scene.control.Separator());
    }
}
