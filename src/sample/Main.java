package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    // create the stage for the application
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Edmonton Property Assessments and Crime Occurrences");
        primaryStage.setScene(new Scene(root, 1200.0D, 800.0D));
        primaryStage.show();
    }

    // main method
    public static void main(String[] args) {
        launch(args);
    }
}
