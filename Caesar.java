
import java.io.*;

public class Caesar {

    public static void main(String[] args) {
        // using main for testing, probably wont need in future
        try {
            int shift = Integer.parseInt(args[0]);
            System.out.println(shift);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String message = reader.readLine();
            System.out.println(message);
            String cipherText = encrypt(message, shift);
            System.out.println(cipherText);
            System.out.println(decrypt(cipherText, shift));
            reader.close();

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

    public static String decrypt(String input, int key) {
        return caesar(input, -key);
    }
    public static String encrypt(String input, int key) {
        return caesar(input, key);
    }
}