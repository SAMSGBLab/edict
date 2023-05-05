package modelingEntities;

import guimodel.Application;

public class ApplicationEntity extends BaseEntity {
    private Application application;
    public ApplicationEntity(double x,double y) {
        super(x, y, 70, 70);
        this.getRectangle().setStyle("-fx-fill: #3fb0dc; -fx-stroke: #000000; -fx-stroke-width: 2px;");
        this.getEntityName().setText("Application");
        makeDraggable(this);

    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }
    public String toString() {
        return application.toString() + "," + super.toString();
    }
}
