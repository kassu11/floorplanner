package view;

import entity.Settings;
import org.springframework.core.io.Resource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
/**
 * Settings singleton class for saving all the settings
 */
public class SettingsSingleton {
    /**
     * Is draw grid
     */
    private boolean isDrawGrid = true;
    /**
     * Units visible
     */
    private boolean unitsVisible = true;
    /**
     * Grid height
     */
    private double gridHeight = 750;
    /**
     * Grid width
     */
    private double gridWidth = 750;
    /**
     * Grid size
     */
    private int gridSize = 25;
    /**
     * Measurement unit
     */
    private String measurementUnit = "cm";
    /**
     * Measurement units
     */
    private HashMap<String, Double> measurementUnits = new HashMap<>();
    /**
     * Locale
     */
    private Locale locale = LocaleConfig.values()[0].getLocale();
    /**
     * Constructor for the settings singleton
     */
    private SettingsSingleton() {
        measurementUnits.put("mm", 10.0);
        measurementUnits.put("cm", 1.0);
        measurementUnits.put("dm", 0.1);
        measurementUnits.put("m", 0.01);
        measurementUnits.put("in", 0.393701);
        measurementUnits.put("ft", 0.0328084);
    }
    /**
     * Returns the instance
     * @return instance
     */
    public static SettingsSingleton getInstance() {
        return SettingsSingletonHelper.INSTANCE;
    }
    /**
     * Returns if the grid is enabled
     * @return if the grid is enabled
     */
    public boolean isGridEnabled() {
        return isDrawGrid;
    }
    /**
     * Sets if the grid is enabled
     * @param isDrawGrid if the grid is enabled
     */
    public void setDrawGrid(boolean isDrawGrid) {
        this.isDrawGrid = isDrawGrid;
    }
    /**
     * Returns the grid height
     * @return grid height
     */
    public double getGridHeight() {
        return gridHeight;
    }
    /**
     * Sets the grid height
     * @param gridHeight grid height
     */
    public void setGridHeight(double gridHeight) {
        this.gridHeight = gridHeight;
    }
    /**
     * Returns the grid width
     * @return grid width
     */
    public double getGridWidth() {
        return gridWidth;
    }
    /**
     * Sets the grid width
     * @param gridWidth grid width
     */
    public void setGridWidth(double gridWidth) {
        this.gridWidth = gridWidth;
    }
    /**
     * Returns the grid size
     * @return grid size
     */
    public int getGridSize() {
        return gridSize;
    }
    /**
     * Returns if the units are visible
     * @return if the units are visible
     */
    public boolean isUnitsVisible() {
        return unitsVisible;
    }
    /**
     * Sets if the units are visible
     * @param unitsVisible if the units are visible
     */
    public void setUnitsVisible(boolean unitsVisible) {
        this.unitsVisible = unitsVisible;
    }
    /**
     * Sets the grid size
     * @param gridSize grid size
     */
    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }
    /**
     * Returns the localization
     * @return localization
     */
    public ResourceBundle getLocalization() {
        return ResourceBundle.getBundle("localization", locale);
    }
    /**
     * Returns the localization string
     * @param key key
     * @return localization string
     */
    public String getLocalizationString(String key) {
        try {
            return getLocalization().getString(key);
        } catch (Exception e) {
            System.out.println("Key " + key + " not found in " + locale.getLanguage() + " locale, trying English locale");
            return ResourceBundle.getBundle("localization", LocaleConfig.ENGLISH.getLocale()).getString(key);
        }
    }
    /**
     * Returns the localization with the locale
     * @param locale locale
     * @return localization with the locale
     */
    public ResourceBundle getLocalizationWithLocale(Locale locale) {
        return ResourceBundle.getBundle("localization", locale);
    }
    /**
     * Returns the locale
     * @return locale
     */
    public Locale getLocale() {
        return locale;
    }
    /**
     * Returns all the localization
     * @return all the localization
     */
    public LocaleConfig[] getAllLocalization() {
        return LocaleConfig.values();
    }
    /**
     * Sets the locale with the locale language
     * @param language language
     */
    public void setLocaleWithLocaleLanguage(String language) {
        this.locale = Arrays.stream(LocaleConfig.values())
                .map(LocaleConfig::getLocale)
                .filter(localeConfigLocale -> localeConfigLocale.getLanguage().equals(language))
                .findFirst()
                .orElse(LocaleConfig.ENGLISH.getLocale());
    }
    /**
     * Sets the locale with the locale
     * @param locale locale
     */
    public Settings getSettings() {
        return new Settings(isDrawGrid, gridHeight, gridWidth, gridSize, locale.getLanguage(), measurementUnit);
    }
    /**
     * Sets the settings
     * @param settings settings
     */
    public void setSettings(Settings settings) {
        setDrawGrid(settings.isDrawGrid());
        setGridHeight(settings.getGridHeight());
        setGridWidth(settings.getGridWidth());
        setGridSize(settings.getGridSize());
        setLocaleWithLocaleLanguage(settings.getLocale());
        setMeasurementUnit(settings.getMeasurementUnit());
    }
    /**
     * Singleton helper class
     * @return Settings singleton
     */
    private static class SettingsSingletonHelper {
        private static final SettingsSingleton INSTANCE = new SettingsSingleton();
    }
    /**
     * Returns the measurement unit
     * @return measurement unit
     */
    public String getMeasurementUnit() {
        return measurementUnit;
    }
    /**
     * Sets the measurement unit
     * @param measurementUnit measurement unit
     */
    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }
    /**
     * Returns the measurement units
     * @return measurement units
     */

    public HashMap<String, Double> getMeasurementUnits() {
        return measurementUnits;
    }
    /**
     * Returns the measurement unit modifier
     * @return measurement unit modifier
     */

    public double getMeasurementModifier() {
        return measurementUnits.get(measurementUnit);
    }
}
