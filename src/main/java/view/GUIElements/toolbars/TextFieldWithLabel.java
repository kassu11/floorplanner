package view.GUIElements.toolbars;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import view.SettingsSingleton;
/**
 * Class for a custom text field with a label
 */
public class TextFieldWithLabel extends VBox {
    /**
     * Label
     */
    private Label label;
    /**
     * Text field
     */
    private TextField textField;
    /**
     * Key
     */
    private String key;
    /**
     * Settings singleton
     */
    private SettingsSingleton settings = SettingsSingleton.getInstance();
    /**
     * Constructor for the text field with label
     * @param labelText label text
     * @param textFieldValue text field value
     */
    public TextFieldWithLabel(String labelText, String textFieldValue) {
        this.label = new Label(settings.getLocalizationString(labelText));
        this.textField = new TextField(textFieldValue);
        this.key = labelText;
        this.getChildren().addAll(label, textField);
    }
    /**
     * Returns the label
     * @return label
     */
    public Label getLabel() {
        return label;
    }
    /**
     * Returns the text field
     * @return text field
     */
    public TextField getTextField() {
        return textField;
    }
    /**
     * Returns the key
     * @return key
     */
    public String getKey() {
        return key;
    }
}
