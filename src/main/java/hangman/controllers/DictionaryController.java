package hangman.controllers;

import hangman.medialab.Dictionary;
import javafx.fxml.FXML;

import javafx.scene.control.Label;


import static hangman.game.GameSession.getActiveDictionary;

public class DictionaryController {

    @FXML
    private Label lessThan6;
    @FXML
    private Label between7And9;
    @FXML
    private Label moreThan10;
    @FXML
    private Label dictId;

    public void initialize() {
        Dictionary dict = getActiveDictionary();
        dictId.setText(dict.getDictionaryId());
        lessThan6.setText(String.valueOf(dict.getLessThan6() * 100) + "%");
        between7And9.setText(String.valueOf(dict.getBetween7And9() * 100) + "%");
        moreThan10.setText(String.valueOf(dict.getMoreThan10() * 100) + "%");
    }
}