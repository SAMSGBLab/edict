package home;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
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

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;

import dataParser.DataParser;
import dataParser.NGSIConverter;
import guimodel.Application;
import guimodel.ApplicationCategory;
import guimodel.Device;
import guimodel.SystemSpecifications;
import guimodel.Observation;
import javafx.scene.input.MouseEvent;
import modelingEntities.ApplicationEntity;
import modelingEntities.BaseEntity;
import modelingEntities.BrokerEntity;
import modelingEntities.DeviceEntity;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class HomeController implements Initializable {
    @FXML
    private Button btnDevices;

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnMenus;

    @FXML
    private Button btnPackages;


    @FXML
    private Button btnSimulate;

    @FXML
    private Button btnModeling;

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
    private Pane pnlModeling;
    @FXML
    private Button btnAppCat;
    @FXML
    private Button btnaddDevice;
    @FXML
    private Button btnaddApp;
    @FXML
    private Button btnDeleteEntity;
    @FXML
    private Button btnSaveEntities;

    @FXML
    private Pane pnlDraw;
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




    private SystemSpecifications systemSpecifications = new SystemSpecifications();
    @FXML
    private ObservableList<Device> deviceList;
    @FXML
    private ObservableList<Application> appList;
    @FXML
    private ObservableList<ApplicationCategory> appCatList;
    @FXML
    private ObservableList<Observation> obList;
    @FXML
    private ObservableList<DeviceEntity> deviceEntityList;
    @FXML
    private ObservableList<BrokerEntity> brokerEntityList;
    @FXML
    private ObservableList<ApplicationEntity> applicationEntityList;
    public Device devicedata;
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
        if (systemSpecifications.loadSystemSpecifications())
            initializeSystemSpecifications();

//        initializeDevicesPane();
//        initializeAppsPane();
//        initializeAppCatsPane();
//        initializeTopicsPane();
        initializeModelingPane();

    }

    private void loadEntities() {

        pnlDraw.getChildren().clear();
        int NUM_ROWS = 80;
        int NUM_COLS = 80;
        int CELL_SIZE = 20;

        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                Rectangle cell = new Rectangle(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                cell.setStroke(Color.GRAY);
                cell.setFill(Color.WHITE);
                pnlDraw.getChildren().add(cell);
            }
        }
        BrokerEntity entity = new BrokerEntity();
        pnlDraw.getChildren().add(entity);
        deviceEntityList = FXCollections.observableArrayList();
        deviceEntityList.addAll(DataParser.readEntityFromCsv("devices", DeviceEntity.class));
        brokerEntityList =FXCollections.observableArrayList();
        brokerEntityList.addAll(DataParser.readEntityFromCsv("brokers", BrokerEntity.class));
        applicationEntityList=FXCollections.observableArrayList();
        applicationEntityList.addAll(DataParser.readEntityFromCsv("applications", ApplicationEntity.class));
        for (DeviceEntity deviceEntity : deviceEntityList) {
            deviceEntity.setEntityName(deviceEntity.getDevice().getName());
            deviceEntity.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    AddDeviceController controller = showPanel("AddDevice.fxml", "Device").getController();
                    controller.initData(deviceEntity.getDevice());
                }
            });

            pnlDraw.getChildren().add(deviceEntity);
        }
        for (ApplicationEntity applicationEntity : applicationEntityList) {
            applicationEntity.setEntityName(applicationEntity.getApplication().getName());
            applicationEntity.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    AddAppController controller = showPanel("AddApp.fxml", "Application").getController();
                    controller.initData(applicationEntity.getApplication());
                }
            });
            pnlDraw.getChildren().add(applicationEntity);
        }

    }

    private void initializeModelingPane() {

        loadEntities();
        btnaddDevice.setOnAction(e -> {
            openAddDevice();
        });
        btnaddApp.setOnAction(e -> {
            openAddApp();
        });

        btnDeleteEntity.setOnAction(e -> {
            Thread backgroundThread = new Thread(new Runnable() {
                public void run() {
                    for (Node node : pnlDraw.getChildren()) {
                        if (node instanceof BaseEntity && !((BaseEntity) node).isSelected) {
                            continue;
                        }
                        if (node instanceof DeviceEntity) {
                            DeviceEntity deviceEntity = (DeviceEntity) node;
                            DataParser.deleteObject("devices", deviceEntity.getDevice().getId());
                        } else if (node instanceof ApplicationEntity) {
                            ApplicationEntity applicationEntity = (ApplicationEntity) node;
                            DataParser.deleteObject("applications", applicationEntity.getApplication().getId());
                        }
                    }
                    Platform.runLater(() -> {
                        loadEntities();
                    });
                }
            });

            backgroundThread.start();
        });
        btnSaveEntities.setOnAction(e -> {
            Thread backgroundThread = new Thread(new Runnable() {
                public void run() {
                    for (Node node : pnlDraw.getChildren()) {
                        if (node instanceof DeviceEntity) {
                            DeviceEntity deviceEntity = (DeviceEntity) node;
                            DataParser.addEntityToCsv("devices", deviceEntity.toString());
                        } else if (node instanceof ApplicationEntity) {
                            ApplicationEntity applicationEntity = (ApplicationEntity) node;
                            DataParser.addEntityToCsv("applications", applicationEntity.toString());
                        }


                    }
                    Platform.runLater(() -> {
                        loadEntities();
                    });
                }
            });

            backgroundThread.start();

        });


    }


    public void initializeSystemSpecifications() {
        commChannelLossRT.setText(systemSpecifications.getCommChannelLossRT() + "");
        commChannelLossTS.setText(systemSpecifications.getCommChannelLossTS() + "");
        commChannelLossVS.setText(systemSpecifications.getCommChannelLossVS() + "");
        commChannelLossAN.setText(systemSpecifications.getCommChannelLossAN() + "");
        bandwidthPolicy.setText(systemSpecifications.getBandwidthPolicy() + "");
        brokerCapacity.setText(systemSpecifications.getBrokerCapacity() + "");
        systemBandwidth.setText(systemSpecifications.getSystemBandwidth() + "");
        durationField.setText(systemSpecifications.getSimulationDuration() + "");
        aliasField.setText(systemSpecifications.getAlias() + "");
        messageField.setText(systemSpecifications.getGlobalMessageSize() + "");
    }

    public void initializeDevicesPane() {
        deviceName.setCellValueFactory(new PropertyValueFactory<Device, String>("name"));
        devicePublishFrequency.setCellValueFactory(new PropertyValueFactory<>("publishFrequency"));
        deviceMessageSize.setCellValueFactory(new PropertyValueFactory<>("messageSize"));

        deviceEdit.setCellFactory(param -> new TableCell<Device, Void>() {
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
                    DataParser.deleteObject("devices", data.getId());
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
        appName.setCellValueFactory(new PropertyValueFactory<Application, String>("name"));
        appPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        appRate.setCellValueFactory(new PropertyValueFactory<>("processingRate"));
        appEdit.setCellFactory(param -> new TableCell<Application, Void>() {
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
                    DataParser.deleteObject("applications", data.getId());
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
        appCatName.setCellValueFactory(new PropertyValueFactory<ApplicationCategory, String>("name"));


        appCatEdit.setCellFactory(param -> new TableCell<ApplicationCategory, Void>() {
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
                    DataParser.deleteObject("applicationCategories", data.getId());
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
        appCatList.addAll(DataParser.readModelFromCSv("applicationCategories", ApplicationCategory.class));
        appCatTable.setItems(appCatList);

    }

    public void initializeTopicsPane() {
        obName.setCellValueFactory(new PropertyValueFactory<Observation, String>("name"));
        obEdit.setCellFactory(param -> new TableCell<Observation, Void>() {
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
                    DataParser.deleteObject("observations", data.getId());
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
        obList.addAll(DataParser.readModelFromCSv("observations", Observation.class));
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
        if (actionEvent.getSource() == btnModeling) {
            pnlModeling.toFront();

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
        if (systemSpecifications.saveSystemSpecifications())
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
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose csv File Location");
//		chooser.getExtensionFilters().add(new ExtensionFilter("csv Files", "*.csv"));
        File defaultDirectory = new File(System.getProperty("user.dir"));
        chooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = chooser.showDialog(new Stage());
        path = selectedDirectory.getPath();
        return path;
    }


    public void chooseDatamodelFolder() {
        String path = openFileChooser();
        if (path != null && !path.isEmpty())
            dataPathId.setText(path);

    }

    public void chooseDestinationFile() {
        String path = openCsvChooser();
        if (path != null && !path.isEmpty())
            dirPathId.setText(path);

    }

    public void simulate() {
        if (dataPathId.getText().isEmpty() || dirPathId.getText().isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Please choose a valid path");
            alert.showAndWait();
            return;
        }

        String jarPath = "iotsimulator.jar";
        int simulationDuration = durationField.getText().isEmpty() ? 0 : Integer.valueOf(durationField.getText());
        String alias = aliasField.getText();
        double globalMessageSize = messageField.getText().isEmpty() ? 0 : Double.valueOf(messageField.getText());

        try {
            List<String> command = new ArrayList<>();
            command.add("java");
            command.add("-jar");
            command.add(jarPath);
            command.add(dataPathId.getText());
            command.add(dirPathId.getText());
            command.add(simulationDuration + "");
            command.add(alias);
            command.add(globalMessageSize + "");
            System.out.println("command" + command);
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
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    StringBuilder outputBuilder = new StringBuilder();
                    StringBuilder errorOutputBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        final String currentLine = line;
                        Platform.runLater(() -> {
                            System.out.println(currentLine);
                            if (!alert.isShowing())
                                alert.showAndWait();
                            alert.setContentText(currentLine);

                        });
                    }
                    while ((line = errorReader.readLine()) != null) {
                        final String currentLine = line;
                        Platform.runLater(() -> {
                            System.out.println(currentLine);
                            if (!alert.isShowing())
                                alert.showAndWait();
                            alert.setContentText(currentLine);

                        });
                    }

                    int exitCode = process.waitFor();
                    if (exitCode == 0) {
                        Platform.runLater(() -> {

                            alert.setContentText("Simulation finished successfully");
                            if (!alert.isShowing())
                                alert.showAndWait();

                        });
                    } else {
                        Platform.runLater(() -> {

                            Alert alertError = new Alert(AlertType.ERROR);
                            alertError.setContentText("Simulation failed");
                            alertError.showAndWait();
                            alert.close();
                        });
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    String chooseNGSIOutput() {
        String path = openFileChooser();
        if (path != null && !path.isEmpty())
            return path;
        return null;
    }

    @FXML
    void generateNGSI(ActionEvent event) {
        String path =chooseNGSIOutput();
        if (path == null || path.isEmpty()) {
            File dir = new File("output");
            dir.mkdirs();
            path = "output";
        }
        NGSIConverter.generateNGSIfromCsv(path);
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText("NGSI files generated successfully in :" + path);
        alert.showAndWait();

    }


    FXMLLoader showPanel(String resource, String type) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            List<Window> windows = Stage.getWindows().stream().filter(Window::isShowing).collect(Collectors.toList());
            fxmlLoader.setLocation((getClass().getResource("/fxml/" + resource)));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Add " + type);
            stage.setScene(new Scene(root));
            stage.initOwner(windows.get(0));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    loadEntities();
                }
            });
            stage.setOnHidden(new EventHandler<WindowEvent>() {

                @Override
                public void handle(WindowEvent paramT) {
                    loadEntities();

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
        if (type.equals("Observation")) {
            resource = "AddObservation.fxml";
            AddObservationController controller = showPanel(resource, type).getController();
            if (obData != null) {
                controller.initData(obData);
                obData = null;
            }
        } else if (type.equals("Device")) {
            resource = "AddDevice.fxml";
            AddDeviceController controller = showPanel(resource, type).getController();
            if (devicedata != null) {
                controller.initData(devicedata);
                devicedata = null;
            }
        } else if (type.equals("Application")) {
            resource = "AddApp.fxml";
            AddAppController controller = showPanel(resource, type).getController();
            if (appData != null) {
                controller.initData(appData);
                appData = null;
            }
        } else if (type.equals("Application Category")) {
            resource = "AddAppCat.fxml";
            AddAppCategoryController controller = showPanel(resource, type).getController();
            if (appCatData != null) {
                controller.initData(appCatData);
                appCatData = null;
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
    public void openAddDevice() {
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
