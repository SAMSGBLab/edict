package home;

import java.io.IOException;
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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;
import modelingEntities.ApplicationEntity;
import org.controlsfx.control.CheckComboBox;

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
    Double X = 400.0;
    Double Y = 250.0;

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
        applicationCategory = new LabeledListView<>("Application Category", FXCollections.observableArrayList(applicationCategoriesList));

        applicationCategory.getListView().setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                ApplicationCategory selectedCategory = applicationCategory.getSelectedItem();
                FXMLLoader loader=showPanel();
                AddAppCategoryController controller=loader.getController();
                controller.initData(selectedCategory);

            }
        });
        applicationCategoryConverter = new StringConverter<>() {
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
        Button addApplicationCategory = new Button("+");
        addApplicationCategory.setOnAction(event -> {
            showPanel();
        });
        HBox applicationCategoryField = new HBox(applicationCategory, addApplicationCategory);


        getObservations();
        applicationTopics = new LabeledCheckComboBox<>("Application Topics", FXCollections.observableArrayList(observationList));

        observationConverter = new StringConverter<>() {
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

        FormBox.getChildren().addAll(id, name, priotity, processingRate, applicationCategoryField,applicationTopics);


    }

    FXMLLoader showPanel() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            List<Window> windows = Stage.getWindows().stream().filter(Window::isShowing).collect(Collectors.toList());
            fxmlLoader.setLocation((getClass().getResource("/fxml/AddAppCat.fxml")));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Add application category");
            stage.setScene(new Scene(root));
            stage.initOwner(windows.get(0));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setOnCloseRequest(event -> loadData());
            stage.setOnHidden(paramT -> loadData());
            stage.show();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return fxmlLoader;
    }
    public void loadData() {
        getApplicationCategories();
        applicationCategory.setItems(applicationCategoriesList);
    }

    public void initData(Application app,Double x,Double y) {
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
        X=x;
        Y=y;

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
                    DataParser.deleteFromCsv("observations", observation.getId());
                    DataParser.addToCsv("observations", observation.toString());

                });

        app.setReceivesObservation(selectedIds);
        ApplicationEntity appEntity = new ApplicationEntity(X,Y,70,70);
        appEntity.setApplication(app);
        DataParser.addToCsv("applications", appEntity.toString());

        Stage stage = (Stage) SubmitButton.getScene().getWindow();
        stage.close();


    }
}
