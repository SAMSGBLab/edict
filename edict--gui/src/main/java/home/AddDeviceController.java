package home;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.UUID;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import customControls.*;
import guimodel.Device;
import guimodel.Observation;
import dataParser.DataParser;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.StageStyle;
public class AddDeviceController extends BaseAddController {


	LabeledTextField id;
	LabeledTextField name;
	LabeledTextField publishFrequency;
	LabeledTextField messageSize;
	LabeledListView <String> distribution;
	LabeledCheckComboBox<String> topics;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		id= new LabeledTextField("Id",LabeledTextField.TYPE_TEXT);
		id.setText("urn:ngsi-ld:edict:Device:"+UUID.randomUUID().toString());
		id.setDisable(true);
		name= new LabeledTextField("Name",LabeledTextField.TYPE_TEXT);
		publishFrequency= new LabeledTextField("Publish Frequency",LabeledTextField.TYPE_NUM);
		messageSize= new LabeledTextField("Message Size",LabeledTextField.TYPE_NUM);
		distribution=  new LabeledListView<String>("Distribution",FXCollections.observableArrayList("deterministic","exponential"));
		ArrayList<Object> observations = DataParser.readModelFromCSv("observations",Observation.class);
		ObservableList<String> topicsIds = FXCollections.observableArrayList();
		for(Object topic:observations) {
			if(topic instanceof Observation) {
				topicsIds.add(((Observation) topic).getId());	
			}
			
		}
		topics= new LabeledCheckComboBox<String>("Observations",FXCollections.observableArrayList(topicsIds));
		FormBox.getChildren().addAll(id,name,publishFrequency,messageSize,distribution,topics);
		
		
	}
	public void initData(Device device) {
		id.setText(device.getId());
		name.setText(device.getName());
		publishFrequency.setText(((Integer)device.getPublishFrequency()).toString());
		messageSize.setText(((Integer)device.getMessageSize()).toString());
		distribution.setSelectedItem(device.getDataDistribution());
		topics.setCheckedItems(device.getCapturesObservation());
		id.setDisable(true);

		SubmitButton.setText("Edit");
		
	}
	@FXML 
	public void SaveModel() { 
		String device="";
		for (Node node : FormBox.getChildren()) {
			if(node instanceof LabeledTextField) {
				if (((LabeledTextField) node).getText().isEmpty()) {
					showAlertDialog("Please fill all the fields");
					return;	
				}
				device+=((LabeledTextField) node).getText()+",";
			
			}
			if(node instanceof LabeledCheckComboBox) {
				
				for(Object topic:((LabeledCheckComboBox) node).getCheckedItems()) {
					device+=topic.toString()+";";
				}
				if(((LabeledCheckComboBox) node).getCheckedItems().size()==0) {
					device+=";";
				}
				
				device+=",";
			}
			if(node instanceof LabeledListView) {
				if (((LabeledListView) node).getSelectedItem()==null) {
					showAlertDialog("Please select a distribution");
					return;	
				}
				else
				device+=((LabeledListView) node).getSelectedItem().toString() +",";
			}
		}
		DataParser.addModeltoCsv("devices",device);
		
		  Stage stage = (Stage) SubmitButton.getScene().getWindow();
		  stage.close();


	}
}
