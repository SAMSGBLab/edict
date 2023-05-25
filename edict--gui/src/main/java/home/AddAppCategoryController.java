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
	LabeledTextField code;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		id=new LabeledTextField("Id",LabeledTextField.TYPE_TEXT);
		id.setText("urn:ngsi-ld:edict:ApplicationCategory:"+UUID.randomUUID().toString());
		id.setDisable(true);
		name= new LabeledTextField("Name",LabeledTextField.TYPE_TEXT);
		code= new LabeledTextField("Code",LabeledTextField.TYPE_TEXT);
		FormBox.getChildren().addAll(id,name,code);
		
		
	}
	public void initData(ApplicationCategory appCat) {
		id.setText(appCat.getId());
		name.setText(appCat.getName());
		id.setDisable(true);
		code.setText(appCat.getCode());

		SubmitButton.setText("Edit");
	}


	@FXML 
	public void SaveCategory() { 
		String app_cat="";
		app_cat+=id.getText()+",";
		app_cat+=name.getText()+",";
		app_cat+=code.getText();
		DataParser.addToCsv("applicationCategories",app_cat );
		
		  Stage stage = (Stage) SubmitButton.getScene().getWindow();
		  stage.close();


	}
	@FXML
	public void deleteAppCat() {
		DataParser.deleteFromCsv("applicationCategories", id.getText());
		Stage stage = (Stage) SubmitButton.getScene().getWindow();
		stage.close();
	}
			   
}
