package modelingEntities;

import java.util.UUID;

public class BrokerEntity extends BaseEntity {

    private String brokerId;
    public BrokerEntity() {
        super(320, 170, 100, 100);

        this.getRectangle().setStyle("-fx-fill: #3f9e8a; -fx-stroke: #000000; -fx-stroke-width: 2px;");
        this.getEntityName().setText("Broker");
    }


    public String getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(String brokerId) {
        this.brokerId = brokerId;
    }

    public String toString() {
        return brokerId+","+super.toString();
    }
}
