package modelingEntities;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

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
        entityName.setPrefHeight(20);
        entityName.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 10px; -fx-font-weight: bold; -fx-alignment: center;");

    }

    public void makeDraggable(Node node) {
        entityName.setOnMouseEntered(e -> {
            setCursor(Cursor.HAND);

        });
        entityName.setOnMousePressed(event -> {
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

        entityName.setOnMouseDragged(e -> {

                rectangle.setCursor(Cursor.MOVE);
                double oldX = node.getTranslateX();
                double oldY = node.getTranslateY();
                double arrowX = 0;
                double arrowY = 0;
                if (arrow != null) {
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
                if (arrow != null) {
                    arrow.updateArrowEnd(arrowX - newX + oldX, arrowY - newY + oldY);
                }

        });

        rectangle.setOnMouseMoved(e -> {
            double mouseX = e.getX();
            double width = rectangle.getWidth();
            double edgeSize = 10;

            if (mouseX > width - edgeSize) {
                rectangle.setCursor(Cursor.H_RESIZE);
            } else {
                rectangle.setCursor(Cursor.DEFAULT);
            }
        });

        rectangle.setOnMouseDragged(e -> {
            double mouseX = e.getX();
            double width = rectangle.getWidth();
            double height = rectangle.getHeight();
            double edgeSize = 10;

            double minWidth = 50;
            double maxWidth = 200;

            if (mouseX > width - edgeSize) {
                if (mouseX >= minWidth && mouseX <= maxWidth) {
                    double aspectRatio = width / height;
                    double newHeight = mouseX / aspectRatio;
                    rectangle.setWidth(mouseX);
                    rectangle.setHeight(newHeight);

                    entityName.setPrefWidth(mouseX);
                    entityName.setLayoutY((newHeight / 2) - 10);
//                    entityName.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: " + (newHeight / 5) + "px; -fx-font-weight: bold; -fx-alignment: center;");
                    if (leftNode != null) {
                        leftNode.setCenterX(0);
                        leftNode.setCenterY(newHeight / 2);
                        arrow.updateArrowStart(0, newHeight / 2);

                    }
                    if (rightNode != null) {
                        rightNode.setCenterX(mouseX);
                        rightNode.setCenterY(newHeight / 2);
                        arrow.updateArrowStart(mouseX, newHeight / 2);

                    }
                }
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
        return (Math.round(this.getTranslateX() * 100.0) / 100.0)+","+(Math.round(this.getTranslateY() * 100.0) / 100.0)+","+rectangle.getWidth()+","+rectangle.getHeight();
    }
}
