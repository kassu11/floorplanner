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
        TextField gridWidth = new TextField();
        gridWidth.setText(String.format("%.0f", SettingsSingleton.getGridWidth()));
        TextField gridHeight = new TextField();
        gridHeight.setText(String.format("%.0f", SettingsSingleton.getGridHeight()));
        TextField gridSize = new TextField();
        gridSize.setText(String.format("%d", SettingsSingleton.getGridSize()));
        Button setGridSize = new Button("Set grid size");
        this.getItems().add(new Separator());
        this.getItems().add(gridWidth);
        this.getItems().add(new Separator());
        this.getItems().add(gridHeight);
        this.getItems().add(new Separator());
        this.getItems().add(gridSize);
        this.getItems().add(new Separator());
        this.getItems().add(setGridSize);
        setGridSize.setOnAction(e -> {
            SettingsSingleton.setGridWidth(Double.parseDouble(gridWidth.getText()));
            SettingsSingleton.setGridHeight(Double.parseDouble(gridHeight.getText()));
            SettingsSingleton.setGridSize(Integer.parseInt(gridSize.getText()));
            gc.clear();
            controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
            System.out.println("Grid size set to " + SettingsSingleton.getGridWidth() + "x" + SettingsSingleton.getGridHeight());
        });

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
