module edict.edict_gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.json;
    requires org.apache.commons.lang3;
    requires org.controlsfx.controls;
	requires com.fasterxml.jackson.annotation;
	requires com.fasterxml.jackson.databind;
	requires java.prefs;
    requires com.opencsv;

    opens customControls;
    opens home to javafx.fxml;
    opens modelingEntities to javafx.base,com.fasterxml.jackson.databind;
    opens guimodel to javafx.base,com.fasterxml.jackson.databind;
    exports home;
}
