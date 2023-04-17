package home;

import java.net.URL;
import java.util.ResourceBundle;

import customControls.LabeledCheckComboBox;
import customControls.LabeledTextField;
import dataParser.DataParser;
import guimodel.Appl;
import guimodel.ApplicationCategory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AppController implements Initializable{

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
	LabeledTextField processingDistribution;
	LabeledTextField applicationCategory;
	LabeledCheckComboBox applicationTopics;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		id= new LabeledTextField("Id",LabeledTextField.TYPE_TEXT);
		name= new LabeledTextField("Name",LabeledTextField.TYPE_TEXT);
		applicationCategory= new LabeledTextField("Application Category",LabeledTextField.TYPE_TEXT);
		priotity= new LabeledTextField("Priotity",LabeledTextField.TYPE_NUM);
		processingRate= new LabeledTextField("Processing Rate",LabeledTextField.TYPE_NUM);
		processingDistribution= new LabeledTextField("Processing Distribution",LabeledTextField.TYPE_TEXT);
		applicationTopics= new LabeledCheckComboBox("Application Topics",FXCollections.observableArrayList("topic1","topic2","topic3"));
		FormBox.getChildren().addAll(id,name,priotity,processingRate,processingDistribution,applicationCategory,applicationTopics);

		
		
	}
	public void initData(Appl app) {
		id.setText(app.getAppId());
		name.setText(app.getAppName());
		priotity.setText(((Integer) app.getPriority()).toString());
		processingRate.setText(((Double)app.getProcessingRate()).toString());
		processingDistribution.setText(app.getProcessingDistribution());
		id.setDisable(true);

		
	}


	@FXML 
	public void saveApp() { 
		String app="";
		for (Node node : FormBox.getChildren()) {
			if(node instanceof LabeledTextField) {
				System.out.println(((LabeledTextField) node).getText());
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
		}
		DataParser.addModeltoCsv("applications",app );
		
		  Stage stage = (Stage) SubmitButton.getScene().getWindow();
		  stage.close();


	}
}
