package model;

import model.shapeContainers.FinalShapesSingleton;
import model.shapes.Shape;

import java.io.*;
import java.util.List;

public class FileManager {

    private File currentFile;

    FinalShapesSingleton finalShapesSingleton = FinalShapesSingleton.getInstance();


    private FileManager() {
    }

    private static class FileManagerSingletonHelper {
        private static final FileManager INSTANCE = new FileManager();
    }

    public static FileManager getInstance() {
        return FileManager.FileManagerSingletonHelper.INSTANCE;
    }

    public void setCurrentFile(File currentFile) {
        if (currentFile == null) throw new NullPointerException("File is null");
        this.currentFile = currentFile;
    }

    public boolean checkFileFormat(String file) {
        String lastFour = file.substring(file.length() - 4);
        return lastFour.equals(".ser");
    }

    public String addFileFormat(String file) {
        if (checkFileFormat(file)) return file;
        return file + ".ser";
    }

    public void exportFloorPlan() {
        try (FileOutputStream fileOut = new FileOutputStream(currentFile);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)){
            objectOut.writeObject(finalShapesSingleton.getShapes());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void importFloorPlan() {
        try (FileInputStream fileIn = new FileInputStream(currentFile);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)){
            List<Shape> shapes = (List<Shape>) objectIn.readObject();
            finalShapesSingleton.clearShapes();
            finalShapesSingleton.addAllShapes(shapes);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
