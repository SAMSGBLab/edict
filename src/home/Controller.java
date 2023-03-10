package home;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import model.Appl;
import model.ApplicationCategory;
import model.Broker;
import model.Device;
import model.DeviceType;
import model.SystemSpecifications;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;

import org.controlsfx.control.CheckComboBox;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import iotSys.broker.App;
import iotSys.broker.ReadSimulationResults;

public class Controller implements Initializable {

	@FXML
	private MenuItem ParametersMenuItem;

	@FXML
	private VBox pnItems = null;

	@FXML
	private VBox pnDeviceItems = null;

	@FXML
	private VBox pnAppItems = null;

	@FXML
	private VBox pnAppCatItems;

	@FXML
	private Button btnOverview;

	@FXML
	private Button btnOrders;

	@FXML
	private Button btnCustomers;

	@FXML
	private Button btnMenus;

	@FXML
	private Button btnPackages;

	@FXML
	private Button btnSettings;

	@FXML
	private Button btnSettings1;

	@FXML
	private Button btnSignout;

	@FXML
	private Pane pnlCustomer;

	@FXML
	private Pane pnlOrders;

	@FXML
	private Pane pnlOverview;

	@FXML
	private Pane pnlMenus;

	@FXML
	private Pane pnlAppCat;

	@FXML
	private Pane pnlSmlSettings;

	@FXML
	private Button btnAppCat;

	@FXML
	private TextField topicId;

	@FXML
	private TextField topicName;

	@FXML
	private TextField deviceId;

	@FXML
	private TextField deviceName;

	@FXML
	private TextField devicePublishFrequency;

	@FXML
	private TextField deviceMessageSize;

	@FXML
	private TextField deviceDistribution;

	@FXML
	private TextField devicePublishesTo;

	@FXML
	private TextField appId;

	@FXML
	private TextField appName;

	@FXML
	private TextField appPriority;

	@FXML
	private TextField appProcessingRate;

	@FXML
	private TextField appProcessingDistribution;

	@FXML
	private TextField appSubscribesTo;

	@FXML
	private TextField commChannelLossRT;

	@FXML
	private TextField commChannelLossTS;

	@FXML
	private TextField commChannelLossVS;

	@FXML
	private TextField commChannelLossAN;

	@FXML
	private TextField bandwidthPolicy;

	@FXML
	private TextField brokerCapacity;

	@FXML
	private TextField systemBandwidth;

	@FXML
	private TextField applicationCategoryId;

	@FXML
	private TextField applicationCategoryName;

	@FXML
	private Text pathId;

	@FXML
	private Text dirPathId;

	@FXML
	private ComboBox<String> appCategory;

	@FXML
	private ComboBox<String> deviceType1;

	@FXML
	private CheckComboBox<Topic> deviceTopics;

	@FXML
	private CheckComboBox<Topic> appTopics;

	@FXML
	private CheckComboBox<String> qosAttList;

//	private List<Topic> topics = new ArrayList<>();

	private Set<Topic> topics = new HashSet<>();

	private Set<Device> devices = new HashSet<>();

	private Map<String, Appl> apps = new HashMap<>();

	private Map<String, ApplicationCategory> applicationCategories = new HashMap<>();

	private Set<String> virtualSensors = new HashSet<>();

	private Set<String> actuators = new HashSet<>();

	private Set<DeviceType> deviceTypes = new HashSet<>();

	private Device selectedDevice;

	private Topic selectedTopic;

	private ApplicationCategory selectedAppCat;

	private Appl selectedApp;

	private ItemButtonListener myListener;

	private Broker broker;

	private SystemSpecifications systemSpecifications = new SystemSpecifications();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		broker = new Broker();
		broker.setBrokerId("input");
		broker.setBrokerName("input");
		broker.setBufferSize(100000000);

		uploadCsvAppCatFile();
		uploadCsvAppsFile();
		uploadCsvDevicesFile();
		uploadCsvtopicsFile();
		uploadCsvDeviceTypesFile();

		for (Topic t : topics) {
			deviceTopics.getItems().add(t);
			appTopics.getItems().add(t);

		}
		deviceTopics.getCheckModel().check(0);
		appTopics.getCheckModel().check(0);

		qosAttList.getItems().add("Response Time");
		qosAttList.getItems().add("Throughput");
		qosAttList.getCheckModel().check(0);
//		qosAttList.getItems().add("");
//		qosAttList.getItems().add("");

		myListener = new ItemButtonListener() {
			@Override
			public void onClickListener(Device device) {
				setSelectedDevice(device);
				System.out.println("hi from myListener");
			}
		};

		List<String> catsId = new ArrayList<>();
		for (ApplicationCategory cat : applicationCategories.values())
			catsId.add(cat.getCategoryId());
		appCategory.setItems(FXCollections.observableArrayList(catsId));

		List<String> deviceTypesNames = new ArrayList<>();
		for (DeviceType dt : deviceTypes)
			deviceTypesNames.add(dt.getDeviceTypeName());
		deviceType1.setItems(FXCollections.observableArrayList(deviceTypesNames));

		Node[] nodes = new Node[topics.size()];
		Iterator<Topic> iterator = topics.iterator();
		for (int i = 0; i < nodes.length; i++) {
			try {

				final int j = i;

				// give the items some effect

				FXMLLoader loader = new FXMLLoader(getClass().getResource("Item.fxml"));
//				loader.setController(new Item(tps.get(i)));
				Topic current = (Topic) iterator.next();
				loader.setController(new Item(current));
				nodes[i] = loader.load();
				nodes[i].setOnMouseEntered(event -> {
					nodes[j].setStyle("-fx-background-color : #CADCE8");
				});
				nodes[i].setOnMouseExited(event -> {
					nodes[j].setStyle("-fx-background-color : #FFFFFF");
				});
				nodes[i].setOnMouseClicked(event -> {
					setSelectedTopic(current);
					selectedTopic = current;
				});

				pnItems.getChildren().add(nodes[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		initializeDevicesPane();
		initializeAppsPane();
		initializeAppCatsPane();

	}

	public void initializeDevicesPane() {
		Node[] nodes = new Node[devices.size()];
		Iterator<Device> iterator = devices.iterator();
		for (int i = 0; i < nodes.length; i++) {
			try {

				final int j = i;

				// give the items some effect

				FXMLLoader loader = new FXMLLoader(getClass().getResource("DeviceItem.fxml"));
				Device current = (Device) iterator.next();
				loader.setController(new DeviceController(current, myListener));
				nodes[i] = loader.load();
				nodes[i].setOnMouseEntered(event -> {
					nodes[j].setStyle("-fx-background-color : #CADCE8");
				});
				nodes[i].setOnMouseExited(event -> {
					nodes[j].setStyle("-fx-background-color : #FFFFFF");
				});
				nodes[i].setOnMouseClicked(event -> {
					setSelectedDevice(current);
					selectedDevice = current;
				});
				pnDeviceItems.getChildren().add(nodes[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void initializeAppsPane() {
		Node[] nodes = new Node[apps.size()];
		Iterator<Entry<String, Appl>> iterator = apps.entrySet().iterator();

		for (int i = 0; i < nodes.length; i++) {
			try {

				final int j = i;

				// give the items some effect

				FXMLLoader loader = new FXMLLoader(getClass().getResource("AppItem.fxml"));
				Appl current = (Appl) iterator.next().getValue();
				loader.setController(new AppController(current));
				nodes[i] = loader.load();
				nodes[i].setOnMouseEntered(event -> {
					nodes[j].setStyle("-fx-background-color : #CADCE8");
				});
				nodes[i].setOnMouseExited(event -> {
					nodes[j].setStyle("-fx-background-color : #ffffff");
				});
				nodes[i].setOnMouseClicked(event -> {
//					selectedApp = current;
					selectedApp = new Appl(current);
					setSelectedApp(current);
				});
				pnAppItems.getChildren().add(nodes[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void initializeAppCatsPane() {
		Node[] nodes = new Node[applicationCategories.size()];
		Iterator<Entry<String, ApplicationCategory>> iterator = applicationCategories.entrySet().iterator();

		for (int i = 0; i < nodes.length; i++) {
			try {

				final int j = i;

				FXMLLoader loader = new FXMLLoader(getClass().getResource("AppCatItem.fxml"));
				ApplicationCategory current = (ApplicationCategory) iterator.next().getValue();
				loader.setController(new AppCatController(current));
				nodes[i] = loader.load();
				nodes[i].setOnMouseEntered(event -> {
					nodes[j].setStyle("-fx-background-color : #CADCE8");
				});
				nodes[i].setOnMouseExited(event -> {
					nodes[j].setStyle("-fx-background-color : #FFFFFF");
				});
				nodes[i].setOnMouseClicked(event -> {

					selectedAppCat = current;
					setSelectedAppCat(current);
				});
				pnAppCatItems.getChildren().add(nodes[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void addDevice() {
		if (deviceId.getText() == null || deviceId.getText().trim().isEmpty())
			return;
		Device newDevice = new Device(deviceId.getText(), deviceName.getText(),
				Integer.valueOf(devicePublishFrequency.getText()), Double.valueOf(deviceMessageSize.getText()),
				deviceDistribution.getText(), deviceType1.getSelectionModel().getSelectedItem(), new ArrayList<>());
		List<Topic> deviceCheckedTopics = deviceTopics.getCheckModel().getCheckedItems();
		for (int i = 0; i < deviceCheckedTopics.size(); i++) {
			newDevice.getPublishesTo().add(deviceCheckedTopics.get(i));
			deviceCheckedTopics.get(i).getPublishers().add(newDevice);
		}
		devices.add(newDevice);

		selectedDevice = new Device(newDevice);
//		for(Topic tp : newDevice.getPublishesTo()) {
//			tp.getPublishers().add(newDevice);
//		}
		fillDevicesView();

//		File file = new File(System.getProperty("user.dir")+"\\resources\\devicesDescription.csv");
//	    try {
//	        FileWriter outputfile = new FileWriter(file,true);
//	  
//	        CSVWriter writer = new CSVWriter(outputfile);
//	        String[] data = { deviceId.getText(), deviceName.getText(), deviceType1.getSelectionModel().getSelectedItem(),deviceMessageSize.getText() };
//	        writer.writeNext(data);
//	        writer.close();
//	    }
//	    catch (IOException e) {
//	        e.printStackTrace();
//	    }
		deviceId.setText("");
		for (Device device : devices)
			System.out.println("Device: " + device.getDeviceId() + ", " + device.getDevicename());
	}

	public void addApp() {
		Appl newApp = new Appl(appId.getText(), appName.getText(),
				applicationCategories.get(appCategory.getSelectionModel().getSelectedItem()),
				Integer.valueOf(appPriority.getText()), Double.valueOf(appProcessingRate.getText()),
				appProcessingDistribution.getText(), new ArrayList<>());
		List<Topic> appCheckedTopics = appTopics.getCheckModel().getCheckedItems();
		for (int i = 0; i < appCheckedTopics.size(); i++) {
			newApp.getSubscribesTo().add(appCheckedTopics.get(i));
			appCheckedTopics.get(i).getSubscribers().add(newApp);
		}

		appId.setText("");
		apps.put(newApp.getAppId(), newApp);
		selectedApp = new Appl(newApp);
		fillAppsView();
	}

	public void addTopic() {
		Topic newTopic = new Topic(topicId.getText(), topicName.getText());
		topicName.setText("");
		topicId.setText("");
		topics.add(newTopic);
		broker.getTopics().add(newTopic);
		deviceTopics.getItems().add(newTopic);
		appTopics.getItems().add(newTopic);
		fillTopicsView();
		for (Topic topic : topics)
			System.out.println("Topic: " + topic.getId() + ", " + topic.getName());
	}

	public void addAppCat() {
		ApplicationCategory newApplicationCategory = new ApplicationCategory(applicationCategoryId.getText(),
				applicationCategoryName.getText());
		applicationCategoryName.setText("");
		applicationCategoryId.setText("");
		applicationCategories.put(newApplicationCategory.getCategoryId(), newApplicationCategory);
		appCategory.getItems().add(newApplicationCategory.getCategoryId());
		fillAppCatView();
		for (ApplicationCategory ac : applicationCategories.values())
			System.out.println("AppCat: " + ac.getCategoryId() + ", " + ac.getCategoryName());
	}

//	public void deleteDevice() {
//		
//		if (deviceId.getText() != null && !deviceId.getText().trim().isEmpty()) {
//			Set<Device> devs = new HashSet<>();
//		    devs.addAll(devices);
//
//	    	Device dd = null;
//
//		    Iterator<Device> iterator = devs.iterator();
//		    while(iterator.hasNext()) {
//		    	Device dev = iterator.next();
//		    	if(dev.getDeviceId().equals(deviceId.getText().trim())) {
//		    		dd = dev;
//		    		System.out.println("caught.. ");
//		    	}
//		    }
//		    if(dd != null)
//		    	devs.remove(dd);
//			saveDevicesToFile(devs);
//			
//			deviceId.setText("");
//			fillDevicesView();
//		}else {
//			System.out.println("no item selected\n");
//		}
//	}

	public void deleteDevice() {

		if (deviceId.getText() != null && !deviceId.getText().trim().isEmpty()) {

			Device dd = null;

			Iterator<Device> iterator = devices.iterator();
			while (iterator.hasNext()) {
				Device dev = iterator.next();
				if (dev.getDeviceId().equals(deviceId.getText().trim())) {
					dd = dev;
					System.out.println("caught.. ");
				}
			}
			if (dd != null)
				devices.remove(dd);

			deviceId.setText("");
			fillDevicesView();
		} else {
			System.out.println("no item selected\n");
		}
	}

	public void deleteApp() {

		if (appId.getText() != null && !appId.getText().trim().isEmpty()) {

			Appl dd = null;

			Iterator<Appl> iterator = apps.values().iterator();
			while (iterator.hasNext()) {
				Appl app = iterator.next();
				if (app.getAppId().equals(appId.getText().trim())) {
					dd = app;
					System.out.println("caught.. ");
				}
			}
			if (dd != null)
				apps.remove(dd.getAppId());

			appId.setText("");
			fillAppsView();
		} else {
			System.out.println("no item selected\n");
		}
	}

	public void deleteTopic() {
		if (topicId.getText() != null && !topicId.getText().trim().isEmpty()) {
			Topic dd = null;
			for (Topic d : topics) {
				System.out.println("item: " + d.getId());
				System.out.println("field: " + topicId.getText());
				if (d.getId().equals(topicId.getText().trim())) {
					dd = d;
					System.out.println("caught.. ");
				}
			}
			if (dd != null) {
				topics.remove(dd);
				broker.getTopics().remove(dd);
				refreshTopicsCheckComboBox();
			}
			topicId.setText("");
			topicName.setText("");
			fillTopicsView();
		} else {
			System.out.println("no item selected");
		}
	}

	public void deleteAppCat() {
		if (applicationCategoryId.getText() != null && !applicationCategoryId.getText().trim().isEmpty()) {
			ApplicationCategory dd = null;
			for (ApplicationCategory d : applicationCategories.values()) {
				System.out.println("item: " + d.getCategoryId());
				System.out.println("field: " + applicationCategoryId.getText());
				if (d.getCategoryId().equals(applicationCategoryId.getText().trim())) {
					dd = d;
					System.out.println("caught.. ");
				}
			}
			if (dd != null)
				applicationCategories.remove(dd.getCategoryId());
			applicationCategoryId.setText("");
			applicationCategoryName.setText("");
			fillAppCatView();
		} else {
			System.out.println("no item selected");
		}
	}

	public void editTopic() {
		Topic dd = null;
		for (Topic d : topics) {
			if (d.getId().equals(selectedTopic.getId())) {
				dd = d;
			}
		}
		dd.setId(topicId.getText());
		dd.setName(topicName.getText());
		fillTopicsView();
	}

	public void editAppCat() {
		ApplicationCategory dd = null;
		for (ApplicationCategory d : applicationCategories.values()) {
			if (d.getCategoryId().equals(selectedAppCat.getCategoryId())) {
				dd = d;
			}
		}
		dd.setCategoryId(applicationCategoryId.getText());
		dd.setCategoryName(applicationCategoryName.getText());
		fillAppCatView();
	}

	public void editDevice() {

		if (deviceId.getText() != null && !deviceId.getText().trim().isEmpty()) {

			Device dd = null;

			Iterator<Device> iterator = devices.iterator();
			while (iterator.hasNext()) {
				Device dev = iterator.next();
				if (dev.getDeviceId().equals(deviceId.getText().trim())) {
					dd = dev;
					System.out.println("caught.. ");
				}
			}
			if (dd != null) {
				dd.setDeviceId(deviceId.getText());
				dd.setDevicename(deviceName.getText());
				dd.setPublishFrequency(Integer.valueOf(devicePublishFrequency.getText()));
				dd.setMessageSize(Double.valueOf(deviceMessageSize.getText()));
				dd.setDistribution(deviceDistribution.getText());
				dd.setDeviceType(deviceType1.getSelectionModel().getSelectedItem());
				dd.setPublishesTo(new ArrayList<>());

//				List<Topic> deviceCheckedTopics = deviceTopics.getCheckModel().getCheckedItems();
//				for (int i = 0; i < deviceCheckedTopics.size(); i++) {
//					dd.getPublishesTo().add(deviceCheckedTopics.get(i));
//					boolean exists = false;
//					for(Device dev : deviceCheckedTopics.get(i).getPublishers()) {
//						if(dev.getDeviceId().equals(dd.getDeviceId()))
//								exists = true;
//					}
//					if(!exists) //must remove unchecked topics
//						deviceCheckedTopics.get(i).getPublishers().add(dd);

//				}

				for (int i = 0; i < deviceTopics.getCheckModel().getItemCount(); i++) {
					if (deviceTopics.getCheckModel().isChecked(i)) {
						if (!selectedDevice.getPublishesTo().contains(deviceTopics.getCheckModel().getItem(i))) {
							deviceTopics.getCheckModel().getItem(i).getPublishers().add(dd);
						}
						dd.getPublishesTo().add(deviceTopics.getCheckModel().getItem(i));
					} else {
						if (selectedDevice.getPublishesTo().contains(deviceTopics.getCheckModel().getItem(i))) {
							deviceTopics.getCheckModel().getItem(i).getPublishers().remove(dd);
						}
					}
				}

			}
			fillDevicesView();
			selectedDevice = new Device(dd);
		} else {
			System.out.println("no item selected\n");
		}
	}

	public void editApp() {

		if (appId.getText() != null && !appId.getText().trim().isEmpty()) {

			Appl dd = null;

			Iterator<Appl> iterator = apps.values().iterator();
			while (iterator.hasNext()) {
				Appl app = iterator.next();
				if (app.getAppId().equals(appId.getText().trim())) {
					dd = app;
					System.out.println("caught.. ");
				}
			}
			if (dd != null) {
				dd.setAppId(appId.getText());
				dd.setAppName(appName.getText());
				dd.setApplicationCategory(applicationCategories.get(appCategory.getSelectionModel().getSelectedItem()));
				dd.setPriority(Integer.valueOf(appPriority.getText()));
				dd.setProcessingDistribution(appProcessingDistribution.getText());
				dd.setProcessingRate(Double.valueOf(appProcessingRate.getText()));

				dd.setSubscribesTo(new ArrayList<>());
//				List<Topic> appCheckedTopics = appTopics.getCheckModel().getCheckedItems();
//				for (int i = 0; i < appCheckedTopics.size(); i++)
//					dd.getSubscribesTo().add(appCheckedTopics.get(i));

				for (int i = 0; i < appTopics.getCheckModel().getItemCount(); i++) {
					if (appTopics.getCheckModel().isChecked(i)) {
						if (!selectedApp.getSubscribesTo().contains(appTopics.getCheckModel().getItem(i))) {
							appTopics.getCheckModel().getItem(i).getSubscribers().add(dd);
						}
						dd.getSubscribesTo().add(appTopics.getCheckModel().getItem(i));
					} else {
						if (selectedApp.getSubscribesTo().contains(appTopics.getCheckModel().getItem(i))) {
							appTopics.getCheckModel().getItem(i).getSubscribers().remove(dd);
						}
					}
				}
			}
			fillAppsView();
			selectedApp = new Appl(dd);
		} else {
			System.out.println("no item selected\n");
		}
	}

//	public void editDevice() {
//
//		if (deviceId.getText() != null && !deviceId.getText().trim().isEmpty()) {
//			Set<Device> devs = new HashSet<>();
//			devs.addAll(devices);
//
//			Device dd = null;
//
//			Iterator<Device> iterator = devs.iterator();
//			while (iterator.hasNext()) {
//				Device dev = iterator.next();
//				if (dev.getDeviceId().equals(deviceId.getText().trim())) {
//					dd = dev;
//					System.out.println("caught.. ");
//				}
//			}
//			if (dd != null) {
//				dd.setDeviceId(deviceId.getText());
//				dd.setDevicename(deviceName.getText());
//				dd.setPublishFrequency(Integer.valueOf(devicePublishFrequency.getText()));
//				dd.setMessageSize(Double.valueOf(deviceMessageSize.getText()));
//				dd.setDistribution(deviceDistribution.getText());
//				dd.setDeviceType(deviceType1.getSelectionModel().getSelectedItem());
////				dd.setPublishesTo(null);
//			}
//			saveDevicesToFile(devs);
//			fillDevicesView();
//		} else {
//			System.out.println("no item selected\n");
//		}
//	}

	public void resetDeviceForm() {

	}

	public void handleClicks(ActionEvent actionEvent) {
		if (actionEvent.getSource() == btnCustomers) {
			pnlCustomer.setStyle("-fx-background-color : #FFFFFF");
			pnlCustomer.toFront();
		}

		if (actionEvent.getSource() == btnSettings1) {
			pnlSmlSettings.setStyle("-fx-background-color : #FFFFFF");
			pnlSmlSettings.toFront();
		}

		if (actionEvent.getSource() == btnMenus) {
			pnlMenus.setStyle("-fx-background-color : #FFFFFF");
			pnlMenus.toFront();
		}
		if (actionEvent.getSource() == btnOverview) {
//			pnlOverview.setStyle("-fx-background-color : #FFFFFF");
//			pnlOverview.toFront();
			pnlOrders.setStyle("-fx-background-color : #FFFFFF");
			pnlOrders.toFront();
		}
		if (actionEvent.getSource() == btnPackages) {
			pnlOrders.setStyle("-fx-background-color : #FFFFFF");
			pnlOrders.toFront();
		}
		if (actionEvent.getSource() == btnAppCat) {
			pnlAppCat.setStyle("-fx-background-color : #FFFFFF");
			pnlAppCat.toFront();
		}
		if (actionEvent.getSource() == btnOrders) {
//			pnlOrders.setStyle("-fx-background-color : #464F67");
//			pnlOrders.toFront();
			pnlOverview.setStyle("-fx-background-color : #FFFFFF");
			pnlOverview.toFront();
		}
	}

	public void uploadCsvDevicesFile() {
		try {
			FileReader filereader = new FileReader(System.getProperty("user.dir")+"\\resources\\devicesDescription.csv");

			CSVReader csvReader = new CSVReader(filereader);
			String[] nextRecord;

			csvReader.readNext(); // skip header

			while ((nextRecord = csvReader.readNext()) != null) {
				Device current = new Device();

				current.setDistribution("exponential");
				current.setPublishFrequency(1);
				current.setPublishesTo(new ArrayList<Topic>());

				current.setDeviceId(nextRecord[0]);
				current.setDevicename(nextRecord[1]);
				current.setDeviceType(nextRecord[2]);
				current.setMessageSize(Double.valueOf(nextRecord[3]));

				devices.add(current);

				for (String cell : nextRecord) {
					System.out.print(cell + "\t");
				}
				System.out.println();
			}

			csvReader.close();
			filereader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void uploadCsvAppCatFile() {
		try {
//			FileReader filereader = new FileReader(System.getProperty("user.dir")+"\\resources\\appCategoriesDescription.csv");
			FileReader filereader = new FileReader(System.getProperty("user.dir")+"\\resources\\appCategoriesDescription.csv");
			CSVReader csvReader = new CSVReader(filereader);
			String[] nextRecord;

			csvReader.readNext(); // skip header

			while ((nextRecord = csvReader.readNext()) != null) {
				ApplicationCategory current = new ApplicationCategory();

				current.setCategoryId(nextRecord[0]);
				current.setCategoryName(nextRecord[1]);

				applicationCategories.put(current.getCategoryId(), current);

				for (String cell : nextRecord) {
					System.out.print(cell + "\t");
				}
				System.out.println();
			}

			csvReader.close();
			filereader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void uploadCsvAppsFile() {
		try {
			FileReader filereader = new FileReader(System.getProperty("user.dir")+"\\resources\\appsDescription.csv");

			CSVReader csvReader = new CSVReader(filereader);
			String[] nextRecord;

			csvReader.readNext(); // skip header

			while ((nextRecord = csvReader.readNext()) != null) {
				Appl current = new Appl();

				current.setProcessingDistribution("exponential");
				current.setPriority(0);
				current.setSubscribesTo(new ArrayList<Topic>());
//
				current.setAppId(nextRecord[0]);
				current.setAppName(nextRecord[1]);
				current.setApplicationCategory(applicationCategories.get(nextRecord[2])); // to be edited
				current.setProcessingRate(Double.valueOf(nextRecord[3]));

				apps.put(current.getAppId(), current);

				for (String cell : nextRecord) {
					System.out.print(cell + "\t");
				}
				System.out.println();
			}

			csvReader.close();
			filereader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void uploadCsvtopicsFile() {
		try {
			FileReader filereader = new FileReader(System.getProperty("user.dir")+"\\resources\\topicsDescription.csv");

			CSVReader csvReader = new CSVReader(filereader);
			String[] nextRecord;

			csvReader.readNext(); // skip header

			while ((nextRecord = csvReader.readNext()) != null) {
				Topic current = new Topic();

				current.setId(nextRecord[0]);
				current.setName(nextRecord[1]);

				topics.add(current);
				broker.getTopics().add(current);
				for (String cell : nextRecord) {
					System.out.print(cell + "\t");
				}
				System.out.println();
			}

			csvReader.close();
			filereader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void uploadCsvDeviceTypesFile() {
		try {
			FileReader filereader = new FileReader(System.getProperty("user.dir")+"\\resources\\deviceTypesDescription.csv");

			CSVReader csvReader = new CSVReader(filereader);
			String[] nextRecord;

			csvReader.readNext(); // skip header

			while ((nextRecord = csvReader.readNext()) != null) {
				DeviceType current = new DeviceType();

				current.setDeviceTypeId(nextRecord[0]);
				current.setDeviceTypeName(nextRecord[1]);

				deviceTypes.add(current);

				for (String cell : nextRecord) {
					System.out.print(cell + "\t");
				}
				System.out.println();
			}

			csvReader.close();
			filereader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fillTopicsView() {
//		Node[] nodes = new Node[14];
		pnItems.getChildren().removeAll();
		pnItems.getChildren().setAll();

		Node[] nodes = new Node[topics.size()];
		Iterator<Topic> iterator = topics.iterator();
		for (int i = 0; i < nodes.length; i++) {
			try {

				final int j = i;

				// give the items some effect

				FXMLLoader loader = new FXMLLoader(getClass().getResource("Item.fxml"));
				Topic current = (Topic) iterator.next();
				loader.setController(new Item(current));
				nodes[i] = loader.load();
				nodes[i].setOnMouseEntered(event -> {
					nodes[j].setStyle("-fx-background-color : #CADCE8");
				});
				nodes[i].setOnMouseExited(event -> {
					nodes[j].setStyle("-fx-background-color : #FFFFFF");
				});
				nodes[i].setOnMouseClicked(event -> {
					setSelectedTopic(current);
					selectedTopic = current;
				});
				pnItems.getChildren().add(nodes[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void fillDevicesView() {
		System.out.println("fill view..");
		pnDeviceItems.getChildren().removeAll();
		pnDeviceItems.getChildren().setAll();

		Node[] nodes = new Node[devices.size()];
		Iterator<Device> iterator = devices.iterator();
		for (int i = 0; i < nodes.length; i++) {
			final int j = i;

			// give the items some effect

			FXMLLoader loader = new FXMLLoader(getClass().getResource("DeviceItem.fxml"));
			Device current = (Device) iterator.next();
			loader.setController(new DeviceController(current, myListener));
			try {
				nodes[i] = loader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
			nodes[i].setOnMouseEntered(event -> {
				nodes[j].setStyle("-fx-background-color : #CADCE8");
			});
			nodes[i].setOnMouseExited(event -> {
				nodes[j].setStyle("-fx-background-color : #FFFFFF");
			});
			nodes[i].setOnMouseClicked(event -> {
				setSelectedDevice(current);
//				selectedDevice = current;
				selectedDevice = new Device(current);
			});
			pnDeviceItems.getChildren().add(nodes[i]);
		}
	}

	public void fillAppsView() {
//		Node[] nodes = new Node[14];
		pnAppItems.getChildren().removeAll();
		pnAppItems.getChildren().setAll();

		Node[] nodes = new Node[apps.size()];
		Iterator<Entry<String, Appl>> iterator = apps.entrySet().iterator();

		for (int i = 0; i < nodes.length; i++) {
			try {

				final int j = i;

				// give the items some effect

				FXMLLoader loader = new FXMLLoader(getClass().getResource("AppItem.fxml"));
				Appl current = (Appl) iterator.next().getValue();
				loader.setController(new AppController(current));
				nodes[i] = loader.load();
				nodes[i].setOnMouseEntered(event -> {
					nodes[j].setStyle("-fx-background-color : #CADCE8");
				});
				nodes[i].setOnMouseExited(event -> {
					nodes[j].setStyle("-fx-background-color : #ffffff");
				});
				nodes[i].setOnMouseClicked(event -> {
					setSelectedApp(current);
					selectedApp = current;
				});
				pnAppItems.getChildren().add(nodes[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void fillAppCatView() {
//		Node[] nodes = new Node[14];
		pnAppCatItems.getChildren().removeAll();
		pnAppCatItems.getChildren().setAll();

		Node[] nodes = new Node[applicationCategories.size()];
		Iterator<Entry<String, ApplicationCategory>> iterator = applicationCategories.entrySet().iterator();

		for (int i = 0; i < nodes.length; i++) {
			try {

				final int j = i;

				// give the items some effect

				FXMLLoader loader = new FXMLLoader(getClass().getResource("AppCatItem.fxml"));
				ApplicationCategory current = (ApplicationCategory) iterator.next().getValue();
				loader.setController(new AppCatController(current));
				nodes[i] = loader.load();
				nodes[i].setOnMouseEntered(event -> {
					nodes[j].setStyle("-fx-background-color : #CADCE8");
				});
				nodes[i].setOnMouseExited(event -> {
					nodes[j].setStyle("-fx-background-color : #FFFFFF");
				});
				nodes[i].setOnMouseClicked(event -> {

					selectedAppCat = current;
					setSelectedAppCat(current);
				});
				pnAppCatItems.getChildren().add(nodes[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setSelectedDevice(Device device) {
		System.out.println(device.getPublishesTo().size());
//		device.getPublishesTo().add(topics.iterator().next());
		deviceId.setText(device.getDeviceId());
		deviceName.setText(device.getDevicename());
		devicePublishFrequency.setText(device.getPublishFrequency() + "");
		deviceMessageSize.setText(device.getMessageSize() + "");
		deviceDistribution.setText(device.getDistribution());
		deviceType1.getSelectionModel().select(device.getDeviceType());

		List<Topic> temp = new ArrayList<>();

		for (Topic tp : device.getPublishesTo())
			temp.add(tp);
		deviceTopics.getCheckModel().checkAll();
		for (int i = 0; i < deviceTopics.getCheckModel().getItemCount(); i++) {
			if (!temp.contains(deviceTopics.getCheckModel().getItem(i)))
				deviceTopics.getCheckModel().clearCheck(i);
		}

	}

	public void setSelectedApp(Appl app) {
		appId.setText(app.getAppId());
		appName.setText(app.getAppName());
		appPriority.setText(app.getPriority() + "");
		appProcessingDistribution.setText(app.getProcessingDistribution());
		appProcessingRate.setText(app.getProcessingRate() + "");
		appCategory.getSelectionModel().select(app.getApplicationCategory().getCategoryId());
		List<Topic> temp = new ArrayList<>();

		for (Topic tp : app.getSubscribesTo())
			temp.add(tp);
		appTopics.getCheckModel().checkAll();
		for (int i = 0; i < appTopics.getCheckModel().getItemCount(); i++) {
			if (!temp.contains(appTopics.getCheckModel().getItem(i)))
				appTopics.getCheckModel().clearCheck(i);
		}

	}

	public void setSelectedTopic(Topic topic) {
		topicId.setText(topic.getId());
		topicName.setText(topic.getName());

	}

	public void setSelectedAppCat(ApplicationCategory applicationCategory) {
		applicationCategoryId.setText(applicationCategory.getCategoryId());
		applicationCategoryName.setText(applicationCategory.getCategoryName());

	}

	public void saveSystemSpecifications() {
		systemSpecifications.setSystemBandwidth(Integer.valueOf(systemBandwidth.getText()));
		systemSpecifications.setBandwidthPolicy(bandwidthPolicy.getText());
		systemSpecifications.setBrokerCapacity(Integer.valueOf(brokerCapacity.getText()));
		systemSpecifications.setCommChannelLossAN(Integer.valueOf(commChannelLossAN.getText()));
		systemSpecifications.setCommChannelLossRT(Integer.valueOf(commChannelLossRT.getText()));
		systemSpecifications.setCommChannelLossTS(Integer.valueOf(commChannelLossTS.getText()));
		systemSpecifications.setCommChannelLossVS(Integer.valueOf(commChannelLossVS.getText()));

		System.out.println("saved!");
	}

	// choose destination directory
	public String openFileChooser() {
		String path;

		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Choose location");

		File defaultDirectory = new File(System.getProperty("user.dir"));
		chooser.setInitialDirectory(defaultDirectory);

		File selectedDirectory = chooser.showDialog(new Stage());
		path = selectedDirectory.getPath();

		System.out.println("path: " + path);
		return path;
	}

	public String openFile1Chooser() {
		String path;

		FileChooser chooser = new FileChooser();
		chooser.setTitle("Choose Json File");

//		FileFilter filter = new FileNameExtensionFilter("MP3 File","mp3");
//		chooser.filtFileFilter(filter);
		chooser.getExtensionFilters().add(new ExtensionFilter("Json Files", "*.json"));

		File defaultDirectory = new File(System.getProperty("user.dir"));
		chooser.setInitialDirectory(defaultDirectory);

		File selectedDirectory = chooser.showOpenDialog(new Stage());
		path = selectedDirectory.getPath();

		System.out.println("path: " + path);
		return path;
	}

	public void chooseJsonFile() {
		String path = openFile1Chooser();
		pathId.setText(path);
	}

	public void chooseDestinationFolder() {
		String path = openFileChooser();
		dirPathId.setText(path);
	}

	public void simulate() {

//		 String path = openFileChooser();

		JSONObject[] jsonObjectss = new JSONObject[topics.size()];
		JSONObject[] devicesJsonObject = new JSONObject[devices.size()];
		JSONObject[] virtualSensorsJsonObject = new JSONObject[virtualSensors.size()];
		JSONObject[] actuatorsJsonObject = new JSONObject[actuators.size()];
		JSONObject[] applicationsJsonObject = new JSONObject[apps.size()];
		JSONObject[] applicationCategoriesJsonObject = new JSONObject[applicationCategories.size()];
		JSONObject[] brokersJsonObject = new JSONObject[1]; // for now we have one broker

		Iterator<Topic> iterator = topics.iterator();
		Iterator<Device> devicesiterator = devices.iterator();
//		Iterator<Entry<String, App>> appsIterator = apps.entrySet().iterator(); replaced below

		List<String> topicsNames = new ArrayList<>();
		for (Topic topic : broker.getTopics())
			topicsNames.add(topic.getId()); // it may be getId()

		JSONObject brokerJsonObject = new JSONObject();
		brokerJsonObject.put("brokerId", broker.getBrokerId());
		brokerJsonObject.put("brokerName", broker.getBrokerName());
		brokerJsonObject.put("bufferSize", broker.getBufferSize());
		brokerJsonObject.put("processingRate", broker.getProcessingRate());
		brokerJsonObject.put("topics", topicsNames);
		brokersJsonObject[0] = brokerJsonObject;

		for (int i = 0; i < topics.size(); i++) {
			Topic current = iterator.next();
			jsonObjectss[i] = new JSONObject();
			jsonObjectss[i].put("topicId", current.getId());
			jsonObjectss[i].put("topicName", current.getName());
			jsonObjectss[i].put("priority", 0);
			System.out.println(current.getPublishers().size() + "size");
			List<String> devicesIds = new ArrayList<>();
			for (Device dev : current.getPublishers())
				devicesIds.add(dev.getDeviceId());
			List<String> appsIds = new ArrayList<>();
			for (Appl app : current.getSubscribers())
				appsIds.add(app.getAppId());
			jsonObjectss[i].put("publishers", devicesIds);
			jsonObjectss[i].put("subscribers", appsIds);

		}

		for (int i = 0; i < devices.size(); i++) {
			Device current = devicesiterator.next();
			devicesJsonObject[i] = new JSONObject();
			devicesJsonObject[i].put("deviceId", current.getDeviceId());
			devicesJsonObject[i].put("deviceName", current.getDevicename());
			devicesJsonObject[i].put("publishFrequency", current.getPublishFrequency());
			devicesJsonObject[i].put("messageSize", current.getMessageSize());
			devicesJsonObject[i].put("distribution", current.getDistribution());

			List<String> topicsIds = new ArrayList<>();
			for (Topic tp : current.getPublishesTo())
				topicsIds.add(tp.getId());
			devicesJsonObject[i].put("publishesTo", topicsIds);

		}

		Collection<Appl> appls = apps.values();
		Iterator<Appl> appsIterator = appls.iterator();

		for (int i = 0; i < appls.size(); i++) {
			Appl current = appsIterator.next();
			applicationsJsonObject[i] = new JSONObject();
			applicationsJsonObject[i].put("applicationId", current.getAppId());
			applicationsJsonObject[i].put("applicationName", current.getAppName());
			applicationsJsonObject[i].put("applicationCategory", current.getApplicationCategory().getCategoryId());
			applicationsJsonObject[i].put("priority", current.getPriority());
			applicationsJsonObject[i].put("processingRate", current.getProcessingRate());
			applicationsJsonObject[i].put("processingDistribution", current.getProcessingDistribution());
//			applicationsJsonObject[i].put("subscribesTo", current.getSubscribesTo());

			List<String> topicsIds = new ArrayList<>();
			for (Topic tp : current.getSubscribesTo())
				topicsIds.add(tp.getId());
			applicationsJsonObject[i].put("subscribesTo", topicsIds);

		}

		Collection<ApplicationCategory> appCats = applicationCategories.values();
		Iterator<ApplicationCategory> appCatsIterator = appCats.iterator();

		for (int i = 0; i < appCats.size(); i++) {
			ApplicationCategory current = appCatsIterator.next();
			applicationCategoriesJsonObject[i] = new JSONObject();
			applicationCategoriesJsonObject[i].put("categoryId", current.getCategoryId());
			applicationCategoriesJsonObject[i].put("categoryName", current.getCategoryName());

		}

		try {
//			JFileChooser fileChooser = new JFileChooser();
//			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
//	    	int result = fileChooser.showOpenDialog(pnItems.getStage());
//	    	if (result == JFileChooser.APPROVE_OPTION) {
//	    	    File selectedFile = fileChooser.getSelectedFile();
//	    	    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
//	    	}
//			String filePath = System.getProperty("user.dir")+"\\resources\\out.json";
//			String filePath = Controller.class.getProtectionDomain().getCodeSource().getLocation().getPath()+"\\lib\\out.json";
//			String filePath =  System.getProperty("user.dir") + "\\lib\\out.json";
			String jsonPath = pathId.getText();
			String destPath = dirPathId.getText();
			if (destPath.equals("") || destPath.isEmpty()) {
				destPath = openFileChooser();
			}

			if (jsonPath.equals("") || jsonPath.isEmpty()) {
				jsonPath = destPath + "\\out.json";

//			String filePath = (jsonPath.equals("") || jsonPath.isEmpty()) ? destPath + "\\out.json" : jsonPath;
				String filePath = jsonPath;
				FileWriter file = new FileWriter(filePath);

				LinkedHashMap<String, JSONObject[]> jsonOrderedMap = new LinkedHashMap<String, JSONObject[]>();

//			jsonOrderedMap.put("1","red");
//			jsonOrderedMap.put("2","blue");
//			jsonOrderedMap.put("3","green");

				jsonOrderedMap.put("IoTdevices", devicesJsonObject);
				jsonOrderedMap.put("virtualSensors", virtualSensorsJsonObject);
				jsonOrderedMap.put("actuators", actuatorsJsonObject);
				jsonOrderedMap.put("applications", applicationsJsonObject);
				jsonOrderedMap.put("applicationCategories", applicationCategoriesJsonObject);
				jsonOrderedMap.put("topics", jsonObjectss);
				jsonOrderedMap.put("broker", brokersJsonObject);

				JSONObject orderedJson = new JSONObject(jsonOrderedMap);
				orderedJson.put("systemBandwidth", systemSpecifications.getSystemBandwidth());
//			orderedJson.put("bandwidthPolicy", systemSpecifications.getBandwidthPolicy());
				orderedJson.put("bandwidthPolicy", "none");
				orderedJson.put("priorityPolicy", "apps");
				orderedJson.put("commChannelLossAN", systemSpecifications.getCommChannelLossAN());
				orderedJson.put("commChannelLossRT", systemSpecifications.getCommChannelLossRT());
				orderedJson.put("commChannelLossTS", systemSpecifications.getCommChannelLossTS());
				orderedJson.put("commChannelLossVS", systemSpecifications.getCommChannelLossVS());
				orderedJson.put("brokerCapacity", systemSpecifications.getBrokerCapacity());

//			JSONArray jsonArray = new JSONArray(Arrays.asList(orderedJson));

				file.write(orderedJson.toString(4));
				file.close();

			}

//				 							---- json file created ----

//			String args []= {"", destPath + "\\out.json", destPath + "\\iotsystem.jsimg"};
			String args[] = { "", jsonPath, destPath + "\\iotsystem.jsimg" };
			App.main(args);

			String args1[] = { destPath };
			ReadSimulationResults.main(args1);

			String[] command = { "java", "-cp", System.getProperty("user.dir") + "\\lib\\JMT.jar",
					"jmt.commandline.Jmt", "sim", destPath + "\\iotsystem.jsimg", "-maxtime", "60" };

			ProcessBuilder pb = new ProcessBuilder(command);
			pb.redirectErrorStream(true);
			File log = new File(System.getProperty("user.dir") + "\\lib\\java-version.log");
			pb.redirectOutput(log);

			Process p = pb.start();
			System.out.println("jlj");

			pathId.setText("");
			dirPathId.setText("");
//			log.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getDeviceFileIndex(Device device) {
		int counter = 0;
		try {
//			FileReader filereader = new FileReader(System.getProperty("user.dir")+"\\resources\\devicesDescription.csv");
			FileReader filereader = new FileReader(System.getProperty("user.dir")+"\\resources\\devicesDescription.csv");

			try (CSVReader csvReader = new CSVReader(filereader)) {
				String[] nextRecord;

				csvReader.readNext(); // skip header
				counter++;
				while ((nextRecord = csvReader.readNext()) != null) {
					counter++;
					if (nextRecord[0].equals(device.getDeviceId()))
						return counter;
				}

				csvReader.close();
			}
			filereader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public void saveDevicesToFile(Set<Device> devs) {
		File file = new File(System.getProperty("user.dir")+"\\resources\\deletetest.csv");

		Iterator<Device> iterator = devs.iterator();

		try {
			FileWriter outputfile = new FileWriter(file);

			CSVWriter writer = new CSVWriter(outputfile);
			while (iterator.hasNext()) {
				Device dev = iterator.next();
				String[] data = { dev.getDeviceId(), dev.getDevicename(), dev.getDeviceType(), dev.getDeviceType() };
				writer.writeNext(data);
			}

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveAppsToFile(Set<Appl> apps) {
		File file = new File(System.getProperty("user.dir")+"\\resources\\deletetest.csv");

		Iterator<Appl> iterator = apps.iterator();

		try {
			FileWriter outputfile = new FileWriter(file);

			CSVWriter writer = new CSVWriter(outputfile);
			while (iterator.hasNext()) {
				Appl app = iterator.next();
				String[] data = { app.getAppId(), app.getAppName(), app.getApplicationCategory().getCategoryId(),
						app.getProcessingRate() + "" };
				writer.writeNext(data);
			}

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void refreshTopicsCheckComboBox() {
		deviceTopics.getItems().clear();
		appTopics.getItems().clear();
		for (Topic t : topics) {
			deviceTopics.getItems().add(t);
			appTopics.getItems().add(t);
		}
	}

	@FXML
	void openParametersDialougue(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation((getClass().getResource("ParametersDialogue.fxml")));
			fxmlLoader.setController(new ParametersController());
			DialogPane dialogPane = fxmlLoader.load();

			Dialog<ButtonType> dialog = new Dialog<>();
			dialog.setDialogPane(dialogPane);
			dialog.setTitle("Set Parameters");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
