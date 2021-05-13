
import java.io.*;
import java.util.*;
import java.util.stream.*;

public class Caesar {

    public static String caesar(String input, int shift) {
        String output = "";
        for (char c: input.toCharArray()) {
            if (Character.isLetter(c)) {
                c = Tools.shiftLetter(c, shift);
            }
            output += c;
        }
        return output;
    }

    public static String decryptWithoutKey(String cipherText) {
        Hashtable<Character, Integer> dictionary = Tools.letterFrequency(cipherText);
        Map.Entry<Character, Integer> commonLetter = null;
        for (Map.Entry<Character, Integer> entry : dictionary.entrySet())
        {
            if (commonLetter == null || entry.getValue().intValue() > commonLetter.getValue().intValue()) {
                commonLetter = entry;
            }
        }
        int shift = (int)commonLetter.getKey() - 'e';
        return caesar(cipherText, -shift);
    }

    public static String decrypt(String input, String key) {
        int intKey;
        try {
            intKey = Integer.parseInt(key);
        } catch(Exception e) {
            if (key.length() > 1 || !Character.isLetter(key.toCharArray()[0])) {
                System.err.println("Caesar cipher requires an integer or a letter as a key.");
                return null;
            }
            intKey = (int)Character.toUpperCase(key.toCharArray()[0]) - 65;
        }
        return caesar(input, -intKey);
    }
    public static String encrypt(String input, String key) {
        int intKey;
        try {
            intKey = Integer.parseInt(key);
        } catch(Exception e) {
            if (key.length() > 1 || !Character.isLetter(key.toCharArray()[0])) {
                System.err.println("Caesar cipher requires an integer or a letter as a key.");
                return null;
            }
            intKey = (int)Character.toUpperCase(key.toCharArray()[0]) - 65;
        }
        return caesar(input, intKey);
    }
}