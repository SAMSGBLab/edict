package modelingEntities;

public class BrokerEntity extends BaseEntity {

    public BrokerEntity(double width, double height) {
        super(100, 100, width, height);
        this.getRectangle().setStyle("-fx-fill: #3f9e8a; -fx-stroke: #000000; -fx-stroke-width: 2px;");
        this.getEntityName().setText("Broker");
    }
}
