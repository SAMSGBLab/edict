package modelingEntities;

public class ApplicationEntity extends BaseEntity {

    public ApplicationEntity(double width, double height) {
        super(50, 20, width, height);
        this.getRectangle().setStyle("-fx-fill: #3fb0dc; -fx-stroke: #000000; -fx-stroke-width: 2px;");
        this.getEntityName().setText("Application");
    }
}
