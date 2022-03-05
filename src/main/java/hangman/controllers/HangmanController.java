package hangman.controllers;

import hangman.game.GameSession;
import hangman.game.Hangman;
import hangman.game.popup.CreatePopUp;
import hangman.game.popup.DictionaryPopUp;
import hangman.game.popup.LoadPopUp;
import hangman.game.popup.RoundsPopUp;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;


import java.net.URL;
import java.util.List;

import static java.lang.Integer.parseInt;

public class HangmanController {
    private HangmanController controller = this;
    private GameSession activeGameSession;
    private List<GameSession> gameHistory;

    @FXML
    private Label possWords;
    @FXML
    private Label totalPoints;
    @FXML
    private Label accuracy;
    @FXML
    private Label mainText;
    @FXML
    private MenuItem applicationStart;
    @FXML
    private MenuItem applicationLoad;
    @FXML
    private MenuItem applicationCreate;
    @FXML
    private MenuItem applicationExit;
    @FXML
    public HBox gameState;
    @FXML
    private ChoiceBox selectLetter;
    @FXML
    private Button submitLetter;
    @FXML
    private MenuItem detailsDictionary;
    @FXML
    private MenuItem detailsRounds;
    @FXML
    private MenuItem detailsSolution;
    @FXML
    private WebView webview;


    @FXML
    public Label selectedLetter;
    @FXML
    public VBox availableLetters;

    public void initialize() {
        // menu event handlers
        applicationStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (activeGameSession != null && activeGameSession.isInProgress()) {
                    activeGameSession.surrenderGame();
                }
                if (GameSession.getActiveDictionary() == null) {
                    setMainText("No Active Dictionary!");
                    return;
                }
                activeGameSession = new GameSession();
                setMainText("");
                activeGameSession.start();
            }
        });
        applicationLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new LoadPopUp();
            }
        });
        applicationCreate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new CreatePopUp();
            }
        });
        applicationExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
        detailsDictionary.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new DictionaryPopUp();
            }
        });
        detailsRounds.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new RoundsPopUp(400, 350);
            }
        });
        detailsSolution.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                activeGameSession.surrenderGame();
            }
        });


        selectedLetter.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println(newValue);
                submitLetter.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if ((activeGameSession != null) && activeGameSession.isInProgress()) {
                            activeGameSession.makeChoice(parseInt(newValue), (String) selectLetter.getValue());
                        }
                    }
                });
            }
        });

        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < alphabet.length(); i++) {
            selectLetter.getItems().add(String.valueOf(alphabet.charAt(i)));
        }
    }

    public void setHangmanState(int errors) {
        System.out.println("hangman" + errors + ".svg");
        URL url = Hangman.class.getResource("hangman" + errors + ".svg");
        System.out.println(url);

        webview.getEngine().load(url.toExternalForm());
    }

    public void setMainText(String value) {
        mainText.setText(value);
    }


    // top interface controllers
    public void setPossWords(String value) {
        possWords.setText(value);
    }

    public void setTotalPoints(String value) {
        totalPoints.setText(value);
    }

    public void setAccuracy(String value) {
        accuracy.setText(value);
    }
}