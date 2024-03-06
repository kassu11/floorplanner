package view.GUIElements;

import controller.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.SettingsSingleton;

public class OptionsToolbar extends CustomToolbar {

    private final Controller controller;
    private CustomCanvas gc;

    public OptionsToolbar(Stage stage, Controller controller, CustomCanvas gc) {
        super(stage);
        this.controller = controller;
        this.gc = gc;
        this.setOrientation(Orientation.HORIZONTAL);
        addButton(new Button("File"));
        addButton(new Button("Settings"));
    }

    public void showSettings() {
        Stage settingsWindow = new Stage();

        settingsWindow.setTitle("Settings");
        CheckBox showLengths = new CheckBox("Show lengths");
        showLengths.setSelected(SettingsSingleton.isDrawLengths());
        CheckBox showAreas = new CheckBox("Show areas");
        Label shapeLabel = new Label("Shape settings");
        ComboBox<String> languageSettings = new ComboBox<>();
        Label otherSettingsLabel = new Label("Other settings");
        Button saveButton = new Button("Save");

        saveButton.setOnAction(e -> {
            SettingsSingleton.setDrawLengths(showLengths.isSelected());
            controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
            settingsWindow.close();
        });

        languageSettings.getItems().addAll("ENG", "FIN", "JPN");

        Insets defaultInsets = new Insets(10, 10, 10, 10);

        HBox languageSettingsLayout = new HBox(new Label("Language"), languageSettings);
        languageSettingsLayout.setPadding(defaultInsets);
        languageSettingsLayout.setSpacing(10);

        VBox shapeSettingsLayout = new VBox(shapeLabel ,showLengths, showAreas, saveButton);
        shapeSettingsLayout.setPadding(defaultInsets);
        shapeSettingsLayout.setSpacing(10);

        VBox otherSettingsLayout = new VBox(otherSettingsLabel ,languageSettingsLayout);
        otherSettingsLayout.setPadding(defaultInsets);
        otherSettingsLayout.setSpacing(10);

        HBox settingsLayout = new HBox(shapeSettingsLayout, otherSettingsLayout);
        settingsLayout.setPadding(defaultInsets);
        settingsLayout.setSpacing(30);

        Scene settingsScene = new Scene(settingsLayout, 400, 400);

        settingsWindow.setScene(settingsScene);
        settingsWindow.show();
    }

}
