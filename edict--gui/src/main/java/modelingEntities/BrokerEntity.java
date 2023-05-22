package modelingEntities;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.UUID;

public class BrokerEntity extends BaseEntity {

    public BrokerEntity() {
        super(320, 170, 100, 100);
        double radius = 7;

        this.getRectangle().setStyle("-fx-fill: #3f9e8a; -fx-stroke: #000000; -fx-stroke-width: 2px;");
        this.getEntityName().setText("Messaging System");
        Color color = Color.RED;
        double width = this.getRectangle().getWidth();
        double height;
        height = this.getRectangle().getHeight();


        leftNode = new Circle(radius,color);
        leftNode.setCenterX(0);
        leftNode.setCenterY(height / 2);
        rightNode = new Circle(radius,color);
        rightNode.setCenterX(width);
        rightNode.setCenterY(height / 2);
        this.getChildren().addAll(rectangle,leftNode, rightNode, entityName);

    }

}
