import java.util.ArrayList;
import java.lang.StringBuilder;
import java.lang.Math;


public class Playfair{
	
	private static final String PLACEHOLDER = "X";
	private static final String MISSING_LETTER = "J";
	private static final String REPLACEMENT_LETTER = "I";
	
	//will remove main method soon
	public static void main(String[] args){
		
		if(args.length != 3){
			System.err.println("Invalid input: Expected parameters: <-(e/d), keyword, message>");
			return;
		}
		if(args[0].compareTo("-d") != 0 && args[0].compareTo("-e") != 0){
			System.err.println("Invalid input: -d for decrypted. -e for encrypted");
			return;
		}
		
		boolean encrypt = args[0].compareTo("-d") != 0;
		String keyword = args[1];
		String message = args[2];
		
		if(encrypt){
			String cypherText = encrypt(message, keyword);
			System.err.println("Cypher text: "+ cypherText);	
		}
		else{
			String text = decrypt(message, keyword);
			System.err.println("Message: "+ text);	
		}
	}
	
	public static String encrypt(String message, String key){
		return convertText(message, key, true);
	}	
	
	public static String decrypt(String cypher, String key){
		return convertText(cypher, key, false);
	}
	
	private static String convertText(String message, String key, boolean encrypt){
		PlayfairTable table = new PlayfairTable(prepareString(key));
		message = prepareString(message);
		StringBuilder convertedText = new StringBuilder();
		
		for(int i = 0; i < message.length(); i+=2){			
			String pair = i+1 < message.length() ? message.substring(i, i+2) : message.charAt(i) + PLACEHOLDER;
			String convertedPair = table.getConvertedPair(pair, encrypt);
			convertedText.append(convertedPair);
		}
		return convertedText.toString();
	}
	
	private static String prepareString(String orig){
		String prepared = orig.toUpperCase().replaceAll("[^A-Z]","");
		return prepared.replaceAll(MISSING_LETTER, REPLACEMENT_LETTER);
	}
}

class PlayfairTable{
	
	private static final int MAX_SIZE = 25;	
	private static final String ALPHABET = "ABCDEFGHIKLMNOPQRSTUVWXYZ";
		
	private int currentSize = 0;	
	private ArrayList<Character> table = new ArrayList<Character>();
	
	public PlayfairTable(String keyword){
		addCharacters(keyword);
		addCharacters(ALPHABET);
	}
	
	
	private void addCharacters(String keyword){	
		//each character only appears once
		for(int i =0; i < keyword.length(); i++){
			if(currentSize == MAX_SIZE)break;			
			
			if(table.contains(keyword.charAt(i)))continue;
			
			table.add(keyword.charAt(i));
			currentSize++;
		}
	}
	
	public String getConvertedPair(String pair, boolean encrypt){
		if(pair.length() != 2)return null;
		
		int message1 = table.indexOf(pair.charAt(0));
		int message2 = table.indexOf(pair.charAt(1));
		
		//get character positions
		int mColumn1 = getColumnNumber(message1), mColumn2 = getColumnNumber(message2);
		int mRow1 = getRowNumber(message1), mRow2 = getRowNumber(message2);
		
		//the cypher character positions
		int cColumn1 = mColumn1, cColumn2 = mColumn2, cRow1 = mRow1, cRow2 = mRow2;		
		
		if(mColumn1 == mColumn2){	
			//move letters up/down one
			cRow1 = encrypt ? (mRow1 + 1) % 5 : Math.floorMod(mRow1 - 1, 5);
			cRow2 = encrypt ? (mRow2 + 1) % 5 : Math.floorMod(mRow2 - 1, 5);
		}
		else if(mRow1 == mRow2){
			//move letters to the right/left by one
			cColumn1 = encrypt ? (mColumn1 + 1) % 5 : Math.floorMod(mColumn1 - 1, 5);
			cColumn2 = encrypt ? (mColumn2 + 1) % 5: Math.floorMod(mColumn2 - 1, 5);
		}
		else{
			//letter at opposite corner of rectangle on same row
			cColumn1 = mColumn2;
			cColumn2 = mColumn1;
		}
		//get new characters
		char cypher1 = table.get((cRow1 * 5) + cColumn1);
		char cypher2 = table.get((cRow2 * 5) + cColumn2);
		return "" + cypher1 + cypher2;				
	}	
	
	private int getColumnNumber(int index){
		return index % 5;
	}
	
	private int getRowNumber(int index){
		return index / 5;
	}
	
	public void print(){		
		System.err.println(table);
	}
}
