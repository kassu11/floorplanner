package view.GUIElements;

import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.stage.Stage;

public class OptionsToolbar extends CustomToolbar {

    public OptionsToolbar(Stage stage) {
        super(stage);
        this.setOrientation(Orientation.HORIZONTAL);
        addButton(new Button("File"));
        addButton(new Button("Settings"));
    }

    public void showSettings() {
        Stage settingsWindow = new Stage();
        settingsWindow.setTitle("Settings");
        settingsWindow.show();
    }
}
