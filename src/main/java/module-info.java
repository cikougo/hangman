module game.hangman {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires javafx.web;


    opens hangman.game to javafx.fxml;
    exports hangman.game;
    exports hangman.controllers;
    opens hangman.controllers to javafx.fxml;
}