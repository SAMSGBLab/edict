package modelingEntities;

import guimodel.Application;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ApplicationEntity extends BaseEntity {
    private Application application;
    private Label applicationCategory;
    public ApplicationEntity(double x,double y,double width,double height) {
        super(x, y, width, height);
        this.getRectangle().setStyle("-fx-fill: #3fb0dc; -fx-stroke: #000000; -fx-stroke-width: 2px;");
        this.getEntityName().setText("Application");
        makeDraggable(this);
        double radius = 7;
        leftNode = new Circle(radius, Color.RED);
        leftNode.setCenterX(0);
        leftNode.setCenterY(rectangle.getHeight() / 2);
        applicationCategory = new Label();
        applicationCategory.setLayoutX((rectangle.getWidth()/2));
        applicationCategory.setLayoutY((rectangle.getHeight()/2)+10);

        arrow = new Arrow(leftNode.getCenterX(), leftNode.getCenterY(), 420-this.getTranslateX(), 170+50-this.getTranslateY());
        getChildren().addAll(arrow,applicationCategory);

        this.getChildren().addAll(rectangle,leftNode,entityName);
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Label getApplicationCategory() {
        return applicationCategory;
    }

    public void setApplicationCategory(Label applicationCategory) {
        this.applicationCategory = applicationCategory;
    }

    public String toString() {
        return application.toString() + "," + super.toString();
    }
}
