package modelingEntities;

import guimodel.Device;
import home.AddDeviceController;
import home.HomeController;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DeviceEntity extends BaseEntity {
    private Device device;

    public DeviceEntity(double width, double height) {
        super(20, 20, width, height);

        this.getRectangle().setStyle("-fx-fill: #e59636; -fx-stroke: #000000; -fx-stroke-width: 2px;");
        this.getEntityName().setText("Device");
        this.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                HomeController h = new HomeController();

                AddDeviceController controller = showPanel("AddDevice.fxml", "Device").getController();
                if(this.device!=null)
                    controller.initData(this.device);
                controller.initEntity(this);
            }
        });
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

//                    updateList();
                }
            });
            stage.setOnHidden(new EventHandler<WindowEvent>() {

                @Override
                public void handle(WindowEvent paramT) {
//                    updateList();

                }
            });
            stage.show();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return fxmlLoader;
    }
    public void setDevice(Device device) {
        this.device = device;
    }

    public Device getDevice() {
        return device;
    }
}
