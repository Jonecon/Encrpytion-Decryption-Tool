import java.io.*;
import java.util.*;

public class Tools {
	
	public static void main(String[] args) {
		try {

			// TESTING METHODS
			// System.out.println(indexOfCoincidence(args[0]));
		} catch(Exception e) {

			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	// REMOVES SPACES FROM A STRING
	public static String removeSpaces(String message) {
		return message.replaceAll("\\s", "");
	}

	// REMOVES ALL NON LETTER CHARACTERS AND REMOVES CAPITALS
	public static String simplifyMessage(String message) {

		// GET ALL WORD CHAR [a-zA-Z0-9_]
		String[] stringArray = message.split("\\W+");
		String result = "";

		for(String s: stringArray) {
			result += s;
		}

		return result.toLowerCase();
	}

	// READS THE FILE
	// FILES CAN BE PIPED INTO A JAVA PROGRAM LIKE SO:
	// type filename.txt | java program
	public static String readStdIn() {
		try {

			// READS FILE FROM STANDARD INPUT input(stdin)
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String input = "";
			String line;

			// WHILE NOT END OF THE LINE
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

	public static double indexOfCoincidence(String message) {

		message = simplifyMessage(message);
		Hashtable<Character, Integer> dictionary = new Hashtable<Character, Integer>();
		
		// COUNT OCCURANCES OF CHARACTErS
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

	public static Hashtable<Character, Integer> letterFrequency(String message) {

		message = simplifyMessage(message);
		Hashtable<Character, Integer> dictionary = new Hashtable<Character, Integer>();

		// COUNT OCCURANCES OF CHARACTErS
		for (char c: message.toCharArray()) {
			dictionary.put(c, (dictionary.get(c) != null ? dictionary.get(c) : 0) + 1);
		}

		return dictionary;
	}
}