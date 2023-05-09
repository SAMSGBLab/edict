package home;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

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
import javafx.scene.layout.HBox;
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
import javafx.util.StringConverter;
import modelingEntities.DeviceEntity;

public class AddDeviceController extends BaseAddController {


	LabeledTextField id;
	LabeledTextField name;
	LabeledTextField publishFrequency;
	LabeledTextField messageSize;
	LabeledListView <String> distribution;
	LabeledCheckComboBox<Observation> topics;
	ObservableList<Observation> observationList;
	StringConverter<Observation> converter;

	public void getObservations(){
		ArrayList<Object> observations = DataParser.readModelFromCSv("observations",Observation.class);
		observationList = FXCollections.observableArrayList();
		observationList.addAll( observations.stream().map(obs -> (Observation) obs).collect(Collectors.toList()));
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		id= new LabeledTextField("Id",LabeledTextField.TYPE_TEXT);
		id.setText("urn:ngsi-ld:edict:Device:"+UUID.randomUUID().toString());
		id.setDisable(true);
		name= new LabeledTextField("Name",LabeledTextField.TYPE_TEXT);
		publishFrequency= new LabeledTextField("Publish Frequency",LabeledTextField.TYPE_NUM);
		messageSize= new LabeledTextField("Message Size",LabeledTextField.TYPE_NUM);
		distribution=  new LabeledListView<String>("Distribution",FXCollections.observableArrayList("deterministic","exponential"));
		getObservations();

		topics= new LabeledCheckComboBox<>("Observations",observationList);
		 converter = new StringConverter<>() {
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
		topics.setConverter(converter);
		TextField textField = new TextField();
		Button addButton = new Button("Add");
		addButton.setOnAction(event -> {
			String newItem = textField.getText();
			if (!newItem.isEmpty()) {
				Observation ob=new Observation(UUID.randomUUID().toString());
				ob.setName(newItem);
				DataParser.addModeltoCsv("observations",ob.toString());
				textField.clear();
			}
			getObservations();
		});
		HBox topicsField = new HBox(topics,textField, addButton);
		FormBox.getChildren().addAll(id,name,publishFrequency,messageSize,distribution,topicsField);
		
		
	}
	public void initData(Device device) {
		id.setText(device.getId());
		name.setText(device.getName());
		publishFrequency.setText(((Integer)device.getPublishFrequency()).toString());
		messageSize.setText(((Integer)device.getMessageSize()).toString());
		distribution.setSelectedItem(device.getDataDistribution());

		List<Observation> selectedTopics = device.getCapturesObservation().stream()
				.map(converter::fromString)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		topics.setCheckedItems(selectedTopics);
		id.setDisable(true);

		SubmitButton.setText("Edit");
		
	}

	@FXML
	public void SaveModel() {
		Device device = new Device();
		device.setId(id.getText());
		device.setName(name.getText());
		device.setPublishFrequency(Integer.parseInt(publishFrequency.getText()));
		device.setMessageSize(Integer.parseInt(messageSize.getText()));
		device.setDataDistribution(distribution.getSelectedItem());
		List<String> selectedIds = topics.getCheckedItems().stream()
				.map(Observation::getId)
				.collect(Collectors.toList());
		device.setCapturesObservation(selectedIds);

		DeviceEntity entity= new DeviceEntity(20,20);
		entity.setDevice(device);
		DataParser.addModeltoCsv("devices",entity.toString());
		
		  Stage stage = (Stage) SubmitButton.getScene().getWindow();
		  stage.close();


	}
}
