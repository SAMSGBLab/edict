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

    private Rectangle rectangle;
    private Circle topNode;
    private Circle bottomNode;
    private Circle leftNode;
    private Circle rightNode;
    private Label entityName;
    private double startX;
    private double startY;
    private List<Arrow> arrows;
    public boolean isSelected = false;

    public BaseEntity(double x, double y, double width, double height) {
        this.setLayoutX(0);
        this.setLayoutY(0);
        this.setTranslateX(x);
        this.setTranslateY(y);
        double radius = 7;
        arrows= new ArrayList<>();

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


        // create circles to represent the nodes on each side of the rectangle
        topNode = new Circle(radius, Color.RED);
        topNode.setCenterX(width / 2);
        topNode.setCenterY(0);
        makeConnectable(topNode);

        bottomNode = new Circle(radius, Color.RED);
        bottomNode.setCenterX(width / 2);
        bottomNode.setCenterY(height);
        makeConnectable(bottomNode);

        leftNode = new Circle(radius, Color.RED);
        leftNode.setCenterX(0);
        leftNode.setCenterY(height / 2);
        makeConnectable(leftNode);
        rightNode = new Circle(radius, Color.RED);
        rightNode.setCenterX(width);
        rightNode.setCenterY(height / 2);
        makeConnectable(rightNode);

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
        this.getChildren().addAll(rectangle, topNode, bottomNode, leftNode, rightNode, entityName);

    }

    public void makeDraggable(Node node) {


        node.setOnMouseDragged(e -> {
            if (!(e.getTarget() == rectangle)) {
                return;
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
            double finalNewX = newX;
            double finalNewY = newY;

            node.setTranslateX(newX);
            node.setTranslateY(newY);
        });
    }

    private void makeConnectable(Circle node) {
        node.setOnMousePressed(event -> {
            Arrow arrow = new Arrow(node.getCenterX(), node.getCenterY(), event.getX(), event.getY());
            getChildren().add(arrow);
            arrows.add(arrow);
        });
    }

    public List<Arrow> getArrows() {
        return arrows;
    }

    public void setArrows(List<Arrow> arrows) {
        this.arrows = arrows;
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
        return Math.round(this.getTranslateX() * 100.0) / 100.0+","+Math.round(this.getTranslateY() * 100.0) / 100.0;
    }
}
