package hangman.game.popup;

import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopUp {
    protected Stage popUp;

    public PopUp() {
        popUp = new Stage();
        popUp.setHeight(250);
        popUp.setWidth(300);
        popUp.initModality(Modality.APPLICATION_MODAL);
    }

    public PopUp(int width, int height) {
        popUp = new Stage();
        popUp.setHeight(height);
        popUp.setWidth(width);
        popUp.initModality(Modality.APPLICATION_MODAL);
    }
}
