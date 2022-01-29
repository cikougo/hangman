package hangman.game;

import hangman.medialab.Dictionary;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class Hangman extends Application {
    @Override
    public void init() throws IOException, InterruptedException {
        try {
            Dictionary dict = new Dictionary("OL31390631M");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        BorderPane layout = new BorderPane();
        Button btn = new Button("hi");
        layout.setTop(btn);
        Scene scene = new Scene(layout, 400, 400);
        scene.getStylesheets().add(Hangman.class.getResource("Hangman.css").toExternalForm());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}