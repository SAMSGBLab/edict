package home;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Home.fxml"));
        Scene scene =new Scene(root);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/style/textField.css").toExternalForm());
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

}
