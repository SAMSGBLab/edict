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
import guimodel.Appl;
import guimodel.ApplicationCategory;
import guimodel.Broker;
import guimodel.Device;
import guimodel.SystemSpecifications;
import guimodel.Topic;
import java.io.BufferedReader;
import java.io.InputStreamReader;


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
	private Button btnDevices;

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
    private TableColumn<Appl, Void> appPriority;
    @FXML
    private TableColumn<Appl, Void> appRate;
    @FXML
    private TableColumn<Appl, Void> appEdit;
    @FXML
    private TableColumn<Appl, Void> appDelete;

    @FXML
    private TableColumn<Appl, String> appName;
    
    @FXML
    private TableView<Appl> appTable;
    
    @FXML
    private TableColumn<Topic, Void> obEdit;
    @FXML
    private TableColumn<Topic, Void> obDelete;

    @FXML
    private TableColumn<Topic, String> obName;
    
    @FXML
    private TableView<Topic> obTable;

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
	private ComboBox<String> appCategory;


	@FXML
	private org.controlsfx.control.CheckComboBox<Topic> appTopics;

	@FXML
	private org.controlsfx.control.CheckComboBox<String> qosAttList;


	private SystemSpecifications systemSpecifications = new SystemSpecifications();
	@FXML
    private ObservableList<Device> deviceList;
	@FXML
    private ObservableList<Appl> appList;
	@FXML
    private ObservableList<ApplicationCategory> appCatList;
	@FXML
    private ObservableList<Topic> obList;
	
    private Device devicedata;
    private ApplicationCategory appCatData;
    private Appl appData;
    private Topic obData;
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

		initializeDevicesPane();
		initializeAppsPane();
		initializeAppCatsPane();
		initializeTopicsPane();

	}

	
	public void initializeDevicesPane() {
	     deviceName.setCellValueFactory(new PropertyValueFactory<Device,String>("deviceName"));
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
	                    DataParser.deleteModel("devices", data.getDeviceId());
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
		appName.setCellValueFactory(new PropertyValueFactory<Appl,String>("appName"));
		appPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
		appRate.setCellValueFactory(new PropertyValueFactory<>("processingRate"));
		appEdit.setCellFactory(param -> 	new TableCell<Appl, Void>() {
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
		appDelete.setCellFactory(param -> new TableCell<Appl, Void>() {
			private final Button btn = new Button("Delete");

			{
				btn.setOnAction((ActionEvent event) -> {
					Appl data = getTableView().getItems().get(getIndex());
					System.out.println("deleted: " + data);
					DataParser.deleteModel("applications", data.getAppId());
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
		appList.addAll(DataParser.readModelFromCSv("applications", Appl.class));
		appTable.setItems(appList);

	}

	public void initializeAppCatsPane() {
		appCatName.setCellValueFactory(new PropertyValueFactory<ApplicationCategory,String>("categoryName"));


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
                    DataParser.deleteModel("applicationCategories",data.getCategoryId());
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
		obName.setCellValueFactory(new PropertyValueFactory<Topic,String>("name"));
		obEdit.setCellFactory(param -> 	new TableCell<Topic, Void>() {
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
		obDelete.setCellFactory(param -> new TableCell<Topic, Void>() {
			private final Button btn = new Button("Delete");
			{
				btn.setOnAction((ActionEvent event) -> {
					Topic data = getTableView().getItems().get(getIndex());
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
		obList.addAll(DataParser.readModelFromCSv("observations",Topic.class));
		obTable.setItems(obList);

	}

	public void handleClicks(ActionEvent actionEvent) {
		if (actionEvent.getSource() == btnCustomers) {
			pnlApplication.setStyle("-fx-background-color : #FFFFFF");
			pnlApplication.toFront();
		}

		if (actionEvent.getSource() == btnSimulate) {
			pnlSmlSettings.setStyle("-fx-background-color : #FFFFFF");
			pnlSmlSettings.toFront();
		}

		if (actionEvent.getSource() == btnMenus) {
			pnlMenus.setStyle("-fx-background-color : #FFFFFF");
			pnlMenus.toFront();
		}
		if (actionEvent.getSource() == btnPackages) {
			pnlOrders.setStyle("-fx-background-color : #FFFFFF");
			pnlOrders.toFront();
		}
		if (actionEvent.getSource() == btnAppCat) {
			pnlAppCat.setStyle("-fx-background-color : #FFFFFF");
			pnlAppCat.toFront();
		}
		if (actionEvent.getSource() == btnDevices) {
			pnlDevices.setStyle("-fx-background-color : #FFFFFF");
			pnlDevices.toFront();

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

		System.out.println("saved!");
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

//		FileFilter filter = new FileNameExtensionFilter("MP3 File","mp3");
//		chooser.filtFileFilter(filter);
		chooser.getExtensionFilters().add(new ExtensionFilter("csv Files", "*.csv"));

		File defaultDirectory = new File(System.getProperty("user.dir"));
		chooser.setInitialDirectory(defaultDirectory);

		File selectedDirectory = chooser.showOpenDialog(new Stage());
		path = selectedDirectory.getPath();

		System.out.println("path: " + path);
		return path;
	}


	public void chooseDatamodelFolder() {
		String path = openFileChooser();
		if(path != null && !path.isEmpty())
		dataPathId.setText(path);
		else {
			dataPathId.setText("Please choose a valid path");
		}
	}
	
	public void chooseDestinationFile() {
		String path = openCsvChooser();
		if(path != null && !path.isEmpty())
		dirPathId.setText(path);
		else {
			dirPathId.setText("Please choose a valid file");
		}
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
	ProcessBuilder pb = new ProcessBuilder(command);
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

//	public void saveDevicesToFile(Set<Device> devs) {
//		File file = new File(System.getProperty("user.dir")+"\\resources\\deletetest.csv");
//
//		Iterator<Device> iterator = devs.iterator();
//
//		try {
//			FileWriter outputfile = new FileWriter(file);
//
//			CSVWriter writer = new CSVWriter(outputfile);
//			while (iterator.hasNext()) {
//				Device dev = iterator.next();
//				String[] data = { dev.getDeviceId(), dev.getDevicename(), dev.getDeviceType(), dev.getDeviceType() };
//				writer.writeNext(data);
//			}
//
//			writer.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

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
	@FXML 
	void openAddObservation() {
		try {
			List<Window> windows = Stage.getWindows().stream().filter(Window::isShowing).collect(Collectors.toList()); 	
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation((getClass().getResource("AddObservation.fxml")));
			Parent root  = fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle("Add Observation");
			stage.setScene(new Scene(root));
			stage.initOwner(windows.get(0));
			stage.initModality(Modality.APPLICATION_MODAL); 
			ObservationController controller=fxmlLoader.getController();
			if(obData!=null) {
				
			controller.initData(obData);
			obData=null;	
			}

			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			    @Override
			    public void handle(WindowEvent event) {
			    	updateDeviceList();
			    }
			});
			stage.setOnHidden(new EventHandler<WindowEvent>() {

			    @Override
			    public void handle(WindowEvent paramT) {
			    	updateDeviceList();
				     
			    }
			});
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void openAddCategory(){
		try {
			List<Window> windows = Stage.getWindows().stream().filter(Window::isShowing).collect(Collectors.toList()); 	
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation((getClass().getResource("AddDevice.fxml")));
			Parent root  = fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle("Add Device");
			stage.setScene(new Scene(root));
			stage.initOwner(windows.get(0));
			stage.initModality(Modality.APPLICATION_MODAL); 
			AddDeviceController controller=fxmlLoader.getController();
			if(devicedata!=null) {
				
			controller.initData(devicedata);
			devicedata=null;	
			}

			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			    @Override
			    public void handle(WindowEvent paramT) {
			    	updateDeviceList();
				     
			    }
			});
			stage.setOnHidden(new EventHandler<WindowEvent>() {

			    @Override
			    public void handle(WindowEvent paramT) {
			    	updateDeviceList();
				     
			    }
			});
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML 
	public void openAddApp() {
		try {
			List<Window> windows = Stage.getWindows().stream().filter(Window::isShowing).collect(Collectors.toList()); 	
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation((getClass().getResource("AddApp.fxml")));
			Parent root  = fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle("Add Application");
			stage.setScene(new Scene(root));
			stage.initOwner(windows.get(0));
			stage.initModality(Modality.APPLICATION_MODAL); 
			AppController controller=fxmlLoader.getController();
			if(appData!=null) {
				
			controller.initData(appData);
			appData=null;	
			}

			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			    @Override
			    public void handle(WindowEvent paramT) {
			    	updateDeviceList();
				     
			    }
			});
			stage.setOnHidden(new EventHandler<WindowEvent>() {

			    @Override
			    public void handle(WindowEvent paramT) {
			    	updateDeviceList();
				     
			    }
			});
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML 
	public void openAddAppCat() {
		try {
			List<Window> windows = Stage.getWindows().stream().filter(Window::isShowing).collect(Collectors.toList()); 	
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation((getClass().getResource("AddAppCat.fxml")));
			Parent root  = fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle("Add Application Category");
			stage.setScene(new Scene(root));
			stage.initOwner(windows.get(0));
			stage.initModality(Modality.APPLICATION_MODAL); 
			AppCatController controller=fxmlLoader.getController();
			if(appCatData!=null) {
				
			controller.initData(appCatData);
			appCatData=null;	
			}

			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			    @Override
			    public void handle(WindowEvent paramT) {
			    	updateDeviceList();
				     
			    }
			});
			stage.setOnHidden(new EventHandler<WindowEvent>() {

			    @Override
			    public void handle(WindowEvent paramT) {
			    	updateDeviceList();
				     
			    }
			});
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML 
	public  void openAddDevice() {
		try {
			List<Window> windows = Stage.getWindows().stream().filter(Window::isShowing).collect(Collectors.toList()); 	
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation((getClass().getResource("AddDevice.fxml")));
			Parent root  = fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle("Add Device");
			stage.setScene(new Scene(root));
			stage.initOwner(windows.get(0));
			stage.initModality(Modality.APPLICATION_MODAL); 
			AddDeviceController controller=fxmlLoader.getController();
			if(devicedata!=null) {
				
			controller.initData(devicedata);
			devicedata=null;	
			}

			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			    @Override
			    public void handle(WindowEvent paramT) {
			    	updateDeviceList();
				     
			    }
			});
			stage.setOnHidden(new EventHandler<WindowEvent>() {

			    @Override
			    public void handle(WindowEvent paramT) {
			    	updateDeviceList();
				     
			    }
			});
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	 public void updateDeviceList() {
	        new Thread(() -> {
	            Platform.runLater(() -> {
		deviceList.clear();
		deviceList.addAll(DataParser.readModelFromCSv("devices", Device.class));
		deviceTable.setItems(deviceList);
		appCatList.clear();
		appCatList.addAll(DataParser.readModelFromCSv("applicationCategories", ApplicationCategory.class));
		appCatTable.setItems(appCatList);	
		appList.clear();
		appList.addAll(DataParser.readModelFromCSv("applications", Appl.class));
		appTable.setItems(appList);	
		obList.clear();
		obList.addAll(DataParser.readModelFromCSv("observations", Topic.class));
		obTable.setItems(obList);
	                
	            });
	        }).start();
	        
	    }



}
