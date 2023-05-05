package modelingEntities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dataParser.DataParser;
import guimodel.Device;
import javafx.scene.Node;

public class DeviceEntity extends BaseEntity {

    private Device device;

    public DeviceEntity(double x, double y) {
        super(x, y, 70, 70);

        this.getRectangle().setStyle("-fx-fill: #e59636; -fx-stroke: #000000; -fx-stroke-width: 2px;");
        this.getEntityName().setText("Device");
        makeDraggable(this);

    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Device getDevice() {
        return device;
    }

    @Override
    public String toString() {
        return device.toString() + "," + super.toString();
    }
}
