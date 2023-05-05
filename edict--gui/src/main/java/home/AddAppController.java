package home;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.UUID;

import customControls.LabeledCheckComboBox;
import customControls.LabeledListView;
import customControls.LabeledTextField;
import dataParser.DataParser;
import guimodel.Application;
import guimodel.ApplicationCategory;
import guimodel.Observation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelingEntities.ApplicationEntity;

public class AddAppController extends BaseAddController {

    @FXML
    private VBox FormBox;

    @FXML
    private Pane FormPane;

    @FXML
    private Button SubmitButton;

    LabeledTextField id;
    LabeledTextField name;
    LabeledTextField priotity;
    LabeledTextField processingRate;
    LabeledListView<String> applicationCategory;
    LabeledCheckComboBox applicationTopics;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        id = new LabeledTextField("Id", LabeledTextField.TYPE_TEXT);
        id.setText("urn:ngsi-ld:edict:Application:" + UUID.randomUUID().toString());
        id.setDisable(true);
        name = new LabeledTextField("Name", LabeledTextField.TYPE_TEXT);
        priotity = new LabeledTextField("Priotity", LabeledTextField.TYPE_NUM);
        processingRate = new LabeledTextField("Processing Rate", LabeledTextField.TYPE_NUM);
        ArrayList<Object> categories = DataParser.readModelFromCSv("applicationCategories", ApplicationCategory.class);
        ObservableList<String> categoriesIds = FXCollections.observableArrayList();
        for (Object category : categories) {
            if (category instanceof ApplicationCategory) {
                categoriesIds.add(((ApplicationCategory) category).getId());
            }

        }

        applicationCategory = new LabeledListView("Application Category", FXCollections.observableArrayList(categoriesIds));

        ArrayList<Object> observations = DataParser.readModelFromCSv("observations", Observation.class);
        ObservableList<String> topicsIds = FXCollections.observableArrayList();
        for (Object topic : observations) {
            if (topic instanceof Observation) {
                topicsIds.add(((Observation) topic).getId());
            }

        }
        applicationTopics = new LabeledCheckComboBox("Application Topics", FXCollections.observableArrayList(topicsIds));
        FormBox.getChildren().addAll(id, name, priotity, processingRate, applicationCategory, applicationTopics);


    }

    public void initData(Application app) {
        id.setText(app.getId());
        name.setText(app.getName());
        priotity.setText(((Integer) app.getPriority()).toString());
        processingRate.setText(((Integer) app.getProcessingRate()).toString());
        applicationCategory.setSelectedItem(app.getApplicationCategory());
        applicationTopics.setCheckedItems(app.getRecievesObservation());
        id.setDisable(true);

        SubmitButton.setText("Edit");

    }


    @FXML
    public void saveApp() {
        Application app = new Application();
        app.setId(id.getText());
        app.setName(name.getText());
        app.setPriority(Integer.parseInt(priotity.getText()));
        app.setProcessingRate(Integer.parseInt(processingRate.getText()));
        app.setApplicationCategory(applicationCategory.getSelectedItem());
        app.setRecievesObservation(applicationTopics.getCheckedItems());
        ApplicationEntity appEntity = new ApplicationEntity(20, 20);
        appEntity.setApplication(app);
        DataParser.addEntityToCsv("applications", appEntity.toString());

        Stage stage = (Stage) SubmitButton.getScene().getWindow();
        stage.close();


    }
}
