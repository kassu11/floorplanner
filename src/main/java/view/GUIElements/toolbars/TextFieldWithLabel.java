package view.GUIElements.toolbars;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import view.SettingsSingleton;

public class TextFieldWithLabel extends VBox {
    private Label label;
    private TextField textField;
    private String key;
    private SettingsSingleton settings = SettingsSingleton.getInstance();

    public TextFieldWithLabel(String labelText, String textFieldValue) {
        this.label = new Label(settings.getLocalizationString(labelText));
        this.textField = new TextField(textFieldValue);
        this.key = labelText;
        this.getChildren().addAll(label, textField);
    }

    public Label getLabel() {
        return label;
    }

    public TextField getTextField() {
        return textField;
    }

    public String getKey() {
        return key;
    }
}
