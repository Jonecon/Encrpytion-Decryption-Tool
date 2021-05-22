import java.util.ArrayList;

public class Playfair{
	
	public static void main(String[] args){
		
		if(args.length != 2){
			System.err.println("Expected parameters: <keyword, message>");
			return;
		}
		
		String keyword = args[0];
		String message = args[1];
		
		PlayfairTable table = new PlayfairTable(keyword);
		table.print();
		
	}
	
	public static String encrypt(String message, String key){
		return "";
	}
	
	public static String decrypt(String cypher, String key){
		return "";
	}
	
	

}

class PlayfairTable{
	
	private static final int MAX_SIZE = 25;
	private static final String MISSING_LETTER = "J";
	
	private int currentSize = 0;
	private String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; //missing J because need 25
	private ArrayList<Character> table = new ArrayList<Character>();
	
	public PlayfairTable(String keyword){
		addCharacters(keyword);
		addCharacters(alphabet);
	}
	
	
	private void addCharacters(String keyword){		
		keyword = keyword.toUpperCase().replaceAll("[^A-Z]","");		
		keyword = keyword.replace(MISSING_LETTER, "");
		
		for(int i =0; i < keyword.length(); i++){
			if(currentSize == MAX_SIZE)break;			
			
			if(table.contains(keyword.charAt(i)))continue;
			
			table.add(keyword.charAt(i));
			currentSize++;
		}
	}
	
	public void print(){
		
		System.err.println(table);
	}
	
	
}



























