package hangman.game.popup;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class LoadPopUp extends PopUp {
    public LoadPopUp() {
        super();

        popUp.setResizable(false);
        popUp.setTitle("Load Dictionary");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoadPopUp.fxml"));
        AnchorPane rootLayout = new AnchorPane(new Label("Something went wrong!"));
        try {
            rootLayout = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(rootLayout, 300, 200);
        popUp.setScene(scene);
        popUp.show();
    }

}
