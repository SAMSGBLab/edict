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

public class AddAppController  extends BaseAddController{

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
		id= new LabeledTextField("Id",LabeledTextField.TYPE_TEXT);
		id.setText("urn:ngsi-ld:edict:Application:"+UUID.randomUUID().toString());
		id.setDisable(true);
		name= new LabeledTextField("Name",LabeledTextField.TYPE_TEXT);
		priotity= new LabeledTextField("Priotity",LabeledTextField.TYPE_NUM);
		processingRate= new LabeledTextField("Processing Rate",LabeledTextField.TYPE_NUM);
		ArrayList<Object> categories = DataParser.readModelFromCSv("applicationCategories",ApplicationCategory.class);
		ObservableList<String> categoriesIds = FXCollections.observableArrayList();
		for(Object category:categories) {
			if(category instanceof ApplicationCategory) {
				categoriesIds.add(((ApplicationCategory) category).getId());	
			}
			
		}

		applicationCategory= new LabeledListView("Application Category",FXCollections.observableArrayList(categoriesIds));
		
		ArrayList<Object> observations = DataParser.readModelFromCSv("observations",Observation.class);
		ObservableList<String> topicsIds = FXCollections.observableArrayList();
		for(Object topic:observations) {
			if(topic instanceof Observation) {
				topicsIds.add(((Observation) topic).getId());	
			}
			
		}
		applicationTopics= new LabeledCheckComboBox("Application Topics",FXCollections.observableArrayList(topicsIds));
		FormBox.getChildren().addAll(id,name,priotity,processingRate,applicationCategory,applicationTopics);

		
		
	}
	public void initData(Application app) {
		id.setText(app.getId());
		name.setText(app.getName());
		priotity.setText(((Integer) app.getPriority()).toString());
		processingRate.setText(((Integer)app.getProcessingRate()).toString());
		applicationCategory.setSelectedItem(app.getApplicationCategory());
		id.setDisable(true);

		SubmitButton.setText("Edit");
		
	}


	@FXML 
	public void saveApp() { 
		String app="";
		for (Node node : FormBox.getChildren()) {
			if(node instanceof LabeledTextField) {				
				if (((LabeledTextField) node).getText().isEmpty()) {
				showAlertDialog("Please fill all the fields");
				return;	
				}
				app+=((LabeledTextField) node).getText()+",";
			
			}
			if(node instanceof LabeledCheckComboBox) {
				System.out.println(((LabeledCheckComboBox) node).getCheckedItems());
				for(Object topic:((LabeledCheckComboBox) node).getCheckedItems()) {
					app+=topic.toString()+";";
				}
				if(((LabeledCheckComboBox) node).getCheckedItems().size()==0) {
					app+=";";
				}
				
				app+=",";
			}			
			if(node instanceof LabeledListView) {
				if (((LabeledListView) node).getSelectedItem()==null) {
					showAlertDialog("Please select a category");
					return;	
				}
				else
				app+=((LabeledListView) node).getSelectedItem().toString() +",";
			}
		}
		DataParser.addModeltoCsv("applications",app );
		
		  Stage stage = (Stage) SubmitButton.getScene().getWindow();
		  stage.close();


	}
}
