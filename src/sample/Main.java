package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
            BorderPane root = loader.load();

            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.setTitle("PixelWriter Test");
            primaryStage.show();

            Controller controller = loader.getController();
            controller.init();
            primaryStage.setOnCloseRequest((we -> controller.setClosed()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
