package view.GUIElements.toolbars;

import controller.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.FileManager;
import view.GUIElements.canvas.CustomCanvas;
import view.SettingsSingleton;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;
import java.util.Locale;
import java.util.Set;

import static javafx.geometry.Pos.CENTER;

public class OptionsToolbar extends CustomToolbar {

    private final Controller controller;
    private CustomCanvas gc;
    private SettingsSingleton settings = SettingsSingleton.getInstance();
    private FileManager fileManager = FileManager.getInstance();

    public OptionsToolbar(Stage stage, Controller controller, CustomCanvas gc) {
        super(stage);
        this.controller = controller;
        this.gc = gc;
        this.setOrientation(Orientation.HORIZONTAL);
        addButton(new Button(settings.getLocalizationString("file")), "file");
        addButton(new Button(settings.getLocalizationString("settings")), "settings");
        TextField gridWidth = new TextField();
        gridWidth.setText(String.format("%.0f", SettingsSingleton.getGridWidth()));
        TextField gridHeight = new TextField();
        gridHeight.setText(String.format("%.0f", SettingsSingleton.getGridHeight()));
        TextField gridSize = new TextField();
        gridSize.setText(String.format("%d", SettingsSingleton.getGridSize()));
        addButton(new Button(settings.getLocalizationString("gridSize")), "gridSize");
        this.getItems().add(new Separator());
        this.getItems().add(gridWidth);
        this.getItems().add(new Separator());
        this.getItems().add(gridHeight);
        this.getItems().add(new Separator());
        this.getItems().add(gridSize);
        this.getItems().add(new Separator());
        getButton("gridSize").setOnAction(e -> {
            SettingsSingleton.setGridWidth(Double.parseDouble(gridWidth.getText()));
            SettingsSingleton.setGridHeight(Double.parseDouble(gridHeight.getText()));
            SettingsSingleton.setGridSize(Integer.parseInt(gridSize.getText()));
            gc.clear();
            controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
            System.out.println("Grid size set to " + SettingsSingleton.getGridWidth() + "x" + SettingsSingleton.getGridHeight());
        });

    }

    public void showFile() {
        Stage fileWindow = new Stage();

        fileWindow.setTitle("File");
        Button exportButton = new Button(settings.getLocalizationString("saveFloorplan"));
        Button importButton = new Button(settings.getLocalizationString("loadFloorplan"));

        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Filter all ser files", "ser");
        fileChooser.setDialogTitle("File Explorer");
        fileChooser.setFileFilter(filter);
        fileChooser.setCurrentDirectory(new File("./src/main/resources/"));

        exportButton.setOnAction(e -> {
            int response = fileChooser.showSaveDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                File selectedFile = new File(fileChooser.getSelectedFile().getAbsolutePath());
                if (fileManager.checkFileFormat(selectedFile.getName())) {
                    fileManager.setCurrentFile(selectedFile);
                    fileManager.exportFloorPlan();
                    fileWindow.close();
                }
            }
        });

        importButton.setOnAction(e -> {
            int response = fileChooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                File selectedFile = new File(fileChooser.getSelectedFile().getAbsolutePath());
                if (fileManager.checkFileFormat(selectedFile.getName())) {
                    fileManager.setCurrentFile(selectedFile);
                    fileManager.importFloorPlan();
                    fileWindow.close();
                    controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
                }
            }
        });

        Insets defaultInsets = new Insets(10, 10, 10, 10);

        VBox fileLayout = new VBox(exportButton, importButton);
        fileLayout.setPadding(defaultInsets);
        fileLayout.setSpacing(10);
        fileLayout.setAlignment(CENTER);

        Scene fileScene = new Scene(fileLayout, 200, 100);

        fileWindow.setScene(fileScene);
        fileWindow.show();
    }

    public void showSettings() {
        Stage settingsWindow = new Stage();

        settingsWindow.setTitle(settings.getLocalizationString("settings"));
        CheckBox showLengths = new CheckBox(settings.getLocalizationString("showLengths"));
        showLengths.setSelected(SettingsSingleton.isDrawLengths());
        CheckBox showAreas = new CheckBox(settings.getLocalizationString("showAreas"));
        Label shapeLabel = new Label(settings.getLocalizationString("shapeSettings"));
        ComboBox<String> languageSettings = new ComboBox<>();
        languageSettings.setValue(SettingsSingleton.getLocaleSimpleName());
        Label otherSettingsLabel = new Label(settings.getLocalizationString("otherSettings"));
        CheckBox showGrid = new CheckBox(settings.getLocalizationString("showGrid"));
        showGrid.setSelected(SettingsSingleton.isGridEnabled());
        Button saveButton = new Button(settings.getLocalizationString("save"));

        saveButton.setOnAction(e -> {
            SettingsSingleton.setDrawLengths(showLengths.isSelected());
            SettingsSingleton.setDrawGrid(showGrid.isSelected());
            SettingsSingleton.setLocale(getLocale(languageSettings.getValue()));
            controller.drawAllShapes(gc, Controller.SingletonType.FINAL);
            controller.saveSettings();
            controller.updateToolbarLocalization();
            settingsWindow.close();
        });

        languageSettings.getItems().addAll("ENG", "FIN", "JPN");

        Insets defaultInsets = new Insets(10, 10, 10, 10);

        HBox languageSettingsLayout = new HBox(new Label(settings.getLocalizationString("language")), languageSettings);
        languageSettingsLayout.setSpacing(10);

        VBox shapeSettingsLayout = new VBox(shapeLabel, showLengths, showAreas, showGrid, saveButton);
        shapeSettingsLayout.setPadding(defaultInsets);
        shapeSettingsLayout.setSpacing(10);

        VBox otherSettingsLayout = new VBox(otherSettingsLabel, languageSettingsLayout);
        otherSettingsLayout.setPadding(defaultInsets);
        otherSettingsLayout.setSpacing(10);

        HBox settingsLayout = new HBox(shapeSettingsLayout, otherSettingsLayout);
        settingsLayout.setPadding(defaultInsets);
        settingsLayout.setSpacing(30);

        Scene settingsScene = new Scene(settingsLayout, 400, 400);

        settingsWindow.setScene(settingsScene);
        settingsWindow.show();
    }

    private Locale getLocale(String language) {
        switch (language) {
            case "ENG":
                return new Locale("en", "US");
            case "FIN":
                return new Locale("fi", "FI");
            case "JPN":
                return new Locale("ja", "JP");
            default:
                return new Locale("en", "US");
        }
    }

    public void updateLocalization(){
        for(String key : getButtons().keySet()){
            getButtons().get(key).setText(settings.getLocalizationString(key));
        }
    }
}
