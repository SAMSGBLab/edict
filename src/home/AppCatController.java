package home;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.Appl;
import model.ApplicationCategory;

public class AppCatController implements Initializable{
	
	@FXML
	private Label id;
	
	@FXML
	private Label name;
	
	private ApplicationCategory app;

	public AppCatController(ApplicationCategory app) {
		super();
		this.app = app;
	}

	public ApplicationCategory getTopic() {
		return app;
	}

	public void setTopic(ApplicationCategory app) {
		this.app = app;
		initData();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		id.setText(app.getCategoryName());
		name.setText("Id: "+app.getCategoryId());
		
	}
	
	public void initData() {
		id.setText(app.getCategoryName());
		name.setText("Id: "+app.getCategoryId());
	}
	

}
