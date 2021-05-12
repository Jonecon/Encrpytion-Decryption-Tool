
import java.io.*;

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
    // public static String decode(String cipherText) {
    //     return cipherText;
    // }
}