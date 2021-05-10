
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
        int n = message.length();
        int devisor = n * (n - 1);
        for (Integer count: dictionary.values()) {
            output += (double)count * (count - 1) / devisor;
        }
        return output;
    }

}