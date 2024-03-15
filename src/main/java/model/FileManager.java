package model;

import model.shapeContainers.FinalShapesSingleton;
import model.shapes.Shape;

import java.io.*;
import java.util.ArrayList;
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
        if (currentFile == null) throw new RuntimeException("File is null");
        this.currentFile = currentFile;
    }

    public boolean checkFileFormat(String file) {
        String lastFour = file.substring(file.length() - 4);
        if (lastFour.equals(".ser")){
            System.out.println("Correct file format");
            return true;
        }
        System.out.println("Wrong file format");
        return false;
    }

    public void exportFloorPlan() {
        try {
            FileOutputStream fileOut = new FileOutputStream(currentFile);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(finalShapesSingleton.getShapes());
            objectOut.close();
            fileOut.close();
            System.out.println("The Floor plan was successfully exported to " + currentFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void importFloorPlan() {
        try {
            FileInputStream fileIn = new FileInputStream(currentFile);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            List<Shape> shapes = (List<Shape>) objectIn.readObject();
            finalShapesSingleton.clearShapes();
            finalShapesSingleton.addAllShapes(shapes);
            objectIn.close();
            fileIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<File> getFiles() {
        List<File> files = new ArrayList<>();
        File folder = new File("./src/main/resources/");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                files.add(file);
            }
            System.out.println(file.getName());
        }
        return files;
    }

}
