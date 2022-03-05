package hangman.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.FileNotFoundException;

import static hangman.game.GameSession.loadDictionary;

public class LoadController {

    @FXML
    private Button submitLoad;
    @FXML
    private TextField loadInput;
    @FXML
    private Label errorMessage;

    public void initialize() {
        submitLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    loadDictionary(loadInput.getText());
                    errorMessage.setText("");
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                } catch (FileNotFoundException e) {
                    errorMessage.setText("Dictionary does not exist!");
                }
            }
        });
    }
}