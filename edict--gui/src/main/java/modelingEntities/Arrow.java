package modelingEntities;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;

public class Arrow extends Group {
    private final Line line;
    private Label label;

    public Arrow(double startX, double startY, double endX, double endY) {
        line = new Line(startX, startY, endX, endY);

        label = new Label();

        label.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 13px; -fx-font-style: italic; -fx-font-weight: bold; -fx-font-family: 'Helvetica';");


        this.getChildren().addAll(line, label);
        line.startXProperty().addListener((observable, oldValue, newValue) -> updateLabelPosition());
        line.startYProperty().addListener((observable, oldValue, newValue) -> updateLabelPosition());
        line.endXProperty().addListener((observable, oldValue, newValue) -> updateLabelPosition());
        line.endYProperty().addListener((observable, oldValue, newValue) -> updateLabelPosition());
        Platform.runLater(this::updateLabelPosition);
    }
    public void updateLabelPosition() {
        double startX = line.getStartX();
        double startY = line.getStartY();
        double endX = line.getEndX();
        double endY = line.getEndY();
        double angle = Math.atan2((endY - startY), (endX - startX)) * 180 / Math.PI;
        label.setRotate(angle);
        if (Math.abs(angle) > 90) {
            label.setRotate(angle+180);
        }

        double labelWidth = label.getBoundsInLocal().getWidth();
        double labelHeight = label.getBoundsInLocal().getHeight();
        double labelX;
        double labelY;
        double midX = (startX + endX) / 2;
        double midY = (startY + endY) / 2;
        double distance = 10;
        double offsetX = distance * Math.sin(Math.toRadians(angle));
        double offsetY = distance * Math.cos(Math.toRadians(angle));
        if (angle > 90 || angle < -90) {
            labelX = midX - offsetX;
            labelY = midY + offsetY;
        } else {
            labelX = midX + offsetX;
            labelY = midY - offsetY;
        }


        label.setLayoutX(labelX - labelWidth / 2);
        label.setLayoutY(labelY - labelHeight / 2);
    }




    public void updateArrowEnd(double newEndX, double newEndY) {
        line.setEndX(newEndX);
        line.setEndY(newEndY);

    }
    public void updateArrowStart(double newStartX, double newStartY) {
        line.setStartX(newStartX);
        line.setStartY(newStartY);
    }

    public Double getStartX() {
        return line.getStartX();
    }
    public Double getStartY() {
        return line.getStartY();
    }
    public Double getEndX() {
        return line.getEndX();
    }
    public Double getEndY() {
        return line.getEndY();
    }
    public Label getLabel() {
        return label;
    }
    public void setLabel(Label label) {
        this.label = label;
    }
    @Override
    public String toString() {
        return getStartX()+","+getStartY()+","+getEndX()+","+getEndY();
    }
}