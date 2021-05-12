
import java.io.*;
import java.util.*;

public class Tools {

    public static void main(String[] args) {
        try {
            // testing methods
            // System.out.println(indexOfCoincidence(args[0]));
        } catch(Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    // removes spaces from a string
    public static String removeSpaces(String message) {
        return message.replaceAll("\\s", "");
    }

    // removes all non letter characters and removes capitals
    public static String simplifyMessage(String message) {
        String[] stringArray = message.split("\\W+");
        String result = "";
        for(String s: stringArray) {
            result += s;
        }
        return result.toLowerCase();
    }


    public static double indexOfCoincidence(String message) {
        message = simplifyMessage(message);
        Hashtable<Character, Integer> dictionary = new Hashtable<Character, Integer>();
        // count occurences of characters
        for (char c: message.toCharArray()) {
            dictionary.put(c, (dictionary.get(c) != null ? dictionary.get(c) : 0) + 1);
        }
        double output = 0;
        long n = message.length();
        long devisor = n * (n - 1);
        for (Integer count: dictionary.values()) {
            if (((double)count * (count - 1) / devisor) < 0) {
                System.err.println(count + " " + devisor);
            }
            output += (double)count * (count - 1) / devisor;
        }
        return output;
    }

    // reads a file from the standard input(stdin)
    // returns the file as a string
    // files can be piped into a java program like so:
    // type filename.txt | java program
    public static String readStdIn() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String input = "";
            String line;
            while ((line = reader.readLine()) != null) {
                input += line + '\n';
            }
            reader.close();
            return input;
        } catch(Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static Hashtable<Character, Integer> letterFrequency(String message) {
        message = simplifyMessage(message);
        Hashtable<Character, Integer> dictionary = new Hashtable<Character, Integer>();
        // count occurences of characters
        for (char c: message.toCharArray()) {
            dictionary.put(c, (dictionary.get(c) != null ? dictionary.get(c) : 0) + 1);
        }
        return dictionary;
    }

    public static char shiftLetter(char c, int offset) {
        boolean capital = false;
        if (Character.isUpperCase(c)) {
            c = Character.toLowerCase(c);
            capital = true;
        }
        int ascii = (int)c - 97;
        ascii = (ascii + offset + 26) % 26;
        ascii += 97;
        c = (char)ascii;
        if (capital) {
            c = Character.toUpperCase(c);
        }
        return c;
    }

}