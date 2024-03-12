package entity;

import jakarta.persistence.*;

@Entity
public class Settings {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private boolean isDrawLengths;

    private boolean isDrawGrid;

    private double gridHeight;

    private double gridWidth;

    private int gridSize;

    public Settings(boolean isDrawLengths, boolean isDrawGrid, double gridHeight, double gridWidth, int gridSize) {
        this.isDrawLengths = isDrawLengths;
        this.isDrawGrid = isDrawGrid;
        this.gridHeight = gridHeight;
        this.gridWidth = gridWidth;
        this.gridSize = gridSize;
    }

    public Settings() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDrawLengths() {
        return isDrawLengths;
    }

    public void setDrawLengths(boolean drawLengths) {
        isDrawLengths = drawLengths;
    }

    public boolean isDrawGrid() {
        return isDrawGrid;
    }

    public void setDrawGrid(boolean drawGrid) {
        isDrawGrid = drawGrid;
    }

    public double getGridHeight() {
        return gridHeight;
    }

    public void setGridHeight(double gridHeight) {
        this.gridHeight = gridHeight;
    }

    public double getGridWidth() {
        return gridWidth;
    }

    public void setGridWidth(double gridWidth) {
        this.gridWidth = gridWidth;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }
}
