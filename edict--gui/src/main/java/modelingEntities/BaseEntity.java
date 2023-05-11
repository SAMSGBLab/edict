package modelingEntities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import home.HomeController;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class BaseEntity extends Pane {

    protected Rectangle rectangle;

    protected Circle leftNode;
    protected Circle rightNode;
    protected Label entityName;
    private double startX;
    private double startY;
    protected Arrow arrow;
    public boolean isSelected = false;

    public BaseEntity(double x, double y, double width, double height) {
        this.setLayoutX(0);
        this.setLayoutY(0);
        this.setTranslateX(x);
        this.setTranslateY(y);

        rectangle = new Rectangle(width, height, Color.GRAY);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(2);
        rectangle.setX(0);
        rectangle.setY(0);

        entityName = new Label();
        entityName.setLayoutX(0);
        entityName.setLayoutY((height/2)-10);
        entityName.setText("");
        entityName.setPrefWidth(width);
        entityName.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 10px; -fx-font-weight: bold; -fx-alignment: center;");

        this.setOnMousePressed(event -> {
            isSelected = !isSelected;
            Pane parent = (Pane) this.getParent();
            for (Node n : parent.getChildren()) {
                if (n == this)
                    continue;
                if (n instanceof BaseEntity) {
                    ((BaseEntity) n).isSelected = false;
                    ((BaseEntity) n).rectangle.setStroke(Color.BLACK);
                }
            }
            if (isSelected) {
                rectangle.setStroke(Color.valueOf("#0057FF"));
            } else {
                rectangle.setStroke(Color.BLACK);
            }
            startX = event.getSceneX() - getTranslateX();
            startY = event.getSceneY() - getTranslateY();
        });

    }

    public void makeDraggable(Node node) {


        node.setOnMouseDragged(e -> {
            if (!(e.getTarget() == rectangle)) {
                return;
            }
            double oldX = node.getTranslateX();
            double oldY = node.getTranslateY();
            double arrowX=0;
            double arrowY=0;
            if(arrow != null) {
               arrowX = arrow.getEndX();
               arrowY = arrow.getEndY();
            }

            double newX = e.getSceneX() - startX;
            double newY = e.getSceneY() - startY;

            Pane parent = (Pane) node.getParent();

            if (newX < 0) {
                newX = 0;
            }
            if (newX > parent.getWidth() - 100) {
                newX = parent.getWidth() - 100;
            }
            if (newY < 0) {
                newY = 0;
            }
            if (newY > parent.getHeight() - 50) {
                newY = parent.getHeight() - 50;
            }
            node.setTranslateX(newX);
            node.setTranslateY(newY);
            if(arrow != null) {
                arrow.updateArrow(arrowX-newX+oldX,arrowY-newY+oldY);
            }
        });
    }

    public Arrow getArrow() {
        return arrow;
    }

    public void setArrow(Arrow arrow) {
        this.arrow = arrow;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Label getEntityName() {
        return entityName;
    }


    public void setEntityName(String name) {
        this.entityName.setText(name);
    }

    @Override
    public String toString() {
        return (Math.round(this.getTranslateX() * 100.0) / 100.0)+","+(Math.round(this.getTranslateY() * 100.0) / 100.0)+","+(this.arrow!=null?this.arrow.toString():",,,");
    }
}
