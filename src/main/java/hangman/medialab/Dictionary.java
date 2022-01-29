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

    public Dictionary(String bookId) throws IOException, InvalidCountException, UnderSizeException, InvalidRangeException, UnbalancedException {
        this.words = new ArrayList<>();
        try {
            var fileName = bookId;
            File dict = new File(fileName);
            Scanner dictReader = new Scanner(dict);
            while (dictReader.hasNextLine()) {
                String data = dictReader.nextLine();
                System.out.println(data);
                this.words.add(data);
            }
        } catch (FileNotFoundException fileException) {
            var url = new URL(String.format("https://openlibrary.org/works/%s.json", bookId));
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String stringJson;
            stringJson = bReader.readLine();

            JSONParser parser = new JSONParser();
            JSONObject response;
            try {
                response = (JSONObject) parser.parse(stringJson);
            } catch (ParseException parseException) {
                throw new IOException("Invalid JSON format", parseException);
            }
            response = (JSONObject) response.get("description");

            words = Arrays.stream(((String) response.get("value"))
                            .replaceAll("[^a-zA-Z \n]", "")
                            .trim()
                            .toUpperCase()
                            .split("\\s+"))
                    .filter(w -> w.length() >= 6)
                    .distinct()
                    .collect(Collectors.toList());

            System.out.println(words);


            // InvalidCountException and InvalidRangeException exceptions will never be thrown
            // because of the preprocessing of the response .filter() and .distinct()
            if (hasDuplicateWords(words)) throw new InvalidCountException();
            if (isUnderSized(words)) throw new UnderSizeException();
            if (hasInvalidRange(words)) throw new InvalidRangeException();
            if (isUnbalanced(words)) throw new UnbalancedException();
        }
    }


    public static boolean hasDuplicateWords(List<String> inputWords) {
        Set inputSet = new HashSet(inputWords);
        if (inputSet.size() < inputWords.size()) return true;
        return false;
    }

    public static boolean isUnderSized(List<String> inputWords) {
        if (inputWords.size() < 20) return true;
        return false;
    }

    public static boolean hasInvalidRange(List<String> inputWords) {
        for (String word : inputWords) {
            if (word.length() < 6) return true;
        }
        return false;
    }

    public static boolean isUnbalanced(List<String> inputWords) {
        final int len = inputWords.size();
        int bigWordCount = 0;
        for (String word : inputWords) {
            if (word.length() >= 9) bigWordCount++;
        }
        if (bigWordCount / len >= 0.2) return false;
        return true;
    }
}

// TODO: exception error messages
class InvalidCountException extends Exception {
    public InvalidCountException() {
        super("InvalidCountException");
    }
}

class UnderSizeException extends Exception {
    public UnderSizeException() {
        super("UnderSizeException");
    }
}

class InvalidRangeException extends Exception {
    public InvalidRangeException() {
        super("InvalidRangeException");
    }
}

class UnbalancedException extends Exception {
    public UnbalancedException() {
        super("UnbalancedException");
    }
}