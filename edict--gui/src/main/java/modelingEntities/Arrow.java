package modelingEntities;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class Arrow extends Group {
    private final Line line;
    private final Polygon head;

    public Arrow(double startX, double startY, double endX, double endY) {
        line = new Line(startX, startY, endX, endY);
        head = new Polygon();
        head.getPoints().addAll(new Double[]{
                0.0, 10.0,
                10.0, 0.0,
                0.0, -10.0});
        head.setFill(Color.BLACK);
        head.setStroke(Color.BLACK);
        head.setStrokeWidth(1);

        getChildren().addAll(line, head);

        this.setOnMouseDragged(event -> {
            line.setEndX(event.getX());
            line.setEndY(event.getY());
            updateArrow();
        });
        head.setOnMouseEntered(event -> {
            head.getScene().setCursor(Cursor.HAND);
        });

        head.setOnMouseExited(event -> {
            head.getScene().setCursor(Cursor.DEFAULT);
        });
        updateArrow();
    }

    private void updateArrow() {
        double startX = line.getStartX();
        double startY = line.getStartY();
        double endX = line.getEndX();
        double endY = line.getEndY();

        double angle = Math.atan2((endY - startY), (endX - startX)) * 180 / Math.PI;
        head.setRotate(angle);
        head.setTranslateX(endX);
        head.setTranslateY(endY);
    }
    public void setStartX(double startX) {
        line.setStartX(startX);
        updateArrow();
    }
    public void setStartY(double startY) {
        line.setStartY(startY);
        updateArrow();
    }
    public void setEndX(double endX) {
        line.setEndX(endX);
        updateArrow();
    }
    public void setEndY(double endY) {
        line.setEndY(endY);
        updateArrow();
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

}