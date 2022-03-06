package hangman.controllers;

import hangman.game.GameSession;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class RoundsController {
    @FXML
    private TableView<HistoryRow> table;
    @FXML
    private TableColumn<HistoryRow, String> word;
    @FXML
    private TableColumn<HistoryRow, Integer> tries;
    @FXML
    private TableColumn<HistoryRow, String> result;


    public void initialize() {
        List<GameSession> history = GameSession.getGameHistory();
        if (history.isEmpty()) {
            return;
        }
        word.setCellValueFactory(new PropertyValueFactory<HistoryRow, String>("word"));
        tries.setCellValueFactory(new PropertyValueFactory<HistoryRow, Integer>("tries"));
        result.setCellValueFactory(new PropertyValueFactory<HistoryRow, String>("result"));

        for (int i = history.size() - 1; i >= Math.max(history.size() - 5, 0); i--) {
            GameSession current = history.get(i);
            
            table.getItems().add(new HistoryRow(current.getActiveWord(), current.getNumOfChoices(), current.getGameResult()));
        }

    }


}
