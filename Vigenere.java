
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
                double _chisquare = getChisquare(Caesar.decrypt(s, letter));
                if (_chisquare < chisquare) {
                    chisquare = _chisquare;
                    shift = (char)(letter + 65);
                }
            }
            key += shift;
        }
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
    private static double getChisquare(String input) {
        int inputLength = input.length();
        Hashtable<Character, Double> pEnglish = new Hashtable<Character, Double>();
        // frequencies taken from https://www.dcode.fr/frequency-analysis
        pEnglish.put('a', 0.082 * inputLength);
        pEnglish.put('b', 0.015 * inputLength);
        pEnglish.put('c', 0.028 * inputLength);
        pEnglish.put('d', 0.043 * inputLength);
        pEnglish.put('e', 0.127 * inputLength);
        pEnglish.put('f', 0.022 * inputLength);
        pEnglish.put('g', 0.020 * inputLength);
        pEnglish.put('h', 0.061 * inputLength);
        pEnglish.put('i', 0.070 * inputLength);
        pEnglish.put('j', 0.002 * inputLength);
        pEnglish.put('k', 0.008 * inputLength);
        pEnglish.put('l', 0.040 * inputLength);
        pEnglish.put('m', 0.024 * inputLength);
        pEnglish.put('n', 0.067 * inputLength);
        pEnglish.put('o', 0.075 * inputLength);
        pEnglish.put('p', 0.019 * inputLength);
        pEnglish.put('q', 0.001 * inputLength);
        pEnglish.put('r', 0.060 * inputLength);
        pEnglish.put('s', 0.063 * inputLength);
        pEnglish.put('t', 0.091 * inputLength);
        pEnglish.put('u', 0.028 * inputLength);
        pEnglish.put('v', 0.010 * inputLength);
        pEnglish.put('w', 0.024 * inputLength);
        pEnglish.put('x', 0.002 * inputLength);
        pEnglish.put('y', 0.020 * inputLength);
        pEnglish.put('z', 0.001 * inputLength);

        Hashtable<Character, Integer> letterFreq = Tools.letterFrequency(input);
        double output = 0;
        for (Map.Entry<Character, Double> expected : pEnglish.entrySet()) {
            output += (Math.pow((  letterFreq.get(expected.getKey()) == null ?  0 : letterFreq.get(expected.getKey()) - expected.getValue()  ), 2)) / expected.getValue();
        }
        return output;
    }
}