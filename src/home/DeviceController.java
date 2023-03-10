package home;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import model.Device;

public class DeviceController implements Initializable{
	
	@FXML
	private Label id;
	
	@FXML
	private Label name;
	
	private Device device;
	
	private ItemButtonListener myListener;

	public DeviceController(Device device, ItemButtonListener myListener) {
		super();
		this.device = device;
		this.myListener = myListener;
	}

	public Device getTopic() {
		return device;
	}

	public void setTopic(Device device) {
		this.device = device;
		initData();
	}
	
	@FXML
    private void click(MouseEvent mouseEvent) {
        myListener.onClickListener(device);
        System.out.println("hi from controller");
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		id.setText(device.getDevicename());
		name.setText(device.getDeviceType());
		
	}
	
	public void initData() {
		id.setText(device.getDevicename());
		name.setText(device.getDeviceType());
	}

	public ItemButtonListener getMyListener() {
		return myListener;
	}

	public void setMyListener(ItemButtonListener myListener) {
		this.myListener = myListener;
	}
	

}
