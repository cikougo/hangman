module game.hangman {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;


    opens hangman.game to javafx.fxml;
    exports hangman.game;
}