package view;

import entity.Settings;
import org.springframework.core.io.Resource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

public class SettingsSingleton {
    private boolean isDrawGrid = true;
    private boolean unitsVisible = true;
    private double gridHeight = 750;
    private double gridWidth = 750;
    private int gridSize = 25;
    private String measurementUnit = "cm";
    private HashMap<String, Double> measurementUnits = new HashMap<>();
    private Locale locale = LocaleConfig.values()[0].getLocale();

    private SettingsSingleton() {
        measurementUnits.put("mm", 10.0);
        measurementUnits.put("cm", 1.0);
        measurementUnits.put("dm", 0.1);
        measurementUnits.put("m", 0.01);
        measurementUnits.put("in", 0.393701);
        measurementUnits.put("ft", 0.0328084);
    }

    public static SettingsSingleton getInstance() {
        return SettingsSingletonHelper.INSTANCE;
    }

    public boolean isGridEnabled() {
        return isDrawGrid;
    }

    public void setDrawGrid(boolean isDrawGrid) {
        this.isDrawGrid = isDrawGrid;
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

    public boolean isUnitsVisible() {
        return unitsVisible;
    }

    public void setUnitsVisible(boolean unitsVisible) {
        this.unitsVisible = unitsVisible;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public ResourceBundle getLocalization() {
        return ResourceBundle.getBundle("localization", locale);
    }

    public String getLocalizationString(String key) {
        try {
            return getLocalization().getString(key);
        } catch (Exception e) {
            System.out.println("Key " + key + " not found in " + locale.getLanguage() + " locale, trying English locale");
            return ResourceBundle.getBundle("localization", LocaleConfig.ENGLISH.getLocale()).getString(key);
        }
    }

    public ResourceBundle getLocalizationWithLocale(Locale locale) {
        return ResourceBundle.getBundle("localization", locale);
    }

    public Locale getLocale() {
        return locale;
    }

    public LocaleConfig[] getAllLocalization() {
        return LocaleConfig.values();
    }

    public void setLocaleWithLocaleLanguage(String language) {
        this.locale = Arrays.stream(LocaleConfig.values())
                .map(LocaleConfig::getLocale)
                .filter(localeConfigLocale -> localeConfigLocale.getLanguage().equals(language))
                .findFirst()
                .orElse(LocaleConfig.ENGLISH.getLocale());
    }

    public Settings getSettings() {
        return new Settings(isDrawGrid, gridHeight, gridWidth, gridSize, locale.getLanguage(), measurementUnit);
    }

    public void setSettings(Settings settings) {
        setDrawGrid(settings.isDrawGrid());
        setGridHeight(settings.getGridHeight());
        setGridWidth(settings.getGridWidth());
        setGridSize(settings.getGridSize());
        setLocaleWithLocaleLanguage(settings.getLocale());
        setMeasurementUnit(settings.getMeasurementUnit());
    }

    private static class SettingsSingletonHelper {
        private static final SettingsSingleton INSTANCE = new SettingsSingleton();
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public HashMap<String, Double> getMeasurementUnits() {
        return measurementUnits;
    }

    public double getMeasurementModifier() {
        return measurementUnits.get(measurementUnit);
    }
}
