
import java.io.*;
import java.util.*;
import java.util.stream.*;

public class Caesar {

    public static void main(String[] args) {
        // using main for testing, probably wont need in future
        try {
            int shift = Integer.parseInt(args[0]);
            String input = Tools.readStdIn();
            String cipherText = encrypt(input, shift);
            System.out.println(cipherText);
//            System.out.println(decryptWithoutKey(cipherText));



        } catch(Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

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

    public static String decrypt(String input, int key) {
        return caesar(input, -key);
    }
    public static String encrypt(String input, int key) {
        return caesar(input, key);
    }
}