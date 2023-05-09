package home;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
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
    LabeledListView<ApplicationCategory> applicationCategory;
    LabeledCheckComboBox<Observation> applicationTopics;
    ArrayList<Object> observations;
    ObservableList<Observation> observationList;
    StringConverter<Observation> observationConverter;
    ArrayList<Object> applicationCategories;
    ObservableList<ApplicationCategory> applicationCategoriesList;
    StringConverter<ApplicationCategory> applicationCategoryConverter;

    public void getObservations() {
        observations = DataParser.readModelFromCSv("observations", Observation.class);
        observationList = FXCollections.observableArrayList();

        observationList.addAll(observations.stream().map(obs -> (Observation) obs).collect(Collectors.toList()));
    }

    public void getApplicationCategories() {
        applicationCategories = DataParser.readModelFromCSv("applicationCategories", ApplicationCategory.class);
        applicationCategoriesList = FXCollections.observableArrayList();
        applicationCategoriesList.addAll(applicationCategories.stream().map(category -> (ApplicationCategory) category).collect(Collectors.toList()));
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        id = new LabeledTextField("Id", LabeledTextField.TYPE_TEXT);
        id.setText("urn:ngsi-ld:edict:Application:" + UUID.randomUUID().toString());
        id.setDisable(true);
        name = new LabeledTextField("Name", LabeledTextField.TYPE_TEXT);
        priotity = new LabeledTextField("Priotity", LabeledTextField.TYPE_NUM);
        processingRate = new LabeledTextField("Processing Rate", LabeledTextField.TYPE_NUM);
        getApplicationCategories();
        applicationCategory = new LabeledListView("Application Category", FXCollections.observableArrayList(applicationCategoriesList));
        applicationCategoryConverter = new StringConverter<ApplicationCategory>() {
            @Override
            public String toString(ApplicationCategory applicationCategory) {
                return applicationCategory.getName();
            }

            @Override
            public ApplicationCategory fromString(String id) {
                for (ApplicationCategory applicationCategory : applicationCategoriesList) {
                    if (applicationCategory.getId().equals(id)) {
                        return applicationCategory;
                    }
                }
                return null;
            }
        };

        applicationCategory.setConverter(applicationCategoryConverter);
        TextField textField = new TextField();
        Button addButton = new Button("Add");
        addButton.setOnAction(event -> {
            String newItem = textField.getText();
            if (!newItem.isEmpty()) {
                ApplicationCategory ob = new ApplicationCategory(UUID.randomUUID().toString());
                ob.setName(newItem);
                DataParser.addModeltoCsv("applicationCategories", ob.toString());
                textField.clear();
            }
            getApplicationCategories();
            applicationCategory.setItems(applicationCategoriesList);
        });
        HBox applicationCategoryField = new HBox(applicationCategory, textField, addButton);
        getObservations();
        applicationTopics = new LabeledCheckComboBox("Application Topics", FXCollections.observableArrayList(observationList));
        observationConverter = new StringConverter<Observation>() {
            @Override
            public String toString(Observation observation) {
                return observation.getName();
            }

            @Override
            public Observation fromString(String id) {
                for (Observation observation : observationList) {
                    if (observation.getId().equals(id)) {
                        return observation;
                    }
                }
                return null;
            }
        };
        applicationTopics.setConverter(observationConverter);

        FormBox.getChildren().addAll(id, name, priotity, processingRate, applicationCategoryField, applicationTopics);


    }

    public void initData(Application app) {
        id.setText(app.getId());
        name.setText(app.getName());
        priotity.setText(((Integer) app.getPriority()).toString());
        processingRate.setText(((Integer) app.getProcessingRate()).toString());
        ApplicationCategory selectedCategory = applicationCategoriesList.stream()
                .filter(category -> category.getId().equals(app.getApplicationCategory()))
                .findFirst()
                .orElse(null);
        applicationCategory.setSelectedItem(selectedCategory);

        List<Observation> selectedTopics = app.getReceivesObservation().stream()
                .map(observationConverter::fromString)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        applicationTopics.setCheckedItems(selectedTopics);
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
        app.setApplicationCategory(applicationCategory.getSelectedItem().getId());

        List<String> selectedIds = applicationTopics.getCheckedItems().stream()
                .map(Observation::getId)
                .collect(Collectors.toList());
        applicationTopics.getCheckedItems().forEach(observation -> {
                    List<String> receivedBy = observation.getIsReceivedBy();
                    receivedBy.add(app.getId());
                    observation.setIsReceivedBy(receivedBy);
                    DataParser.deleteObject("observations", observation.getId());
                    DataParser.addModeltoCsv("observations", observation.toString());

                });

        app.setReceivesObservation(selectedIds);
        ApplicationEntity appEntity = new ApplicationEntity(20, 20);
        appEntity.setApplication(app);
        DataParser.addEntityToCsv("applications", appEntity.toString());

        Stage stage = (Stage) SubmitButton.getScene().getWindow();
        stage.close();


    }
}
