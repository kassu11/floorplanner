package view;

import java.util.Locale;
/**
 * Locale configuration
 */
public enum LocaleConfig {
    /**
     * English
     * Finnish
     * Japanese
     */
    ENGLISH("en", "US"),
    FINNISH("fi", "FI"),
    JAPANESE("ja", "JP");
    /**
     * Language
     * Country
     */
    private final String language;
    private final String country;
    /**
     * Constructor for the locale configuration
     * @param language language
     * @param country country
     */
    private LocaleConfig(String language, String country) {
        this.language = language;
        this.country = country;
    }
    /**
     * Returns the locale
     * @return locale
     */
    public Locale getLocale() {
        return new Locale(language, country);
    }
}
