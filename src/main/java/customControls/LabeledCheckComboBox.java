package customControls;

import org.controlsfx.control.CheckComboBox;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class LabeledCheckComboBox<T> extends HBox {
    private Label label;
    private CheckComboBox<T> checkComboBox;

    public LabeledCheckComboBox(String labelText) {
        label = new Label(labelText);
        checkComboBox = new CheckComboBox<>();
        setSpacing(10);
        setPadding(new Insets(10));
        getChildren().addAll(label, checkComboBox);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    }
    public LabeledCheckComboBox(String labelText, ObservableList<T> items) {
        this(labelText);
        getItems().addAll(items);
    }
    public void setItems(ObservableList<T> items) {
        getItems().addAll(items);
    }
    public ObservableList<T> getItems() {
        return checkComboBox.getItems();
    }
    public ObservableList<T> getCheckedItems() {
        return checkComboBox.getCheckModel().getCheckedItems();
    }
    
}
