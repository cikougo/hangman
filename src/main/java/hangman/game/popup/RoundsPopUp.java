package hangman.game.popup;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class RoundsPopUp extends PopUp {
    public RoundsPopUp(int width, int height) {
        super(width, height);

        popUp.setResizable(false);
        popUp.setTitle("Rounds History");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RoundsPopUp.fxml"));
        AnchorPane rootLayout = new AnchorPane(new Label("Something went wrong!"));
        try {
            rootLayout = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(rootLayout, width, height);
        popUp.setScene(scene);
        popUp.show();
    }
}
