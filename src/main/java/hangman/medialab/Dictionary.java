package hangman.medialab;

import java.io.*;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.util.*;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Dictionary {
    private List<String> words;
    private String dictionaryId;

    public Dictionary() {
    }

    public Dictionary load(String bookId) throws FileNotFoundException {
        dictionaryId = bookId;
        this.words = new ArrayList<>();
        String fileDir = "./src/main/java/hangman/medialab/dictionaries/hangman_" + bookId + ".txt";
        File dict = new File(fileDir);
        Scanner dictReader = new Scanner(dict);
        while (dictReader.hasNextLine()) {
            String data = dictReader.nextLine();
            this.words.add(data);
        }
        return this;
    }

    public Dictionary create(String bookId) throws IOException, InvalidCountException, UnderSizeException, InvalidRangeException, UnbalancedException {
        dictionaryId = bookId;
        String fileDir = "./src/main/java/hangman/medialab/dictionaries/hangman_" + bookId + ".txt";
        var url = new URL(String.format("https://openlibrary.org/works/%s.json", bookId));
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        BufferedReader bReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String stringJson;
        stringJson = bReader.readLine();
        JSONParser parser = new JSONParser();
        JSONObject response;
        String corpus;
        try {
            response = (JSONObject) parser.parse(stringJson);
        } catch (ParseException parseException) {
            throw new IOException("Invalid JSON format", parseException);
        }
        try {
            JSONObject description;
            description = (JSONObject) response.get("description");
            corpus = (String) description.get("value");

        } catch (ClassCastException e) {
            corpus = (String) response.get("description");
        }

        words = Arrays.stream(corpus
                        .replaceAll("[^a-zA-Z \n]", " ")
                        .trim()
                        .toUpperCase()
                        .split("\\s+"))
                .filter(w -> w.length() >= 6)
                .distinct()
                .collect(Collectors.toList());


        // InvalidCountException and InvalidRangeException exceptions will never be thrown
        // because of the preprocessing of the response .filter() and .distinct()
        if (hasDuplicateWords(words)) throw new InvalidCountException();
        if (isUnderSized(words)) throw new UnderSizeException();
        if (hasInvalidRange(words)) throw new InvalidRangeException();
        if (isUnbalanced(words)) throw new UnbalancedException();

        // write to file
        File myObj = new File(fileDir);
        myObj.createNewFile();
        FileWriter dictWriter = new FileWriter(fileDir);
        words.forEach(word -> {
            try {
                dictWriter.write(word + '\n');
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        dictWriter.close();
        return this;
    }

    public List<String> getWords() {
        return this.words;
    }

    public String getDictionaryId() {
        return dictionaryId;
    }

    public List<LinkedHashMap<String, Float>> getProbabilities(List<String> gameState) {
        // create subset
        List<String> subsetWords = new ArrayList<>();
        for (int i = 0; i < this.words.size(); i++) {
            if (words.get(i).length() == gameState.size()) {
                subsetWords.add(words.get(i));
            }
        }
        for (int i = 0; i < gameState.size(); i++) {
            String currentLetter = gameState.get(i);
            if (currentLetter != " ") {
                int bound = subsetWords.size();
                for (int j = 0; j < bound; j++) {
                    if (!String.valueOf(subsetWords.get(j).charAt(i)).equals(currentLetter)) {
                        subsetWords.remove(j);
                        j--;
                        bound--;
                    }
                }
            }
        }

        // calc probabilities
        List<HashMap<String, Float>> prob = new ArrayList<>();
        for (int i = 0; i < gameState.size(); i++) {
            HashMap<String, Float> currentProb = null;
            if (gameState.get(i) == " ") {
                currentProb = new HashMap<>();
                for (int j = 0; j < subsetWords.size(); j++) {
                    String currentLetter = String.valueOf(subsetWords.get(j).charAt(i));
                    if (currentProb.containsKey(currentLetter)) {
                        currentProb.put(currentLetter, currentProb.get(currentLetter) + 1);
                    } else {
                        currentProb.put(currentLetter, 1.0F);
                    }
                }
                currentProb.replaceAll((key, oldValue) -> oldValue / subsetWords.size());
            }
            prob.add(currentProb);
        }
        List<LinkedHashMap<String, Float>> sortProb = new ArrayList<>();
        for (int i = 0; i < prob.size(); i++) {
            LinkedHashMap currentSorted = sortProbabilities(prob.get(i));
            sortProb.add(currentSorted);
        }
        return sortProb;
    }

    public Float getLessThan6() {
        int count = 0;
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).length() <= 6) {
                count++;
            }
        }
        return ((float) count) / words.size();
    }

    public Float getBetween7And9() {
        int count = 0;
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).length() > 6 && words.get(i).length() <= 9) {
                count++;
            }
        }
        return ((float) count) / words.size();
    }

    public Float getMoreThan10() {
        int count = 0;
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).length() > 9) {
                count++;
            }
        }
        return ((float) count) / words.size();
    }

    private LinkedHashMap<String, Float> sortProbabilities(HashMap<String, Float> map) {
        if (map == null) {
            return null;
        }
        Set<Map.Entry<String, Float>> entries = map.entrySet();

        Comparator<Map.Entry<String, Float>> valueComparator = new Comparator<Map.Entry<String, Float>>() {
            @Override
            public int compare(Map.Entry<String, Float> e1, Map.Entry<String, Float> e2) {
                Float v1 = e1.getValue();
                Float v2 = e2.getValue();
                return v2.compareTo(v1);
            }
        };
        List<Map.Entry<String, Float>> listOfEntries = new ArrayList<Map.Entry<String, Float>>(entries);
        Collections.sort(listOfEntries, valueComparator);
        LinkedHashMap<String, Float> sortedByValue = new LinkedHashMap<String, Float>(listOfEntries.size());
        for (Map.Entry<String, Float> entry : listOfEntries) {
            sortedByValue.put(entry.getKey(), entry.getValue());
        }
        return sortedByValue;
    }

    private static boolean hasDuplicateWords(List<String> inputWords) {
        Set inputSet = new HashSet(inputWords);
        if (inputSet.size() < inputWords.size()) return true;
        return false;
    }

    private static boolean isUnderSized(List<String> inputWords) {
        if (inputWords.size() < 20) return true;
        return false;
    }

    private static boolean hasInvalidRange(List<String> inputWords) {
        for (String word : inputWords) {
            if (word.length() < 6) return true;
        }
        return false;
    }

    private static boolean isUnbalanced(List<String> inputWords) {
        final int len = inputWords.size();
        float bigWordCount = 0;
        for (String word : inputWords) {
            if (word.length() >= 9) bigWordCount++;
        }
        if (bigWordCount / len >= 0.2) return false;
        return true;
    }
}

class InvalidCountException extends Exception {
    public InvalidCountException() {
        super("Dictionary has duplicate words!");
    }
}

class UnderSizeException extends Exception {
    public UnderSizeException() {
        super("Dictionary has less than 20 words!");
    }
}

class InvalidRangeException extends Exception {
    public InvalidRangeException() {
        super("Dictionary has words with less than 6 letters!");
    }
}

class UnbalancedException extends Exception {
    public UnbalancedException() {
        super("Dictionary hasn't enough 'big' words!");
    }
}