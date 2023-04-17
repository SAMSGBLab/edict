package home;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


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
import dataParser.DataParser;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.StageStyle;
public class AddDeviceController implements Initializable {

    @FXML
    private VBox FormBox;

    @FXML
    private Pane FormPane;

    @FXML
    private Button SubmitButton;
    @FXML
    private Button BackButton;

	LabeledTextField id;
	LabeledTextField name;
	LabeledTextField publishFrequency;
	LabeledTextField messageSize;
	LabeledTextField distribution;
	LabeledCheckComboBox<String> topics;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		id= new LabeledTextField("Id",LabeledTextField.TYPE_TEXT);
		name= new LabeledTextField("Name",LabeledTextField.TYPE_TEXT);
		publishFrequency= new LabeledTextField("Publish Frequency",LabeledTextField.TYPE_NUM);
		messageSize= new LabeledTextField("Message Size",LabeledTextField.TYPE_NUM);
		distribution= new LabeledTextField("Distribution",LabeledTextField.TYPE_TEXT);
		topics= new LabeledCheckComboBox<String>("Observations",FXCollections.observableArrayList("topic1","topic2","topic3"));
		FormBox.getChildren().addAll(id,name,publishFrequency,messageSize,distribution,topics);
		
		
	}
	public void initData(Device device) {
		id.setText(device.getDeviceId());
		name.setText(device.getDeviceName());
		publishFrequency.setText(((Integer)device.getPublishFrequency()).toString());
		messageSize.setText(((Double)device.getMessageSize()).toString());
		distribution.setText(device.getDistribution());
		id.setDisable(true);

		
	}


	@FXML 
	public void SaveDevice() { 
		String device="";
		for (Node node : FormBox.getChildren()) {
			if(node instanceof LabeledTextField) {
				System.out.println(((LabeledTextField) node).getText());
				device+=((LabeledTextField) node).getText()+",";
			
			}
			if(node instanceof LabeledCheckComboBox) {
				System.out.println(((LabeledCheckComboBox) node).getCheckedItems());
				for(Object topic:((LabeledCheckComboBox) node).getCheckedItems()) {
					device+=topic.toString()+";";
				}
				if(((LabeledCheckComboBox) node).getCheckedItems().size()==0) {
					device+=";";
				}
				
				device+=",";
			}
		}
		DataParser.addModeltoCsv("devices",device);
		
		  Stage stage = (Stage) SubmitButton.getScene().getWindow();
		  stage.close();


	}
			   

}
