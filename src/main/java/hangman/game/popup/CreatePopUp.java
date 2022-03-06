package hangman.game.popup;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class CreatePopUp extends PopUp {
    public CreatePopUp() {
        super();

        popUp.setResizable(false);
        popUp.setTitle("Create Dictionary");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CreatePopUp.fxml"));
        AnchorPane rootLayout = new AnchorPane(new Label("Something went wrong!"));
        try {
            rootLayout = (AnchorPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(rootLayout, 300, 200);
        popUp.setScene(scene);
        popUp.show();
    }
}
