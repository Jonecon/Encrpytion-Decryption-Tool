import java.io.*;

public class Vigenere {

    public static void main(String[] args) {
        // using main for testing, probably wont need in future
        try {
            String key = args[0];
            System.out.println(key);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String message = reader.readLine();
            System.out.println(message);
            System.out.println(Tools.simplifyMessage(message));
            String cipherText = encrypt(message, key);
            System.out.println(cipherText);
            System.out.println(decrypt(cipherText, key));
            reader.close();

        } catch(Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static char shiftLetter(char c, int offset) {
        int ascii = (int)c - 97;
        ascii = (ascii + offset) % 26;
        ascii += 97;
        return (char)ascii;
    }

    public static String removeSpaces(String message) {
        return message.replaceAll("\\s", "");
    }


    private static String vigenere(String input, String key, boolean decrypt) {
        String message = "";
        char[] keyArray = key.toUpperCase().toCharArray();
        int i = 0;
        for (char c: input.toCharArray()) {
            if (Character.isLetter(c)) {
                boolean capital = false;
                if (Character.isUpperCase(c)) {
                    c = Character.toLowerCase(c);
                    capital = true;
                }
                int offset = (int)keyArray[i] - 65;
                c = shiftLetter(c, decrypt ? -offset : offset);
                if (capital) {
                    c = Character.toUpperCase(c);
                }
                i = (i + 1) % keyArray.length;
            }
            message += c;
        }
        return message;
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