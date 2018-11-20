/*
    Author: Isabel Bryant
    Date: 11/19/2018
    BookBub Coding Challenge
 */

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class GenreDetector {

    public static void main(String[] args) {

        Map<String, Map<String, Integer>> genreMap = new HashMap<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader("sample_genre_keyword_value.csv"));
            String line;
            br.readLine();                                                                                              // consume first line (header) of csv file

            while (null != (line = br.readLine())) {                                                                    // for each line in csv

                String[] keywordInfo = line.split(",");
                for (int i = 0; i < keywordInfo.length; i++)
                    keywordInfo[i] = keywordInfo[i].trim();

                String genre = keywordInfo[0];                                                                              // resolve genre
                String keyword = keywordInfo[1];                                                                            // resolve keyword
                int score = Integer.parseInt(keywordInfo[2]);                                                               // resolve

                if (genreMap.get(genre) == null)
                    genreMap.put(genre, new HashMap<>());                                                                   // if genre not declared in map, add it

                genreMap.get(genre).put(keyword, score);                                                                    // add the keyword to the score
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("sample_book_json.txt"));
            JSONArray bookList = (JSONArray) obj;

            Iterator<JSONObject> iterator = bookList.iterator();

            while (iterator.hasNext()) { // for each book

                JSONObject next = iterator.next();

                String description = next.get("description").toString();
                String[] words = description.split("\\W");
                String title = next.get("title").toString();

                Map<String, Integer> genreScores = new HashMap<>();
                for (String genre : genreMap.keySet())
                    genreScores.put(genre, 0);

                for (String word : words) {                                                                             // for each word in description
                    for (String genre : genreMap.keySet()) {                                                                // for each genre
                        if (!genreMap.get(genre).containsKey(word))                                                             // genre does NOT contain keyword, continue
                            continue;
                        genreScores.put(genre, genreScores.get(genre) + genreMap.get(genre).get(word));
                    }
                }

                System.out.println(title + ": " + genreScores);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    static class KeywordScore {
        private String word;
        private int score;

        KeywordScore(String word, int score) {
            this.word = word;
            this.score = score;
        }

        public String getWord() {
            return word;
        }

        public int getScore() {
            return score;
        }

        @Override
        public String toString() {
            return "KeywordScore{" +
                    "word='" + word + '\'' +
                    ", score=" + score +
                    '}';
        }
    }
}
