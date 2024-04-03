package view;

import entity.Settings;
import org.springframework.core.io.Resource;

import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

public class SettingsSingleton {
    private boolean drawLengths = true;
    private boolean isDrawGrid = true;
    private boolean unitsVisible = true;
    private double gridHeight = 750;
    private double gridWidth = 750;
    private int gridSize = 25;
    private Locale locale = LocaleConfig.values()[0].getLocale();

    private SettingsSingleton() {
    }

    public static SettingsSingleton getInstance() {
        return SettingsSingletonHelper.INSTANCE;
    }

    public boolean isDrawLengths() {
        return drawLengths;
    }

    public void setDrawLengths(boolean drawLengths) {
        this.drawLengths = drawLengths;
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
        SettingsSingleton.unitsVisible = unitsVisible;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public ResourceBundle getLocalization() {
        return ResourceBundle.getBundle("localization", locale);
    }

    public String getLocalizationString(String key) {
        return getLocalization().getString(key);
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
        return new Settings(drawLengths, isDrawGrid, gridHeight, gridWidth, gridSize, locale.getLanguage());
    }

    public void setSettings(Settings settings) {
        setDrawLengths(settings.isDrawLengths());
        setDrawGrid(settings.isDrawGrid());
        setGridHeight(settings.getGridHeight());
        setGridWidth(settings.getGridWidth());
        setGridSize(settings.getGridSize());
        setLocaleWithLocaleLanguage(settings.getLocale());
    }

    private static class SettingsSingletonHelper {
        private static final SettingsSingleton INSTANCE = new SettingsSingleton();
    }
}
