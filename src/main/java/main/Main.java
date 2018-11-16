package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/Window.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("FlexTid");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

    }
}
