package entity;

import jakarta.persistence.*;
/**
 * Entity class for Settings
 */
@Entity
public class Settings {
    /**
     * Id of the settings
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    /**
     * Boolean value for drawing grid
     */
    private boolean isDrawGrid;
    /**
     * Height of the grid
     */
    private double gridHeight;
    /**
     * Width of the grid
     */
    private double gridWidth;
    /**
     * Size of the grid
     */
    private int gridSize;
    /**
     * Locale
     */
    private String locale;
    /**
     * Measurement unit
     */
    private String measurementUnit;
    /**
     * Constructor for settings
     * @param isDrawGrid boolean value for drawing grid
     * @param gridHeight height of the grid
     * @param gridWidth width of the grid
     * @param gridSize size of the grid
     * @param locale locale
     * @param measurementUnit measurement unit
     */
    public Settings(boolean isDrawGrid, double gridHeight, double gridWidth, int gridSize, String locale, String measurementUnit) {
        this.isDrawGrid = isDrawGrid;
        this.gridHeight = gridHeight;
        this.gridWidth = gridWidth;
        this.gridSize = gridSize;
        this.locale = locale;
        this.measurementUnit = measurementUnit;
    }
    /**
     * Constructor for settings
     */
    public Settings() {

    }
    /**
     * Getter for id
     * @return id
     */
    public int getId() {
        return id;
    }
    /**
     * Setter for id
     * @param id id
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Getter for isDrawGrid
     * @return isDrawGrid
     */
    public boolean isDrawGrid() {
        return isDrawGrid;
    }
    /**
     * Setter for isDrawGrid
     * @param drawGrid isDrawGrid
     */
    public void setDrawGrid(boolean drawGrid) {
        isDrawGrid = drawGrid;
    }
    /**
     * Getter for gridHeight
     * @return gridHeight
     */
    public double getGridHeight() {
        return gridHeight;
    }
    /**
     * Setter for gridHeight
     * @param gridHeight gridHeight
     */
    public void setGridHeight(double gridHeight) {
        this.gridHeight = gridHeight;
    }
    /**
     * Getter for gridWidth
     * @return gridWidth
     */
    public double getGridWidth() {
        return gridWidth;
    }
    /**
     * Setter for gridWidth
     * @param gridWidth gridWidth
     */
    public void setGridWidth(double gridWidth) {
        this.gridWidth = gridWidth;
    }
    /**
     * Getter for gridSize
     * @return gridSize
     */
    public int getGridSize() {
        return gridSize;
    }
    /**
     * Setter for gridSize
     * @param gridSize gridSize
     */
    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }
    /**
     * Getter for locale
     * @return locale
     */
    public String getLocale() {
        return locale;
    }
    /**
     * Setter for locale
     * @param locale locale
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }
    /**
     * Getter for measurementUnit
     * @return measurementUnit
     */
    public void setSettings(Settings settings) {
        this.isDrawGrid = settings.isDrawGrid;
        this.gridHeight = settings.gridHeight;
        this.gridWidth = settings.gridWidth;
        this.gridSize = settings.gridSize;
        this.locale = settings.locale;
        this.measurementUnit = settings.measurementUnit;
    }
    /**
     * Setter for measurementUnit
     * @param measurementUnit measurementUnit
     */
    public String getMeasurementUnit() {
        return measurementUnit;
    }
    /**
     * Setter for measurementUnit
     * @param measurementUnit measurementUnit
     */
    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }
}
