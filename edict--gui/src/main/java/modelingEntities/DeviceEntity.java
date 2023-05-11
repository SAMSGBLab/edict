package modelingEntities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dataParser.DataParser;
import guimodel.Device;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class DeviceEntity extends BaseEntity {

    private Device device;

    public DeviceEntity(double x, double y) {
        super(x, y, 70, 70);

        this.getRectangle().setStyle("-fx-fill: #e59636; -fx-stroke: #000000; -fx-stroke-width: 2px;");
        this.getEntityName().setText("Device");
        double radius = 7;
        rightNode = new Circle(radius, Color.RED);
        rightNode.setCenterX(rectangle.getWidth());
        rightNode.setCenterY(rectangle.getHeight() / 2);

        arrow = new Arrow(rightNode.getCenterX(), rightNode.getCenterY(), 320-this.getTranslateX(), 220-this.getTranslateY());
        getChildren().add(arrow);


        makeDraggable(this);
        this.getChildren().addAll(rectangle, rightNode, entityName);
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
