package view.GUIElements;
/**
 * Class for a combo box item
 */
public class ComboBoxItem {
    /**
     * Value
     * Label
     */
    private final String value, label;
    /**
     * Constructor for the combo box item
     * @param value value
     * @param label label
     */
    public ComboBoxItem(String value, String label) {
        this.value = value;
        this.label = label;
    }
    /**
     * Returns the key
     * @return key
     */
    public String getKey() {
        return value;
    }
    /**
     * Returns the label
     * @return label
     */
    public String getLabel() {
        return label;
    }
    /**
     * Returns the value
     * @return value
     */
    @Override
    public String toString() {
        return label;
    }
}
