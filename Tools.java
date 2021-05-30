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

	public static byte[] readStdInBytes(){
		try{
			ArrayList<Byte> message = new ArrayList<Byte>();
        	BufferedInputStream byteStream = new BufferedInputStream(System.in);
        	int b;
        	while ((b = byteStream.read()) != -1){
        		message.add((byte)b);
        	}
            byteStream.close();

            //Convert list into byte array for encrypt/decrypt methods.
           	byte[] messageBytes = new byte[message.size()];
           	for (int i = 0; i < message.size(); i++){
           		messageBytes[i] = message.get(i).byteValue();
           	}

           	return messageBytes;
		}
		catch(Exception ex){
			System.err.println("Cannot read the bytes from std in: " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}
	}

	public static void outputBytes(byte[] output) {
		try{
			BufferedOutputStream outputStream = new BufferedOutputStream(System.out);
			outputStream.write(output);
           	outputStream.close();
		}
		catch(Exception ex){
			System.err.println("Cannot output the bytes to std out: " + ex.getMessage());
			ex.printStackTrace();
		}
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


	public static double chiSquare(String input) {
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