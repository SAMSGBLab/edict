package home;

import java.net.URL;
import java.util.ResourceBundle;

import customControls.LabeledCheckComboBox;
import customControls.LabeledTextField;
import dataParser.DataParser;
import guimodel.Appl;
import guimodel.ApplicationCategory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import guimodel.Topic;

public class ObservationController implements Initializable{

    @FXML
    private VBox FormBox;

    @FXML
    private Pane FormPane;

    @FXML
    private Button SubmitButton;

	LabeledTextField id;
	LabeledTextField name;
	LabeledCheckComboBox<String> publishers;
	LabeledCheckComboBox<String> subscribers;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		id= new LabeledTextField("id",LabeledTextField.TYPE_TEXT);
		name= new LabeledTextField("name",LabeledTextField.TYPE_TEXT);
		publishers= new LabeledCheckComboBox<String>("publishers");
		subscribers= new LabeledCheckComboBox<String>("subscribers");		
		FormBox.getChildren().addAll(id,name,publishers,subscribers);
	}
	public void initData(Topic top) {
		id.setText(top.getId());
		name.setText(top.getName());
		id.setDisable(true);

		
	}


	@FXML 
	public void saveTopic() { 
		String topic="";
		for (Node node : FormBox.getChildren()) {
			if(node instanceof LabeledTextField) {
				System.out.println(((LabeledTextField) node).getText());
				topic+=((LabeledTextField) node).getText()+",";
			
			}
			if(node instanceof LabeledCheckComboBox) {
				System.out.println(((LabeledCheckComboBox) node).getCheckedItems());
				for(Object o:((LabeledCheckComboBox) node).getCheckedItems()) {
					topic+=o.toString()+";";
				}
				if(((LabeledCheckComboBox) node).getCheckedItems().size()==0) {
					topic+=";";
				}
				
				topic+=",";
			}
		}
		DataParser.addModeltoCsv("observations",topic );
		
		  Stage stage = (Stage) SubmitButton.getScene().getWindow();
		  stage.close();


	}
}
