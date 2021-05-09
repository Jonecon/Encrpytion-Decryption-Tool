import java.io.*;

public class Vigenere {

    public static void main(String[] args) {
        try {
            String key = args[0];
            System.out.println(key);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String message = reader.readLine();
            System.out.println(message);
            // message = removeSpaces(message);
            // System.out.println(message);
            message = capitalise(message);
            System.out.println(message);
            System.out.println();
            String cipherText = encode(message, key);
            System.out.println(cipherText);
            reader.close();

        } catch(Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static String encode(String message, String key) {
        String cipherText = "";
        char[] keyArray = key.toCharArray();
        int i = 0;
        for (char c: message.toCharArray()) {
            if (Character.isLetter(c)) {
                int offset = (int)keyArray[i] - 65;
                cipherText += shiftLetter(c, offset);
                i = (i + 1) % keyArray.length;
            } else {
               cipherText += c;
            }
        }
        return cipherText;
    }

    private static char shiftLetter(char c, int offset) {
        int ascii = (int)c - 65;
        ascii = (ascii + offset) % 26;
        ascii += 65;
        return (char)ascii;
    }

    private static String removeSpaces(String message) {
        return message.replaceAll("\\s", "");
    }

    private static String capitalise(String message) {
        return message.toUpperCase();
    }

    public static String decode(String cipherText, String key) {
        return cipherText;
    }
    // method to (try to) decode without a key
    public static String decode(String cipherText) {
        return cipherText;
    }
}