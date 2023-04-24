package home;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import dataParser.DataParser;
import dataParser.NGSIConverter;
import guimodel.Application;
import guimodel.ApplicationCategory;
import guimodel.Device;
import guimodel.SystemSpecifications;
import guimodel.Observation;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class HomeController implements Initializable {
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
	private Button btnDevices;
    @FXML
    private Button btnGenerator;

	@FXML
	private Button btnCustomers;

	@FXML
	private Button btnMenus;

	@FXML
	private Button btnPackages;

	@FXML
	private Button btnSettings;

	@FXML
	private Button btnSimulate;

	@FXML
	private Button btnSignout;

	@FXML
	private Pane pnlApplication;

	@FXML
	private Pane pnlOrders;
	
	@FXML 
	private Pane pnlDevices;

	@FXML
	private Pane pnlMenus;

	@FXML
	private Pane pnlAppCat;
	
	@FXML
	private Pane pnlGenerator;

	@FXML
	private Pane pnlSmlSettings;
	@FXML
	private Button btnAppCat;
	
    @FXML
    private TableView<Device> deviceTable;
	
    @FXML
    private TableColumn<Device, Void> deviceDelete;

    @FXML
    private TableColumn<Device, Void> deviceEdit;

    @FXML
    private TableColumn<Device, Double> deviceMessageSize;

    @FXML
    private TableColumn<Device, String> deviceName;

    @FXML
    private TableColumn<Device, Integer> devicePublishFrequency;
    
    @FXML
    private TableView<ApplicationCategory> appCatTable;
    @FXML
    private TableColumn<ApplicationCategory, String> appCatName;
    @FXML
    private TableColumn<ApplicationCategory, Void> appCatEdit;
    @FXML
    private TableColumn<ApplicationCategory, Void> appCatDelete;
    
    @FXML
    private TableColumn<Application, Void> appPriority;
    @FXML
    private TableColumn<Application, Void> appRate;
    @FXML
    private TableColumn<Application, Void> appEdit;
    @FXML
    private TableColumn<Application, Void> appDelete;

    @FXML
    private TableColumn<Application, String> appName;
    
    @FXML
    private TableView<Application> appTable;
    
    @FXML
    private TableColumn<Observation, Void> obEdit;
    @FXML
    private TableColumn<Observation, Void> obDelete;

    @FXML
    private TableColumn<Observation, String> obName;
    
    @FXML
    private TableView<Observation> obTable;

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
	private TextField durationField;

	@FXML
	private TextField aliasField;

	@FXML
	private TextField messageField;
	@FXML
	private Text dirPathId;
	
	@FXML
	private Text dataPathId;
	

	@FXML
	private Text ngsiOutputPath;

	@FXML
	private ComboBox<String> appCategory;


	@FXML
	private org.controlsfx.control.CheckComboBox<Observation> appTopics;

	@FXML
	private org.controlsfx.control.CheckComboBox<String> qosAttList;


	private SystemSpecifications systemSpecifications = new SystemSpecifications();
	@FXML
    private ObservableList<Device> deviceList;
	@FXML
    private ObservableList<Application> appList;
	@FXML
    private ObservableList<ApplicationCategory> appCatList;
	@FXML
    private ObservableList<Observation> obList;
	
    private Device devicedata;
    private ApplicationCategory appCatData;
    private Application appData;
    private Observation obData;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		durationField.textProperty().addListener((observable, oldValue, newValue) -> {
		    if (!newValue.matches("\\d*")) {
		    	durationField.setText(newValue.replaceAll("[^\\d]", ""));
		    }
		});
		messageField.textProperty().addListener((observable, oldValue, newValue) -> {
		    if (!newValue.matches("\\d*\\.?\\d*")) {
		    	messageField.setText(newValue.replaceAll("[^\\d\\.]", ""));
		    }
		});
		if(systemSpecifications.loadSystemSpecifications())
			initializeSystemSpecifications();

		initializeDevicesPane();
		initializeAppsPane();
		initializeAppCatsPane();
		initializeTopicsPane();

	}
	public void initializeSystemSpecifications () {
		commChannelLossRT.setText(systemSpecifications.getCommChannelLossRT()+"");
		commChannelLossTS.setText(systemSpecifications.getCommChannelLossTS()+"");
		commChannelLossVS.setText(systemSpecifications.getCommChannelLossVS()+"");
		commChannelLossAN.setText(systemSpecifications.getCommChannelLossAN()+"");
		bandwidthPolicy.setText(systemSpecifications.getBandwidthPolicy()+"");
		brokerCapacity.setText(systemSpecifications.getBrokerCapacity()+"");
		systemBandwidth.setText(systemSpecifications.getSystemBandwidth()+"");
		durationField.setText(systemSpecifications.getSimulationDuration()+"");
		aliasField.setText(systemSpecifications.getAlias()+"");
		messageField.setText(systemSpecifications.getGlobalMessageSize()+"");
	}
	
	public void initializeDevicesPane() {
	     deviceName.setCellValueFactory(new PropertyValueFactory<Device,String>("name"));
	        devicePublishFrequency.setCellValueFactory(new PropertyValueFactory<>("publishFrequency"));
	        deviceMessageSize.setCellValueFactory(new PropertyValueFactory<>("messageSize"));

	        deviceEdit.setCellFactory(param -> 	new TableCell<Device, Void>() {
				private final Button btn = new Button("Edit");
	            {
	                btn.setOnAction((ActionEvent event) -> {
	                    devicedata = getTableView().getItems().get(getIndex());
	                    openAddDevice();
	                });
	            }
	            @Override
	            protected void updateItem(Void item, boolean empty) {
	                super.updateItem(item, empty);

	                if (empty) {
	                    setGraphic(null);
	                } else {
	                    setGraphic(btn);
	                    btn.setStyle("-fx-background-color: #4444F9");
	                }
	            }
	        });
			deviceDelete.setCellFactory(param -> new TableCell<Device, Void>() {
	            private final Button btn = new Button("Delete");

	            {
	                btn.setOnAction((ActionEvent event) -> {
	                    Device data = getTableView().getItems().get(getIndex());
	                    System.out.println("deleted: " + data);
	                    DataParser.deleteModel("devices", data.getId());
	                    deviceTable.getItems().remove(data);
						
	                });
	            }

	            @Override
	            protected void updateItem(Void item, boolean empty) {
	                super.updateItem(item, empty);

	                if (empty) {
	                    setGraphic(null);
	                } else {
	                    setGraphic(btn);
						btn.setStyle("-fx-background-color: #f94444");
	                }
	            }
	        });

	        deviceList = FXCollections.observableArrayList();
			deviceList.addAll(DataParser.readModelFromCSv("devices", Device.class));
	        deviceTable.setItems(deviceList);

	}

	public void initializeAppsPane() {
		appName.setCellValueFactory(new PropertyValueFactory<Application,String>("name"));
		appPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
		appRate.setCellValueFactory(new PropertyValueFactory<>("processingRate"));
		appEdit.setCellFactory(param -> 	new TableCell<Application, Void>() {
			private final Button btn = new Button("Edit");
			{
				btn.setOnAction((ActionEvent event) -> {
					appData = getTableView().getItems().get(getIndex());
					openAddApp();
				});
			}
			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);

				if (empty) {
					setGraphic(null);
				} else {
					setGraphic(btn);
					btn.setStyle("-fx-background-color: #4444F9");
				}
			}
		});
		appDelete.setCellFactory(param -> new TableCell<Application, Void>() {
			private final Button btn = new Button("Delete");
			{
				btn.setOnAction((ActionEvent event) -> {
					Application data = getTableView().getItems().get(getIndex());
					System.out.println("deleted: " + data);
					DataParser.deleteModel("applications", data.getId());
					appTable.getItems().remove(data);
					
				});
			}
			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
				} else {
					setGraphic(btn);
					btn.setStyle("-fx-background-color: #f94444");
				}
			}
		});
		appList = FXCollections.observableArrayList();
		appList.addAll(DataParser.readModelFromCSv("applications", Application.class));
		appTable.setItems(appList);

	}

	public void initializeAppCatsPane() {
		appCatName.setCellValueFactory(new PropertyValueFactory<ApplicationCategory,String>("name"));


        appCatEdit.setCellFactory(param -> 	new TableCell<ApplicationCategory, Void>() {
			private final Button btn = new Button("Edit");
            {
                btn.setOnAction((ActionEvent event) -> {
                    appCatData = getTableView().getItems().get(getIndex());
                    openAddAppCat();
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                    btn.setStyle("-fx-background-color: #4444F9");
                }
            }
        });
		appCatDelete.setCellFactory(param -> new TableCell<ApplicationCategory, Void>() {
            private final Button btn = new Button("Delete");
            {
                btn.setOnAction((ActionEvent event) -> {
                    ApplicationCategory data = getTableView().getItems().get(getIndex());
                    System.out.println("deleted: " + data);
                    DataParser.deleteModel("applicationCategories",data.getId());
                    appCatTable.getItems().remove(data);
					
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
					btn.setStyle("-fx-background-color: #f94444");
                }
            }
        });
        appCatList = FXCollections.observableArrayList();
		appCatList.addAll(DataParser.readModelFromCSv("applicationCategories",ApplicationCategory.class));
        appCatTable.setItems(appCatList);

	}
	public void initializeTopicsPane() {
		obName.setCellValueFactory(new PropertyValueFactory<Observation,String>("name"));
		obEdit.setCellFactory(param -> 	new TableCell<Observation, Void>() {
			private final Button btn = new Button("Edit");
			{
				btn.setOnAction((ActionEvent event) -> {
					obData = getTableView().getItems().get(getIndex());
					openAddObservation();
				});
			}
			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);

				if (empty) {
					setGraphic(null);
				} else {
					setGraphic(btn);
					btn.setStyle("-fx-background-color: #4444F9");
				}
			}
		});
		obDelete.setCellFactory(param -> new TableCell<Observation, Void>() {
			private final Button btn = new Button("Delete");
			{
				btn.setOnAction((ActionEvent event) -> {
					Observation data = getTableView().getItems().get(getIndex());
					System.out.println("deleted: " + data);
					DataParser.deleteModel("observations",data.getId());
					obTable.getItems().remove(data);
					
				});
			}
			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);

				if (empty) {
					setGraphic(null);
				} else {
					setGraphic(btn);
					btn.setStyle("-fx-background-color: #f94444");
				}
			}
		});

		obList = FXCollections.observableArrayList();
		obList.addAll(DataParser.readModelFromCSv("observations",Observation.class));
		obTable.setItems(obList);

	}

	public void handleClicks(ActionEvent actionEvent) {
		if (actionEvent.getSource() == btnCustomers) {
			pnlApplication.toFront();
		}

		if (actionEvent.getSource() == btnSimulate) {
			pnlSmlSettings.toFront();
		}

		if (actionEvent.getSource() == btnMenus) {
			pnlMenus.toFront();
		}
		if (actionEvent.getSource() == btnPackages) {
			pnlOrders.toFront();
		}
		if (actionEvent.getSource() == btnAppCat) {
			pnlAppCat.toFront();
		}
		if (actionEvent.getSource() == btnDevices) {
			pnlDevices.toFront();

		}
		if (actionEvent.getSource() == btnGenerator) {
			pnlGenerator.toFront();

		}
	}
	public void saveSystemSpecifications() {
		systemSpecifications.setSystemBandwidth(Integer.valueOf(systemBandwidth.getText()));
		systemSpecifications.setBandwidthPolicy(bandwidthPolicy.getText());
		systemSpecifications.setBrokerCapacity(Integer.valueOf(brokerCapacity.getText()));
		systemSpecifications.setCommChannelLossAN(Integer.valueOf(commChannelLossAN.getText()));
		systemSpecifications.setCommChannelLossRT(Integer.valueOf(commChannelLossRT.getText()));
		systemSpecifications.setCommChannelLossTS(Integer.valueOf(commChannelLossTS.getText()));
		systemSpecifications.setCommChannelLossVS(Integer.valueOf(commChannelLossVS.getText()));
		systemSpecifications.setSimulationDuration(Integer.valueOf(durationField.getText()));
		systemSpecifications.setAlias(aliasField.getText());
		systemSpecifications.setGlobalMessageSize(Double.valueOf(messageField.getText()));
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		if(systemSpecifications.saveSystemSpecifications())
			alert.setContentText("Settings saved successfully");
		else
			alert.setContentText("Settings not saved ");
		alert.showAndWait();
	
	}

	public String openFileChooser() {
		String path;
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Choose location");
		File defaultDirectory = new File(System.getProperty("user.dir"));
		chooser.setInitialDirectory(defaultDirectory);
		File selectedDirectory = chooser.showDialog(new Stage());
		if (selectedDirectory == null) {
			System.out.println("No Directory selected");
			return null;
		}
		path = selectedDirectory.getPath();
		System.out.println("path: " + path);
		return path;
	}
	public String openCsvChooser() {
		String path;
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Choose csv File");
		chooser.getExtensionFilters().add(new ExtensionFilter("csv Files", "*.csv"));
		File defaultDirectory = new File(System.getProperty("user.dir"));
		chooser.setInitialDirectory(defaultDirectory);
		File selectedDirectory = chooser.showOpenDialog(new Stage());
		path = selectedDirectory.getPath();
		return path;
	}


	public void chooseDatamodelFolder() {
		String path = openFileChooser();
		if(path != null && !path.isEmpty())
		dataPathId.setText(path);
	
	}
	
	public void chooseDestinationFile() {
		String path = openCsvChooser();
		if(path != null && !path.isEmpty())
		dirPathId.setText(path);

	}

	public void simulate() {
		if (dataPathId.getText().isEmpty() || dirPathId.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Please choose a valid path");
			alert.showAndWait();
			return;
		}
	
		String jarPath= "iotSimulator.jar";
		int simulationDuration= durationField.getText().isEmpty() ? 0 : Integer.valueOf(durationField.getText());
		String alias= aliasField.getText();
		double globalMessageSize= messageField.getText().isEmpty() ? 0 : Double.valueOf(messageField.getText());
	
		try {
			List<String> command = new ArrayList<>();
	            command.add("java");
	            command.add("-jar");
	            command.add(jarPath);
	            command.add(dataPathId.getText()); 
	            command.add(dirPathId.getText()); 
	            command.add(simulationDuration+""); 
	            command.add(alias); 
	            command.add(globalMessageSize+""); 
	    System.out.println("command"+command);
		System.out.println("current dir = " + System.getProperty("user.dir"));
		Alert alert = new Alert(AlertType.INFORMATION);
		Platform.runLater(() -> {
			alert.setContentText("Simulation started");
			alert.showAndWait();
		});
		Thread t = new Thread(() -> {
			ProcessBuilder pb = new ProcessBuilder(command);
			try {
				Process process = pb.start();
				BufferedReader reader =
						new BufferedReader(new InputStreamReader(process.getInputStream()));
	
				String line;
				while ((line = reader.readLine()) != null) {
					System.out.println(line);
				}
				
				int exitCode = process.waitFor();
	
			if (exitCode == 0) {
				System.out.println("Process completed successfully.");
			} else {
				System.err.println("Process failed with exit code: " + exitCode);
			}
		} 
		catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
			
		finally {
			Platform.runLater(() -> {
				alert.setContentText("Simulation ended");
				if(!alert.isShowing())
					alert.showAndWait();
			});
		}});
		t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    @FXML
    void chooseNGSIOutput(ActionEvent event) {
    	String path = openFileChooser();
		if(path != null && !path.isEmpty())
		ngsiOutputPath.setText(path);
		
		
    }

    @FXML
    void generate(ActionEvent event) {
    	if (ngsiOutputPath.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Please choose a valid path");
			alert.showAndWait();
			return;
		}
		NGSIConverter.generateNGSIfromCsv(ngsiOutputPath.getText());
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText("NGSI files generated successfully");
		alert.showAndWait();

    }


	FXMLLoader showPanel(String resource,String type){
		FXMLLoader fxmlLoader=new FXMLLoader();
		try {
			List<Window> windows = Stage.getWindows().stream().filter(Window::isShowing).collect(Collectors.toList()); 	
			fxmlLoader.setLocation((getClass().getResource(resource)));
			Parent root  = fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle("Add "+type);
			stage.setScene(new Scene(root));
			stage.initOwner(windows.get(0));
			stage.initModality(Modality.APPLICATION_MODAL); 
			stage.setResizable(false);
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent event) {
		    	updateList();
		    }
		});
		stage.setOnHidden(new EventHandler<WindowEvent>() {

		    @Override
		    public void handle(WindowEvent paramT) {
		    	updateList();
			     
		    }
		});
		stage.show();
	} catch (IOException e) {
	
		e.printStackTrace();
	}
		return fxmlLoader;
	}
	void openAddPanel(String type) {
		String resource = null;
		if (type.equals("Observation")){
			resource = "AddObservation.fxml";
			AddObservationController controller=showPanel(resource,type).getController();
			if(obData!=null) {
				controller.initData(obData);
				obData=null;
			}
		}
		else if (type.equals("Device")){
			resource = "AddDevice.fxml";
			AddDeviceController controller=showPanel(resource,type).getController();
			if(devicedata!=null) {
				controller.initData(devicedata);
				devicedata=null;
			}
		}
		else if (type.equals("Application")){
			resource = "AddApp.fxml";
			AddAppController controller=showPanel(resource,type).getController();
			if(appData!=null) {
				controller.initData(appData);
				appData=null;
			}
		}
		else if (type.equals("Application Category")){
			resource = "AddAppCat.fxml";
			AddAppCategoryController controller=showPanel(resource,type).getController();
			if(appCatData!=null) {
				controller.initData(appCatData);
				appCatData=null;
			}
		}
	}
	@FXML 
	void openAddObservation() {
		openAddPanel("Observation");
	}
	@FXML 
	public void openAddApp() {
		openAddPanel("Application");
	}
	@FXML 
	public void openAddAppCat() {
		openAddPanel("Application Category");
	}
	@FXML 
	public  void openAddDevice() {
		openAddPanel("Device");
	}
	 public void updateList() {
		new Thread(() -> {
			Platform.runLater(() -> {
				deviceList.clear();
				deviceList.addAll(DataParser.readModelFromCSv("devices", Device.class));
				deviceTable.setItems(deviceList);
				appCatList.clear();
				appCatList.addAll(DataParser.readModelFromCSv("applicationCategories", ApplicationCategory.class));
				appCatTable.setItems(appCatList);	
				appList.clear();
				appList.addAll(DataParser.readModelFromCSv("applications", Application.class));
				appTable.setItems(appList);	
				obList.clear();
				obList.addAll(DataParser.readModelFromCSv("observations", Observation.class));
				obTable.setItems(obList);
				
			});
		}).start();
	        
	}
}
