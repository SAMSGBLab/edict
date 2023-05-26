package customControls;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

public class LabeledListView<T> extends HBox {
    private Label label;
    private ListView<T> listView;

    public LabeledListView(String labelText ) {

        label = new Label(labelText);
        label.setMinWidth(150);
        listView = new ListView<>();
        setSpacing(10);
        setPadding(new Insets(10));
        listView.setPlaceholder(new Label("Select a value"));
        listView.setMaxSize(200, 50);
        getChildren().addAll(label, listView);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        
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
    public void setSelectedItem(T item) {
        listView.getSelectionModel().select(item);
    }
    public T getSelectedItem() {
        return listView.getSelectionModel().getSelectedItem();
    }
    public void setSelectedIndex(int index) {
        listView.getSelectionModel().select(index);
    }

    public ListView<T> getListView() {
        return listView;
    }

    public int getSelectedIndex() {
        return listView.getSelectionModel().getSelectedIndex();
    }
    public void setConverter(StringConverter<T> converter) {
        listView.setCellFactory(lv -> {
            TextFieldListCell<T> cell = new TextFieldListCell<T>();
            cell.setConverter(converter);
            return cell;
        });
    }
}