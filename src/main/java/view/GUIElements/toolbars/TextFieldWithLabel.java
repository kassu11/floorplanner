package view.GUIElements.toolbars;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class TextFieldWithLabel extends VBox {
    private Label label;
    private TextField textField;

    public TextFieldWithLabel(String labelText, String textFieldText) {
        this.label = new Label(labelText);
        this.textField = new TextField(textFieldText);
        this.getChildren().addAll(label, textField);
    }

    public Label getLabel() {
        return label;
    }

    public TextField getTextField() {
        return textField;
    }
}
