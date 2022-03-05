package hangman.controllers;

public class HistoryRow {

    private String word;
    private int tries;
    private String result;

    public HistoryRow() {
    }

    public HistoryRow(String word, int tries, String result) {
        this.word = word;
        this.tries = tries;
        this.result = result;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
