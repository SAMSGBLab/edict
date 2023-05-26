package customControls;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class LabeledTextField extends HBox {
    private Label label;

    private TextField textField;
    public static final String TYPE_NUM="number";
    public static final String TYPE_TEXT="text";
    public LabeledTextField(String labelText,String type) {
        label = new Label(labelText +" :");
        label.setMinWidth(150);
        if(Objects.equals(type, "number")){
            textField = new TextField();
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });
        }
        else if(type=="text"){
            textField = new TextField();
        }
        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);
        getChildren().addAll(label, textField);
        setPadding(new Insets(10));
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        
    }

    public String getText() {
        return textField.getText();
    }

    public void setText(String text) {
        textField.setText(text);
    }
}