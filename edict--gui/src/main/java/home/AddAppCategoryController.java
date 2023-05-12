package home;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

import customControls.LabeledCheckComboBox;
import customControls.LabeledTextField;
import dataParser.DataParser;
import guimodel.ApplicationCategory;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddAppCategoryController extends BaseAddController{


    @FXML
    private VBox FormBox;

    @FXML
    private Pane FormPane;

    @FXML
    private Button SubmitButton;

	LabeledTextField id;
	LabeledTextField name;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		id=new LabeledTextField("Id",LabeledTextField.TYPE_TEXT);
		id.setText("urn:ngsi-ld:edict:ApplicationCategory:"+UUID.randomUUID().toString());
		id.setDisable(true);
		name= new LabeledTextField("Name",LabeledTextField.TYPE_TEXT);
		FormBox.getChildren().addAll(id,name);
		
		
	}
	public void initData(ApplicationCategory appCat) {
		id.setText(appCat.getId());
		name.setText(appCat.getName());
		id.setDisable(true);


		SubmitButton.setText("Edit");
	}


	@FXML 
	public void SaveCategory() { 
		String app_cat="";
		for (Node node : FormBox.getChildren()) {
			if(node instanceof LabeledTextField) {
				if (((LabeledTextField) node).getText().isEmpty()) {
					showAlertDialog("Please fill all the fields");
					return;	
				}
				app_cat+=((LabeledTextField) node).getText()+",";
			
			}
			if(node instanceof LabeledCheckComboBox) {
				System.out.println(((LabeledCheckComboBox) node).getCheckedItems());
				for(Object topic:((LabeledCheckComboBox) node).getCheckedItems()) {
					app_cat+=topic.toString()+";";
				}
				if(((LabeledCheckComboBox) node).getCheckedItems().size()==0) {
					app_cat+=";";
				}
				
				app_cat+=",";
			}
		}
		DataParser.addToCsv("applicationCategories",app_cat );
		
		  Stage stage = (Stage) SubmitButton.getScene().getWindow();
		  stage.close();


	}
			   
}
