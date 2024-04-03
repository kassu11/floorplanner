package view;

import java.util.Locale;

public enum LocaleConfig {
    ENGLISH("en", "US"),
    FINNISH("fi", "FI"),
    JAPANESE("ja", "JP");

    private final String language;
    private final String country;

    private LocaleConfig(String language, String country) {
        this.language = language;
        this.country = country;
    }

    public Locale getLocale() {
        return new Locale(language, country);
    }
}
