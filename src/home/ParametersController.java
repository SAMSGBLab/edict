package home;

import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.control.CheckComboBox;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class ParametersController implements Initializable {

	private final String[] params = { "Response time", "latency" };

	@FXML
	private CheckComboBox<String> parameterscheckbox;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		for(String p : params) {
//			parameterscheckbox.getItems().add();
//		}
		init();
//		
	}

	public void init() {
		for (String p : params) {
			parameterscheckbox.getItems().add(p);
		}
//	
	}

}
