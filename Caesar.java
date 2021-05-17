
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
        String output = cipherText;
        double chiSquare = 9999999;
        for (int letter = 0; letter < 26; letter++) {
            String decryption = Caesar.decrypt(cipherText, letter);
            double _chiSquare = Tools.chiSquare(decryption);
            if (_chiSquare < chiSquare) {
                chiSquare = _chiSquare;
                output = decryption;
            }
        }
        return output;
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
    public static String decrypt(String input, int key) {
        return caesar(input, -key);
    }
    public static String encrypt(String input, int key) {
        return caesar(input, key);
    }
}