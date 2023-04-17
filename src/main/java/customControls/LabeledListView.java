package customControls;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;

public class LabeledListView<T> extends VBox {
    private Label label;
    private ListView<T> listView;

    public LabeledListView(String labelText ) {

        label = new Label(labelText);
        listView = new ListView<>();
        setSpacing(10);
        setPadding(new Insets(10));
        getChildren().addAll(label, listView);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
    public LabeledListView(String labelText, ObservableList<T> items) {
        this(labelText);
        listView.setItems(items);
    }

    public void setItems(ObservableList<T> items) {
        listView.setItems(items);
    }

    public ObservableList<T> getItems() {
        return listView.getItems();
    }

    public void setSelectedIndex(int index) {
        listView.getSelectionModel().select(index);
    }

    public int getSelectedIndex() {
        return listView.getSelectionModel().getSelectedIndex();
    }
}