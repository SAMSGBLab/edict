package home;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class Item implements Initializable{
	
	@FXML
	private Label id;
	
	@FXML
	private Label name;
	
	private Topic topic;

	public Item(Topic topic) {
		super();
		this.topic = topic;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
		initData();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		topic = new Topic("0","0");
		id.setText(topic.getName());
		name.setText("Id: "+topic.getId());
		
	}
	
	public void initData() {
		id.setText(topic.getName());
		name.setText("Id: "+topic.getId());
	}
	

}
