import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class Anagrammer {
    private HashMap<Integer, LinkedList<String>> words;

    public Anagrammer() {
        words = new HashMap<Integer, LinkedList<String>>();
        String wordsFile = "words.txt";
        preprocess(wordsFile);
    }

    // PREPROCESS =============================================================
    public void preprocess(String wordsFile) {
        /*  Obtain the words from the list of words given in the file.
            Store them in a hashmap called words. The key will be the length of
            the words and the value will be a linked list of the words with the
            key as the length. */

        try (BufferedReader br = new BufferedReader(new FileReader(wordsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                int len = line.length();
                LinkedList<String> current;

                if (words.containsKey(len)) current = words.get(len);
                else current = new LinkedList<String>();

                current.add(line);
                words.put(len, current);
            }
        } catch (Exception e) {}
    }


    // CONTAINS ===============================================================
    public boolean contains(String s1, String s2) {
        /*  Determine if the second string can be made from the letters in the
            first string */

        HashMap<Character, Integer> letters = new HashMap<Character, Integer>();

        // see what letters the first string contains
        for (int i = 0; i < s1.length(); i++) {
            char focus = s1.charAt(i);

            if (letters.containsKey(focus)) letters.put(focus, letters.get(focus)+1);
            else letters.put(focus, 1);
        }

        // see if letters in second string are contained in first string
        for (int i = 0; i < s2.length(); i++) {
            char focus = s2.charAt(i);

            if (!letters.containsKey(focus) || letters.get(focus) <= 0) {
                return false;
            } else {
                letters.put(focus, letters.get(focus)-1);
            }
        }

        return true;
    }

    // FIND ANAGRAMS ==========================================================
    public HashMap<Integer, LinkedList<String>> findAnagrams(String letters) {
        /*  Slight misnomer. Find all words that can be formed from the letters
            of the target word. */

        HashMap<Integer, LinkedList<String>> anagrams = new HashMap<Integer, LinkedList<String>>();

        for (int len : words.keySet()) {
            if (len > letters.length()) continue;

            LinkedList<String> possible = words.get(len);

            for (String focus : possible) {
                if (contains(letters, focus)) {
                    LinkedList<String> current;

                    if (anagrams.containsKey(len)) current = anagrams.get(len);
                    else current = new LinkedList<String>();

                    current.add(focus);
                    anagrams.put(len, current);
                }
            }
        }

        return anagrams;
    }

    public static void main(String[] args) {
        Anagrammer anagrammer = new Anagrammer();
        Scanner scanner = new Scanner(System.in);

        String in;
        do {
            System.out.println("ENTER LETTERS TO BE ANAGRAMMED");
            // take in letters as lower case with no spaces
            in = scanner.nextLine().toLowerCase().replaceAll("\\s+","");
            HashMap<Integer, LinkedList<String>> anagrams = anagrammer.findAnagrams(in);

            StringBuilder display = new StringBuilder();

            // Display the possible formed words to the player
            for (int key : anagrams.keySet()) {
                display.append("\n" + key + " letters\n");

                int column = 0;
                LinkedList<String> possible = anagrams.get(key);
                for (String focus : possible) {
                    display.append(focus + "\t");

                    // put 6 words in a row for more compact word board
                    column++;
                    if (column == 6) {
                        display.append("\n");
                        column = 0;
                    }
                }

                if (column != 0) display.append("\n");
            }

            System.out.println(display.toString());
        } while (!in.equals(""));
    }
}
