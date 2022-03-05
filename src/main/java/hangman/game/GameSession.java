package hangman.game;

import hangman.controllers.HangmanController;
import hangman.medialab.Dictionary;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class GameSession {
    private static HangmanController controller;
    private static Dictionary activeDictionary = null;
    private static List<GameSession> gameHistory;

    private int totalPoints = 0;
    private int numOfChoices = 0;
    private int numOfCorrectChoices = 0;
    private boolean inProgress = false;
    private String activeWord = null;
    private List<String> gameState;
    private List<String> solution;
    private int errors = 0;
    private int selectedLetter = 0;
    private List<HashMap<String, Float>> probabilities;
    private String gameResult = null;

    public static void init(HangmanController hangmanController) {
        controller = hangmanController;
        gameHistory = new ArrayList<>();
    }

    public void start() {
        inProgress = true;
        controller.setAccuracy("0.0%");
        controller.setTotalPoints("0");
        controller.setMainText("");
        Random rand = new Random();
        activeWord = activeDictionary.getWords().get(rand.nextInt(activeDictionary.getWords().size()));
        System.out.println(activeWord);
        gameState = new ArrayList<>(activeWord.length());
        solution = new ArrayList<>(activeWord.length());
        for (int i = 0; i < activeWord.length(); i++) {
            gameState.add(" ");
            solution.add(String.valueOf(activeWord.charAt(i)));
        }
        controller.gameState.getChildren().clear();
        for (int i = 0; i < gameState.size(); i++) {
            final var index = i;
            Button btnLetter = new Button(gameState.get(index));
            btnLetter.setPrefHeight(40);
            btnLetter.setPrefWidth(40);
            // TODO: button style
            btnLetter.setStyle("");
            btnLetter.setId("gameStateButton_" + index);
            btnLetter.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    selectedLetter = index;
                    controller.selectedLetter.setText(String.valueOf(index));
                }
            });
            controller.gameState.getChildren().add(btnLetter);
            controller.selectedLetter.setText(String.valueOf(selectedLetter));
        }
        updateProbabilities();
        updateAvailableLettersInterface();
        controller.setHangmanState(0);
    }

    public void makeChoice(int index, String answer) {
        if (gameState.get(index) == " ") {
            if (activeWord.charAt(index) == answer.charAt(0)) {
                gameState.set(index, answer);
                Button btn = (Button) controller.gameState.lookup("#gameStateButton_" + index);
                btn.setText(answer);
                correctChoice();
                Float prob = probabilities.get(index).get(answer);
                if (prob < 0.25) {
                    updatePoints(30);
                } else if (prob < 0.4) {
                    updatePoints(15);
                } else if (prob < 0.6) {
                    updatePoints(10);
                } else {
                    updatePoints(5);
                }
                updateProbabilities();
                updateAvailableLettersInterface();
            } else {
                wrongChoice();
                updatePoints(-15);
                controller.setHangmanState(errors);
            }
            updateAccuracy();
        }
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void surrenderGame() {
        loseGame();
    }

    public String getGameResult() {
        return gameResult;
    }

    public int getNumOfChoices() {
        return numOfChoices;
    }

    public String getActiveWord() {
        return activeWord;
    }

    private void updateAvailableLettersInterface() {
        controller.availableLetters.getChildren().clear();
        for (int i = 0; i < probabilities.size(); i++) {
            ListView container = new ListView();
            AtomicReference<String> row = new AtomicReference<>(String.valueOf(i) + " " + new String(Character.toChars(0x27A1)) + " ");
            HashMap<String, Float> temp = probabilities.get(i);
            if (temp == null) {
                row.set(row + new String(Character.toChars(0x2714)));
            } else {
                temp.forEach((key, value) -> {
                    row.set(row.get() + key + ", ");
                });
                String str = row.get();
                row.set(str.substring(0, str.length() - 2));
            }
            container.getItems().add(new Label(row.get()));
            controller.availableLetters.getChildren().add(container);
        }
    }

    private void updateProbabilities() {
        probabilities = activeDictionary.getProbabilities(gameState);
    }

    private void updatePoints(int points) {
        totalPoints = Math.max((totalPoints + points), 0);
        controller.setTotalPoints(String.valueOf(totalPoints));
    }

    private void updateAccuracy() {
        float letterAccuracy = ((float) numOfCorrectChoices) / (float) numOfChoices;
        controller.setAccuracy(String.valueOf(letterAccuracy * 100) + "%");
    }

    private void correctChoice() {
        numOfCorrectChoices = numOfCorrectChoices + 1;
        numOfChoices = numOfChoices + 1;
        if (gameState.equals(solution)) {
            System.out.println("game win");
            this.winGame();
        }
    }

    private void wrongChoice() {
        numOfChoices = numOfChoices + 1;
        errors = errors + 1;
        if (errors == 6) {
            System.out.println("game lost");
            this.loseGame();
        }
    }


    private void winGame() {
        inProgress = false;
        gameResult = "Victory";
        controller.setMainText("You Win!");
        gameHistory.add(this);
    }

    private void loseGame() {
        inProgress = false;
        showAnswer();
        gameResult = "Defeat";
        controller.setMainText("You Lose!");
        gameHistory.add(this);
    }

    private void showAnswer() {
        for (int i = 0; i < solution.size(); i++) {
            Button btn = (Button) controller.gameState.lookup("#gameStateButton_" + i);
            btn.setText(solution.get(i));
        }

    }

    public static List<GameSession> getGameHistory() {
        return gameHistory;
    }

    public static void loadDictionary(String dict) throws FileNotFoundException {
        activeDictionary = new Dictionary().load(dict);
        controller.setPossWords(String.valueOf(activeDictionary.getWords().size()));
        controller.setMainText("");
    }

    public static Dictionary getActiveDictionary() {
        return activeDictionary;
    }
}
