package modelingEntities;

import guimodel.Device;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class DeviceEntity extends BaseEntity {

    private Device device;

    public DeviceEntity(double x, double y,double width,double height) {
        super(x, y, width, height);

        this.getRectangle().setStyle("-fx-fill: #e59636; -fx-stroke: #000000; -fx-stroke-width: 2px;");
        this.getEntityName().setText("Device");
        double radius = 7;
        rightNode = new Circle(radius, Color.RED);
        rightNode.setCenterX(rectangle.getWidth());
        rightNode.setCenterY(rectangle.getHeight() / 2);

        arrow = new Arrow(rightNode.getCenterX(), rightNode.getCenterY(), 320-this.getTranslateX(), 220-this.getTranslateY());
        getChildren().add(arrow);


        makeDraggable();
        this.getChildren().addAll(rectangle, rightNode, entityName);
    }
    public void splitArrow(){
        String[] obs=arrow.getLabel().getText().split(" ");
        arrow.getLabel().setText(obs[0]);
        if(obs.length>1) {
            double distanceBetweenNodes = rectangle.getHeight() / (obs.length);
            double start = rectangle.getHeight() / 2 - distanceBetweenNodes * (obs.length - 1) / 2;
            rightNode.setCenterY(start);
            arrow.updateArrowStart(rightNode.getCenterX(), rightNode.getCenterY());
            for (int i = 1; i < obs.length; i++) {
                Circle circle = new Circle(7, Color.RED);
                circle.setCenterX(rightNode.getCenterX());
                circle.setCenterY(rightNode.getCenterY() + distanceBetweenNodes * i);
                Arrow arrow = new Arrow(circle.getCenterX(), circle.getCenterY(), 320 - this.getTranslateX(), 220 - this.getTranslateY());
                arrow.getLabel().setText(obs[i]);

                getChildren().addAll(circle, arrow);
            }
        }
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
