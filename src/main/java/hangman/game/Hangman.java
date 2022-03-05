package hangman.game;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Hangman extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException, InterruptedException {
        // only layout here, not logic
        FXMLLoader loader = new FXMLLoader(Hangman.class.getResource("Hangman.fxml"));
        BorderPane rootLayout = (BorderPane) loader.load();
        Scene scene = new Scene(rootLayout, 800, 600);
        GameSession.init(loader.getController());
        primaryStage.setMinWidth(scene.getWidth());
        primaryStage.setMinHeight(scene.getHeight() + 50);
        primaryStage.setTitle("MediaLab Hangman");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}