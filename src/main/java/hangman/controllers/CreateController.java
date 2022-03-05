package hangman.controllers;

import hangman.medialab.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.FileNotFoundException;

public class CreateController {

    @FXML
    private Button submitCreate;
    @FXML
    private TextField createInput;
    @FXML
    private Label errorMessage;

    public void initialize() {
        submitCreate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    new Dictionary().create(createInput.getText());
                    errorMessage.setText("");
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                } catch (FileNotFoundException e) {
                    errorMessage.setText("Invalid Dictionary ID");
                } catch (Exception e) {
                    errorMessage.setText(e.getMessage());
                }
            }
        });
    }
}
