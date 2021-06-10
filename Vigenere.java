
import java.io.*;
import java.util.*;

public class Vigenere {

    public static void main(String[] args) {
        // using main for testing, probably wont need in future
        try {
            String key = args[0];
            String input = Tools.readStdIn();
            String cipherText = encrypt(input, key);
            System.out.println(cipherText);

        } catch(Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static String removeSpaces(String message) {
        return message.replaceAll("\\s", "");
    }


    private static String vigenere(String input, String key, boolean decrypt) {
        String output = "";
        char[] keyArray = key.toUpperCase().toCharArray();
        int i = 0;
        for (char c: input.toCharArray()) {
            if (Character.isLetter(c)) {
                int offset = (int)keyArray[i] - 65;
                c = Tools.shiftLetter(c, decrypt ? -offset : offset);
                i = (i + 1) % keyArray.length;
            }
            output += c;
        }
        return output;
    }

    public static String encrypt(String input, String key) {
        return vigenere(input, key, false);
    }
    public static String decrypt(String input, String key) {
        return vigenere(input, key, true);
    }



    // method to (try to) decode without a key
    public static String findKey(String cipherText) {
        String SimplifiedCipherText = Tools.simplifyMessage(cipherText);
        int n = 1;
        double ioc = 0;
        for (int keyLength = 1; keyLength < 10; keyLength++) {
            double _ioc = Tools.indexOfCoincidence(getEveryNthLetter(SimplifiedCipherText, keyLength, 0));
            if (_ioc > ioc) {
                ioc = _ioc;
                n = keyLength;
            }
        }

        String key = "";
        for (int i = 0; i < n; i++) {
            double chisquare = 999999;
            char shift = 'a';
            String s =  getEveryNthLetter(SimplifiedCipherText, n, i);
            for (int letter = 0; letter < 26; letter++) {
                double _chisquare = Tools.chiSquare(Caesar.decrypt(s, letter));
                if (_chisquare < chisquare) {
                    chisquare = _chisquare;
                    shift = (char)(letter + 65);
                }
            }
            key += shift;
        }
        System.out.println("KEY: " + key);
        return decrypt(cipherText, key);
    }

    private static String getEveryNthLetter(String input, int n, int startIndex) {
        String output = "";
        int i = startIndex;
        char[] charArray = input.toCharArray();
        while (i < charArray.length) {
            output += charArray[i];
            i += n;
        }
        return output;
    }

    public static String randomKey(int minLength, int maxLength) {
        Random r = new Random();
        int length = r.nextInt(maxLength - minLength) + minLength;
        String key = "";
        for (int i = 0; i < length; i++) {
            int letter = r.nextInt(25);
            key += (char)(letter + 65);
        }
        return key;
    }
}