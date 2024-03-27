package view.GUIElements;

public class ComboBoxItem {
    private final String value, label;

    public ComboBoxItem(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
