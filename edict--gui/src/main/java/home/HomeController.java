package home;

import dataParser.DataParser;
import dataParser.NGSIConverter;
import guimodel.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.*;
import modelingEntities.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;


public class HomeController implements Initializable {


    @FXML
    private Button btnPackages;


    @FXML
    private Button btnSimulate;

    @FXML
    private Button btnModeling;

    @FXML
    private Pane pnlOrders;


    @FXML
    private Pane pnlSmlSettings;
    @FXML
    private Pane pnlModeling;

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
    private final SystemSpecifications systemSpecifications = new SystemSpecifications();
    @FXML
    private ObservableList<DeviceEntity> deviceEntityList;
    @FXML
    private ObservableList<BrokerEntity> brokerEntityList;
    @FXML
    private ObservableList<ApplicationEntity> applicationEntityList;

    //getInstance
    public static HomeController getInstance() {
        return new HomeController();
    }

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
        applicationEntityList = FXCollections.observableArrayList();
        applicationEntityList.addAll(DataParser.readEntityFromCsv("applications", ApplicationEntity.class));
        ObservableList<ApplicationCategory> applicationCategories = FXCollections.observableArrayList();
        applicationCategories.addAll(DataParser.readModelFromCSv("applicationCategories", ApplicationCategory.class));
        List<String> colors = Arrays.asList("#e6194b", "#3cb44b", "#ffe119", "#0082c8", "#f58231", "#911eb4", "#46f0f0", "#f032e6", "#d2f53c", "#fabebe");
        HashMap<String, String> categoryColorHashMap = new HashMap<>();
        for (int i = 0; i < applicationCategories.size(); i++) {
            categoryColorHashMap.put(applicationCategories.get(i).getId(), colors.get(i%10));
        }

        HashMap<String, String> applicationCategoryHashMap = new HashMap<>();
        for (ApplicationCategory applicationCategory : applicationCategories) {
            applicationCategoryHashMap.put(applicationCategory.getId(), applicationCategory.getCode());
        }
        ArrayList<Observation> observations = DataParser.readModelFromCSv("observations", Observation.class);
        HashMap<String, String> observationHashMap = new HashMap<>();
        for (Observation observation : observations) {
            observationHashMap.put(observation.getId(), observation.getName());
        }
        for (DeviceEntity deviceEntity : deviceEntityList) {
            deviceEntity.setEntityName(deviceEntity.getDevice().getName());
            if (deviceEntity.getArrow() != null) {

                StringBuilder Names = new StringBuilder();
                deviceEntity.getDevice().getCapturesObservation()
                        .forEach(observation -> Names.append(observationHashMap.get(observation)).append(" "));
                deviceEntity.getArrow().getLabel().setText(String.valueOf(Names));
                deviceEntity.splitArrow();
            }
            for(Node node:deviceEntity.getChildren()){
                node.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2) {
                        AddDeviceController controller = showPanel("AddDevice.fxml", "Device").getController();
                        controller.initData(deviceEntity.getDevice(), deviceEntity.getTranslateX(), deviceEntity.getTranslateY());
                    }

                });
            }

            pnlDraw.getChildren().add(deviceEntity);

        }
        for (ApplicationEntity applicationEntity : applicationEntityList) {
            applicationEntity.setEntityName(applicationEntity.getApplication().getName());
            applicationEntity.getApplicationCategoryLabel().setText(applicationCategoryHashMap.get(applicationEntity.getApplication().getApplicationCategory()));

            if (applicationEntity.getArrow() != null) {
                StringBuilder Names = new StringBuilder();
                applicationEntity.getApplication().getReceivesObservation()
                        .forEach(observation -> Names.append(observationHashMap.get(observation)).append(" "));
                applicationEntity.getArrow().getLabel().setText(String.valueOf(Names));
                applicationEntity.getRectangle().setStyle("-fx-fill: " + categoryColorHashMap.get(applicationEntity.getApplication().getApplicationCategory()) + ";");
                        applicationEntity.getApplicationCategoryLabel().setStyle("-fx-background-color: "
                        + categoryColorHashMap.get(applicationEntity.getApplication().getApplicationCategory())+
                        ";-fx-background-radius: 7px;"+ "-fx-text-alignment: center;"+"-fx-font-weight: bold;");
                applicationEntity.getApplicationCategoryLabel().setPrefWidth(22);
                applicationEntity.getApplicationCategoryLabel().setPrefHeight(22);
                applicationEntity.getApplicationCategoryLabel().setTranslateX(applicationEntity.getRectangle().getWidth() *0.7);

                applicationEntity.splitArrow();
            }
            for(Node node:applicationEntity.getChildren()){
                if(node instanceof Arrow)
                    continue;
                node.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2) {
                        AddAppController controller = showPanel("AddApp.fxml", "Application").getController();
                        controller.initData(applicationEntity.getApplication(), applicationEntity.getTranslateX(), applicationEntity.getTranslateY());

                    }
                });
            }
            pnlDraw.getChildren().add(applicationEntity);
        }

    }

    private void initializeModelingPane() {

        loadEntities();
        btnaddDevice.setOnAction(e -> openAddDevice());
        btnaddApp.setOnAction(e -> openAddApp());

        btnDeleteEntity.setOnAction(e -> {
            Thread backgroundThread = new Thread(() -> {
                for (Node node : pnlDraw.getChildren()) {
                    if (node instanceof BaseEntity && !((BaseEntity) node).isSelected) {
                        continue;
                    }
                    if (node instanceof DeviceEntity) {
                        DeviceEntity deviceEntity = (DeviceEntity) node;
                        DataParser.deleteFromCsv("devices", deviceEntity.getDevice().getId());
                    } else if (node instanceof ApplicationEntity) {
                        ApplicationEntity applicationEntity = (ApplicationEntity) node;
                        DataParser.deleteFromCsv("applications", applicationEntity.getApplication().getId());
                    }
                }
                Platform.runLater(this::loadEntities);
            });

            backgroundThread.start();
        });
        btnSaveEntities.setOnAction(e -> {
            Thread backgroundThread = new Thread(() -> {
                for (Node node : pnlDraw.getChildren()) {
                    if (node instanceof DeviceEntity) {
                        DeviceEntity deviceEntity = (DeviceEntity) node;
                        DataParser.addToCsv("devices", deviceEntity.toString());
                    } else if (node instanceof ApplicationEntity) {
                        ApplicationEntity applicationEntity = (ApplicationEntity) node;
                        DataParser.addToCsv("applications", applicationEntity.toString());
                    }
                }
                Platform.runLater(this::loadEntities);
            });
            backgroundThread.start();
        });
    }


    public void initializeSystemSpecifications() {
        commChannelLossRT.setText(String.valueOf(systemSpecifications.getCommChannelLossRT()));
        commChannelLossTS.setText(String.valueOf(systemSpecifications.getCommChannelLossTS()));
        commChannelLossVS.setText(String.valueOf(systemSpecifications.getCommChannelLossVS()));
        commChannelLossAN.setText(String.valueOf(systemSpecifications.getCommChannelLossAN()));
        bandwidthPolicy.setText(systemSpecifications.getBandwidthPolicy());
        brokerCapacity.setText(String.valueOf(systemSpecifications.getBrokerCapacity()));
        systemBandwidth.setText(String.valueOf(systemSpecifications.getSystemBandwidth()));
        durationField.setText(String.valueOf(systemSpecifications.getSimulationDuration()));
        aliasField.setText(systemSpecifications.getAlias());
        messageField.setText(String.valueOf(systemSpecifications.getGlobalMessageSize()));


    }


    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnSimulate) {
            pnlSmlSettings.toFront();
        }
        if (actionEvent.getSource() == btnPackages) {
            pnlOrders.toFront();
        }
        if (actionEvent.getSource() == btnModeling) {
            pnlModeling.toFront();
            for (DeviceEntity deviceEntity : deviceEntityList) {
                deviceEntity.getArrow().updateLabelPosition();
            }
        }
    }

    public void saveSystemSpecifications() {
        systemSpecifications.setSystemBandwidth(Integer.parseInt(systemBandwidth.getText()));
        systemSpecifications.setBandwidthPolicy(bandwidthPolicy.getText());
        systemSpecifications.setBrokerCapacity(Integer.parseInt(brokerCapacity.getText()));
        systemSpecifications.setCommChannelLossAN(Integer.parseInt(commChannelLossAN.getText()));
        systemSpecifications.setCommChannelLossRT(Integer.parseInt(commChannelLossRT.getText()));
        systemSpecifications.setCommChannelLossTS(Integer.parseInt(commChannelLossTS.getText()));
        systemSpecifications.setCommChannelLossVS(Integer.parseInt(commChannelLossVS.getText()));
        systemSpecifications.setSimulationDuration(Integer.parseInt(durationField.getText()));
        systemSpecifications.setAlias(aliasField.getText());
        systemSpecifications.setGlobalMessageSize(Double.parseDouble(messageField.getText()));
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
        chooser.setTitle("Choose output Location");
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
        int simulationDuration = durationField.getText().isEmpty() ? 0 : Integer.parseInt(durationField.getText());
        String alias = aliasField.getText();
        double globalMessageSize = messageField.getText().isEmpty() ? 0 : Double.parseDouble(messageField.getText());

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
        String path = chooseNGSIOutput();
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
            stage.setOnCloseRequest(event -> loadEntities());
            stage.setOnHidden(paramT -> loadEntities());
            stage.show();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return fxmlLoader;
    }


    @FXML
    public void openAddApp() {
        showPanel("AddApp.fxml", "Application").getController();
    }


    @FXML
    public void openAddDevice() {
        showPanel("AddDevice.fxml", "Device").getController();
    }


}
