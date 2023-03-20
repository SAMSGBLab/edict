module edict.edict_gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.json;
    requires org.apache.commons.lang3;
    requires com.opencsv;
    requires org.controlsfx.controls;
    
    opens home to javafx.fxml;
    exports home;
}
