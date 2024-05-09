package view.GUIElements.toolbars;

import controller.Controller;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.GUIElements.canvas.CanvasContainer;
import view.SettingsSingleton;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

import static javafx.geometry.Pos.CENTER;

public class FileManager {
    /**
     * File manager
     */
    private SettingsSingleton settings = SettingsSingleton.getInstance();
    private model.FileManager fileManager = model.FileManager.getInstance();
    private CanvasContainer canvasContainer;
    private Controller controller;
    /**
     * Shows the file window
     */
    public FileManager(CanvasContainer canvasContainer, Controller controller) {
        this.canvasContainer = canvasContainer;
        this.controller = controller;
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
                File selectedFile = new File(fileManager.addFileFormat(fileChooser.getSelectedFile().getAbsolutePath()));
                fileManager.setCurrentFile(selectedFile);
                fileManager.exportFloorPlan();
                fileWindow.close();
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
                    canvasContainer.updateAllCanvasLayers(controller);
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
}
