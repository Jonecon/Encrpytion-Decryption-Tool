import java.util.ArrayList;
import java.lang.StringBuilder;
import java.lang.Math;
import java.util.regex.Pattern;
import java.io.*;
import java.util.Random;
import java.util.HashMap;


public class Playfair{
	
	private static final String PLACEHOLDER = "X";
	private static final String MISSING_LETTER = "J";
	private static final String REPLACEMENT_LETTER = "I";
	
	private static Random rand = new Random();
	
	private static QuadgramStats loadTrainingProbabilities(String training) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(training));
		QuadgramStats quadStats = new QuadgramStats();
		int count = 0;
		String line = "";
		while((line = reader.readLine()) != null){
			String[] data = line.split(",");
			
			if(data.length == 2){
				quadStats.counts.put(data[0], Integer.parseInt(data[1]));//count
				count++;
			}
		}
		quadStats.total = count;
		return quadStats;
	}
	
	public static String encrypt(String message, String key){
		return convertText(message, key, true);
	}
	
	public static String encrypt(String message){
		String key = randomKey();
		System.err.println("Key: " + key);
		return convertText(message, key, true);
	}
	
	public static String decrypt(String cypher, String key){
		return convertText(cypher, key, false);
	}

	public static String decrypt(String cypher)
	{
		//doesnt work unless initial key is very close
		try{
			QuadgramStats stats = loadTrainingProbabilities("4grams.txt");
			String bestKey = "";
			float bestScore = -4000f;
			
			for(int t = 0; t < 100; t++){
			
				//String pKey = randomKey();
				String pKey = "ABCDEFGHIKLMNOPQRSTUVWXYZ";
				String pText = decrypt(cypher, pKey);
				float pScore = getFitnessScore(pText, stats);
				int lastChanged = 0;
				for(int i = 0; i < 5000; i++){
					String cKey = shuffleKey(pKey);
					String cText = decrypt(cypher, cKey);
					float cScore = getFitnessScore(cText, stats);
					
					if(cScore >= pScore){
						pKey = cKey;
						pText = cText;
						pScore = cScore;
						lastChanged = i;
					}
					else if(i - lastChanged > 100)break;
				}
				//System.err.println("Round " + t + "\tBest Key: " + pKey + "\tScore: " + pScore);
				
				if(pScore > bestScore){
					bestKey = pKey;
					bestScore = pScore;	
				}
				
			}		
			
			//System.err.println("Best Key: " + bestKey + "\tScore: " + bestScore);
			return decrypt(cypher, bestKey);
		}
		catch(Exception ex){
			System.err.println(ex.getMessage());
		}		
		return "";
	}
	
	private static String shuffleKey(String key){
		StringBuilder nKey = new StringBuilder(key);
		
		int c1 = rand.nextInt(25);
		int c2 = rand.nextInt(25);
		char c = nKey.charAt(c1);
		nKey.setCharAt(c1, nKey.charAt(c2));
		nKey.setCharAt(c2, c);		
		
		return nKey.toString();
	}
	
	private static float getFitnessScore(String text, QuadgramStats stats){
		float score = 0f;
		float floor = 0.000000001f;
		for(int i = 0; i < text.length() - 4; i++){
			String quad = text.substring(i, i+4);
			if(stats.counts.containsKey(quad)){
				double prob = (double)stats.counts.get(quad) / stats.total;
				score += Math.log10(prob);
			}
			else score += (float)Math.log10(floor);
		}
		return score;
	}
	
	public static String randomKey(){	
		StringBuilder sb = new StringBuilder("ABCDEFGHIKLMNOPQRSTUVWXYZ");
		String rKey = "";
		for(int i = 25; i > 0; i--){
			int rIndex = rand.nextInt(i);
			rKey+=sb.charAt(rIndex);
			sb.deleteCharAt(rIndex);
		}
		return rKey;
	}
	
	
	public static boolean isLikelyCypher(String cypher){		
		//always even
		if(cypher.length() % 2 != 0) return false;
		//there are never double letters in encryption
		for(int i = 0; i < cypher.length() -1; i+=2)
			if(cypher.charAt(i) == cypher.charAt(i+1)) return false;
		//this playfair always uppercase
		if(!Pattern.matches("[A-Z]+", cypher)) return false;
		
		if(cypher.contains("J"))return false;
		
		return true;
	}	
	
	
	private static String convertText(String message, String key, boolean encrypt){
		PlayfairTable table = new PlayfairTable(prepareString(key));
		message = prepareString(message);
		StringBuilder convertedText = new StringBuilder();
		
		for(int i = 0; i < message.length(); i+=2){			
			String pair = i+1 < message.length() ? message.substring(i, i+2) : message.charAt(i) + PLACEHOLDER;
			String convertedPair = table.getConvertedPair(pair, encrypt);
			if(convertedPair.charAt(0) == convertedPair.charAt(1)) convertedPair = convertedPair.charAt(0) + PLACEHOLDER;
			convertedText.append(convertedPair);
		}
		return convertedText.toString();
	}
	
	private static String prepareString(String orig){
		String prepared = orig.toUpperCase().replaceAll("[^A-Z]","");
		return prepared.replaceAll(MISSING_LETTER, REPLACEMENT_LETTER);
	}
}

class QuadgramStats{
	public HashMap<String, Integer> counts = new HashMap<String, Integer>();
	public int total = 0;
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
