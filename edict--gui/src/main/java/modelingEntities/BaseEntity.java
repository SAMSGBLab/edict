package modelingEntities;

import home.HomeController;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class BaseEntity extends Pane {

    private Rectangle rectangle;
    private Circle topNode;
    private Circle bottomNode;
    private Circle leftNode;
    private Circle rightNode;
    private TextField entityName;
    private double startX;
    private double startY;
    public boolean isSelected = false;
    public BaseEntity(double x, double y, double width, double height) {
        this.setLayoutX(0);
        this.setLayoutY(0);
        this.setTranslateX(x);
        this.setTranslateY(y);
        double radius = 7;

        // create the rectangle and set its properties
        rectangle = new Rectangle(width, height, Color.GRAY);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(2);
        rectangle.setX(0);
        rectangle.setY(0);

        entityName = new TextField();
        entityName.setLayoutX(0);
        entityName.setLayoutY(10);
        entityName.setText("");
        entityName.setEditable(false);
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

        this.setOnMouseClicked(event -> {
            isSelected = !isSelected;
            Pane parent = (Pane) this.getParent();
            for (Node n : parent.getChildren()) {
                if(n==this)
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

        });
        this.getChildren().addAll(rectangle, topNode, bottomNode, leftNode, rightNode, entityName);
        makeDraggable(this);

    }

    private void makeDraggable(Node node) {
        node.setOnMousePressed(e -> {
            startX = e.getSceneX() - node.getTranslateX();
            startY = e.getSceneY() - node.getTranslateY();
        });

        node.setOnMouseDragged(e -> {
            if(!(e.getTarget()==rectangle)) {
                return;
            }
            double newX = e.getSceneX() - startX;
            double newY = e.getSceneY() - startY;

            Pane parent = (Pane) node.getParent();

            if (newX < 0) {
                newX = 0;
            }
            if (newX> parent.getWidth()-100) {
                newX = parent.getWidth()-100;
            }
            if (newY < 0) {
                newY = 0;
            }
            if (newY  > parent.getHeight()-50) {
                newY = parent.getHeight() -50;
            }

            node.setTranslateX(newX);
            node.setTranslateY(newY);
        });
    }
    private void makeConnectable(Circle node) {
        node.setOnMousePressed(event -> {
            Arrow arrow = new Arrow(node.getCenterX(), node.getCenterY(), event.getX(), event.getY());
            getChildren().add(arrow);
        });
    }
    public Rectangle getRectangle() {
        return rectangle;
    }

    public Circle getTopNode() {
        return topNode;
    }

    public Circle getBottomNode() {
        return bottomNode;
    }

    public Circle getLeftNode() {
        return leftNode;
    }

    public Circle getRightNode() {
        return rightNode;
    }

    public TextField getEntityName() {
        return entityName;
    }
}
