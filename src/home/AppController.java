package home;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.Appl;

public class AppController implements Initializable{
	
	@FXML
	private Label id;
	
	@FXML
	private Label name;
	
	private Appl app;

	public AppController(Appl app) {
		super();
		this.app = app;
	}

	public Appl getTopic() {
		return app;
	}

	public void setTopic(Appl app) {
		this.app = app;
		initData();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		id.setText(app.getAppName());
		name.setText("Id: "+app.getAppId());
		
	}
	
	public void initData() {
		id.setText(app.getAppName());
		name.setText("Id: "+app.getAppId());
	}
	

}
