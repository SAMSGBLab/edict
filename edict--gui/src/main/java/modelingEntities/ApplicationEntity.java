package modelingEntities;

import guimodel.Application;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ApplicationEntity extends BaseEntity {
    private Application application;
    private Label applicationCategoryLabel;
    public ApplicationEntity(double x,double y,double width,double height) {
        super(x, y, width, height);
        this.getRectangle().setStyle("-fx-fill: #3fb0dc; -fx-stroke: #000000; -fx-stroke-width: 2px;");
        this.getEntityName().setText("Application");
        makeDraggable();
        double radius = 7;
        leftNode = new Circle(radius, Color.RED);
        leftNode.setCenterX(0);
        leftNode.setCenterY(rectangle.getHeight() / 2);
        applicationCategoryLabel = new Label();
        applicationCategoryLabel.setLayoutX(0);
        applicationCategoryLabel.translateYProperty().bind(this.getEntityName().translateYProperty().add(40));
        applicationCategoryLabel.setPrefHeight(10);
        this.rectangle.widthProperty().addListener((observable, oldValue, newValue) -> {
            applicationCategoryLabel.setTranslateX(newValue.doubleValue()*0.7);
        });

        arrow = new Arrow(leftNode.getCenterX(), leftNode.getCenterY(), 420-this.getTranslateX(), 220-this.getTranslateY());
        getChildren().addAll(arrow);

        this.getChildren().addAll(rectangle,leftNode,entityName, applicationCategoryLabel);
    }
    public void splitArrow(){
        String[] obs=arrow.getLabel().getText().split(" ");
        arrow.getLabel().setText(obs[0]);
        if(obs.length>1) {
            double distanceBetweenNodes = rectangle.getHeight() / (obs.length);
            double start = rectangle.getHeight() / 2 - distanceBetweenNodes * (obs.length - 1) / 2;
            leftNode.setCenterY(start);
            arrow.updateArrowStart(leftNode.getCenterX(), leftNode.getCenterY());
            for (int i = 1; i < obs.length; i++) {
                Circle circle = new Circle(7, Color.RED);
                circle.setCenterX(leftNode.getCenterX());
                circle.setCenterY(leftNode.getCenterY() + distanceBetweenNodes * i);
                Arrow arrow = new Arrow(circle.getCenterX(), circle.getCenterY(), 420 - this.getTranslateX(), 220 - this.getTranslateY());
                arrow.getLabel().setText(obs[i]);

                getChildren().addAll(circle, arrow);
            }
        }
    }
    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Label getApplicationCategoryLabel() {
        return applicationCategoryLabel;
    }

    public void setApplicationCategoryLabel(Label applicationCategoryLabel) {
        this.applicationCategoryLabel = applicationCategoryLabel;
    }

    public String toString() {
        return application.toString() + "," + super.toString();
    }
}
