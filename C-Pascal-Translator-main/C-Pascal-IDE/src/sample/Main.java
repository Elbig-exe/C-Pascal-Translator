package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root;
        root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene scene= new Scene(root,1000,1000);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        scene.setFill(null);
        primaryStage.setTitle("C--");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

    }

}
