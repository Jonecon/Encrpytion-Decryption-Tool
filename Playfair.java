import java.util.ArrayList;
import java.lang.StringBuilder;

public class Playfair{
	
	private static final String PLACEHOLDER = "X";
	private static final String MISSING_LETTER = "J";
	private static final String REPLACEMENT_LETTER = "I";
	
	public static void main(String[] args){
		
		if(args.length != 2){
			System.err.println("Expected parameters: <keyword, message>");
			return;
		}
		
		String keyword = args[0];
		String message = args[1];
		
		String cypherText = encrypt(message, keyword);
		System.err.println("Cypher text: "+ cypherText);	
		
	}
	
	public static String encrypt(String message, String key){
		PlayfairTable table = new PlayfairTable(prepareString(key));
		table.print();		
		
		message = prepareString(message);
		StringBuilder cypherText = new StringBuilder();
		
		for(int i = 0; i < message.length(); i+=2){
			
			String pair = i+1 < message.length() ? message.substring(i, i+2) : message.charAt(i) + PLACEHOLDER;
			System.err.println("Message pair: " + pair);
			String cypherPair = table.getCypherPair(pair);
			System.err.println("Cypher pair: " + cypherPair);
			cypherText.append(cypherPair);
		}
		return cypherText.toString();
	}
	
	private static String prepareString(String orig){
		String prepared = orig.toUpperCase().replaceAll("[^A-Z]","");
		return prepared.replaceAll(MISSING_LETTER, REPLACEMENT_LETTER);
	}
	
	public static String decrypt(String cypher, String key){
		return "";
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
		for(int i =0; i < keyword.length(); i++){
			if(currentSize == MAX_SIZE)break;			
			
			if(table.contains(keyword.charAt(i)))continue;
			
			table.add(keyword.charAt(i));
			currentSize++;
		}
	}
	
	public String getCypherPair(String messagePair){
		if(messagePair.length() != 2)return null;
		
		//havent added checks for missing letter yet
		int message1 = table.indexOf(messagePair.charAt(0));
		int message2 = table.indexOf(messagePair.charAt(1));
		
		//get message character positions
		int mColumn1 = getColumnNumber(message1), mColumn2 = getColumnNumber(message2);
		int mRow1 = getRowNumber(message1), mRow2 = getRowNumber(message2);
		
		//the cypher character positions
		int cColumn1 = mColumn1, cColumn2 = mColumn2, cRow1 = mRow1, cRow2 = mRow2;		
		
		if(mColumn1 == mColumn2){	
			//move letters down one
			cRow1 = (mRow1 + 1) % 5;
			cRow2 = (mRow2 + 1) % 5;
		}
		else if(mRow1 == mRow2){
			//move letters to the right by one
			cColumn1 = (mColumn1 + 1) % 5;
			cColumn2 = (cColumn2 + 1) % 5;
		}
		else{
			//letter at opposite corner of rectangle on same row
			cColumn1 = mColumn2;
			cColumn2 = mColumn1;
		}
		//get new cypher characters
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



























