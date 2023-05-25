package modelingEntities;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.Collection;

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

        double[] triangleCoordinates = {width, height, width, height - 10, width - 10, height};


        entityName = new Label();
        entityName.setLayoutX(0);
        entityName.setPrefWidth(width - 15);
        entityName.setPrefHeight(40);
        entityName.setTextAlignment(TextAlignment.CENTER);
        entityName.setTranslateX(rectangle.getWidth() / 2 - entityName.getPrefWidth() / 2);
        entityName.setTranslateY(rectangle.getHeight() / 2 - entityName.getPrefHeight() / 2);

        entityName.setWrapText(true);
        entityName.setStyle("-fx-background-color: transparent;-fx-text-fill: black; -fx-font-size: 10px;-fx-font-weight: bold; -fx-alignment: center;");
    }

    public void makeDraggable() {
        entityName.setOnMouseEntered(e -> setCursor(Cursor.HAND));
        entityName.setOnMouseExited(e -> setCursor(Cursor.DEFAULT));
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
            double oldX = getTranslateX();
            double oldY = getTranslateY();
            double arrowX = 0;
            double arrowY = 0;
            if (arrow != null) {
                arrowX = arrow.getEndX();
                arrowY = arrow.getEndY();
            }

            double newX = e.getSceneX() - startX;
            double newY = e.getSceneY() - startY;

            Pane parent = (Pane) getParent();

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
            setTranslateX(newX);
            setTranslateY(newY);
            if (arrow != null) {
                for (Node a : this.getChildren().filtered(n -> n instanceof Arrow)) {
                    ((Arrow) a).updateArrowEnd(arrowX - newX + oldX, arrowY - newY + oldY);
                }
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
                    entityName.setPrefWidth(mouseX - 10);
                    entityName.setTranslateX(rectangle.getWidth() / 2 - entityName.getPrefWidth() / 2);
                    entityName.setTranslateY(rectangle.getHeight() / 2 - entityName.getPrefHeight() / 2);
                    if (leftNode != null) {
                        int count = this.getChildren().filtered(n -> n instanceof Circle).size();
                        if (count == 1) {
                            leftNode.setCenterX(0);
                            leftNode.setCenterY(newHeight / 2);
                            arrow.updateArrowStart(0, newHeight / 2);
                        } else {
                            double distanceBetweenNodes = rectangle.getHeight() / count;
                            double start = rectangle.getHeight() / 2 - distanceBetweenNodes * (count - 1) / 2;
                            leftNode.setCenterY(start);
                            Object[] circles = this.getChildren().filtered(n -> n instanceof Circle).toArray();
                            for (int i = 1; i < circles.length; i++) {
                                ((Circle) circles[i]).setCenterX(0);
                                ((Circle) circles[i]).setCenterY(start + distanceBetweenNodes * i);

                            }
                            Object[] arrows = this.getChildren().filtered(n -> n instanceof Arrow).toArray();
                            for (int i = 0; i < arrows.length; i++) {
                                ((Arrow) arrows[i]).updateArrowStart(0, start + distanceBetweenNodes * i);
                            }
                        }
                    }
                    if (rightNode != null) {
                        int count = this.getChildren().filtered(n -> n instanceof Circle).size();
                        if (count == 1) {
                            rightNode.setCenterX(mouseX);
                            rightNode.setCenterY(newHeight / 2);
                            arrow.updateArrowStart(mouseX, newHeight / 2);
                        } else {
                            double distanceBetweenNodes = rectangle.getHeight() / count;
                            double start = rectangle.getHeight() / 2 - distanceBetweenNodes * (count - 1) / 2;
                            rightNode.setCenterX(mouseX);
                            rightNode.setCenterY(start);
                            Object[] circles = this.getChildren().filtered(n -> n instanceof Circle).toArray();
                            for (int i = 1; i < circles.length; i++) {
                                ((Circle) circles[i]).setCenterX(rightNode.getCenterX());
                                ((Circle) circles[i]).setCenterY(start + distanceBetweenNodes * i);

                            }
                            Object[] arrows = this.getChildren().filtered(n -> n instanceof Arrow).toArray();
                            for (int i = 0; i < arrows.length; i++) {
                                ((Arrow) arrows[i]).updateArrowStart(mouseX, start + distanceBetweenNodes * i);
                            }
                        }

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
        return (Math.round(this.getTranslateX() * 100.0) / 100.0) + "," + (Math.round(this.getTranslateY() * 100.0) / 100.0) + "," + rectangle.getWidth() + "," + rectangle.getHeight();
    }
}
