import java.io.*;
import java.util.*;
import java.lang.*;  

public class LocalTransposition {

	/**** DECLARE GLOBAL VARIABLE ****/
	private static int msglen;
	
	// EXAMPLE CALL:
	// type testfiles/message.txt | java LocalTrans cycle > outputfile.txt
	public static void main(String[] args) {
		try {

			// String strCycle = (args.length == 1) ? args[0] : null;
			// String encryptMsg = localTrans(Tools.readStdIn(), strCycle, "encrypt");
			// String decryptMsg = localTrans(encryptMsg, strCycle, "decrypt");

			String decryptMsgWithNoKey = decryptWithNoKey(Tools.readStdIn());

		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	/**** ENCRYPT/DECRYPT (ACCORDING TO THE METHOD PROVIDED) THE MSG USING LOCAL TRANSPOSITION ****/
	public static String transposition(String msg, String intcycle, String method) {

		/**** DECLARE VARIABLES ****/
		// FOR THE ENCRYPTED MSG
		String encyptedMsg = "";
		StringBuilder encrypted = new StringBuilder();

		// FOR THE CYCLE
		msglen = msg.length();
		ArrayList<Integer> cycle = new ArrayList<Integer>();

		// IF THE CYCLE IS FOR ENCRYPTION
		if (method.equals("encrypt")) { cycle = piCycle(intcycle); }
		// IF THE CYCLE IS FOR DECRYPTION  
		else if (method.equals("decrypt")) { cycle = inversePiCycle(intcycle); }

		// FOR BLOCKS OF TEXT
		ArrayList<ArrayList<String>> eachBlock = new ArrayList<ArrayList<String>>();
		ArrayList<String> blockMessages = new ArrayList<String>();
		ArrayList<String> tempBlock = new ArrayList<String>();
		ArrayList<String> copy = new ArrayList<String>();

		// FOR ENCRYPTING/DECRYPTING
		ArrayList<String> singleBlock = new ArrayList<String>();
		// IF CYCLE STARTS WITH 0 BOTH VARIABLE SET TO 0 ELSE -1 AND 1
		int offsetcycle = (cycle.contains(0)) ? 0 : -1;
		int startcycle = (cycle.contains(0)) ? 0 : 1;
		int letterIndex = 0;
		int letterToMove = 0;
		int moveToWhere = 0;

		/**** SEPARATE THE MESSAGE INTO BLOCK SIZE OF THE CYCLE LEN ****/
		// REMOVE THE LAST NEWLINE ON .TXT
		if (msg.substring(msg.length()-1).equals("\n")) {
			msg = msg.substring(0, msg.length()-1);	
		}
		String[] blocks = msg.split("(?<=\\G.{"+ cycle.size() +"})");
		Collections.addAll(blockMessages, blocks);

		/**** MAKE EACH BLOCK INTO AN ARRAYLIST AND PUT INTO AN ARRAYLIST(MAIN ARRAYLIST) ****/
		// FOR EACH BLOCK
		for (String block : blockMessages) {

			// SPLIT THE BLOCK THEN CONVERT INTO ARRAYLIST
			Collections.addAll(tempBlock, block.split(""));
			copy = copyArrayListStr(tempBlock);

			// IF THE BLOCK DOES NOT HAVE THE SIZE OF THE CYCLE
			while(copy.size() != cycle.size()) {
				// ADD PADDING
				copy.add(" ");
			}

			// ADD THIS ARRAYLIST TO THE MAIN ARRAYLIST
			eachBlock.add(copy);
			tempBlock.clear();
		}

		/**** REARRANGING EACH BLOCK ACCORDING TO THE CYCLE THEN ADD TO THE STRINGBUILDER ****/
		// FOR EVERY BLOCK
		for (int i = 0; i < eachBlock.size(); i++) {

			// COPY THE BLOCK TO MODIFY
			singleBlock = copyArrayListStr(eachBlock.get(i));

			// FOR EVERY LETTER IN THE CURRENT BLOCK
			for (int j = 0; j < eachBlock.get(i).size(); j++) {
				
				// GET THE NUMBER IN THE CYCLE (NUMBER TO MOVE)
				letterIndex = startcycle + offsetcycle;
				letterToMove = cycle.get(letterIndex);

				// FIND WHERE TO MOVE THE NUMBER (NEXT NUMBER)
				if ((letterIndex+1) >= cycle.size()) {

					// GET THE FIRST NUMBER IF ITS THE LAST NUMBER AND THEN RESET THE CYCLE
					moveToWhere = cycle.get(0);
					startcycle = (cycle.contains(0)) ? 0 : 1;

				} else {

					moveToWhere = cycle.get(letterIndex+1);
					startcycle++;
				}

				// MODIFY THE COPY ARRAYLIST
				singleBlock.set(moveToWhere+offsetcycle, eachBlock.get(i).get(letterToMove+offsetcycle));
			}

			// ADD EACH LETTER TO THE STRING BUILDER
			for (String letter : singleBlock ) {
				encrypted.append(letter);
			}
		}

		encyptedMsg = encrypted.toString();

		/* DEBUGGING */

		// System.out.println("\n"+ method +"ed text:\n");
		// System.out.println(encyptedMsg);

		// int blockIndex = 0;

		// System.out.println("\nEncrypted Text in Block:\n");
		// ArrayList<String> encryptedMsgDebug = new ArrayList<String>();
		// String[] encryptedDebug = encyptedMsg.split("(?<=\\G.{"+ cycle.size() +"})");
		// Collections.addAll(encryptedMsgDebug, encryptedDebug);
		// for (String block : encryptedMsgDebug ) {
		// 	System.out.println(blockIndex + " " + block);
		// 	blockIndex++;
		// }


		// blockIndex = 0;
		// System.out.println("\nBlockString:\n");
		// for (String block : blockMessages ) {
		// 	System.out.println(blockIndex + " " + block);
		// 	blockIndex++;
		// }

		// blockIndex = 0;
		// System.out.println("\nEachBlock:\n");
		// for (ArrayList<String> block : eachBlock) {
			
		// 	System.out.print(blockIndex + " ");
		// 	for (String letter : block) {
		// 		System.out.print(letter + " ");
		// 	}
		// 	System.out.print("\n");
		// 	blockIndex++;
		// }

		return encyptedMsg;
	}

	public static String decryptWithNoKey(String msg) {

		/**** REMOVE THE LAST NEWLINE ON .TXT ****/
		if (msg.substring(msg.length()-1).equals("\n")) {
			msg = msg.substring(0, msg.length()-1);	
		}

		/**** DECLARE VARIABLES ****/
		String copy = msg;
		int lenofmsg = msg.length();
		ArrayList<Integer> possibleLenValue = getPossiblePiCycleLen(lenofmsg);




		/* DEBUGGING */
		System.out.println("msg to decrypt: " + msg);
		System.out.println("msg length: " + lenofmsg);
		System.out.println("length sqrt: " + (int)Math.sqrt(lenofmsg));
		System.out.println("possible length: " + possibleLenValue);

		return copy;

	} 

	/**** CONVERT THE STRING CYCLE PROVIDED INTO AN ARRAYLIST PiCYCLE ****/
	static ArrayList<Integer> piCycle(String intcycle) {

		/**** DECLARE VARIABLES ****/
		ArrayList<Integer> cycle = new ArrayList<Integer>();

		/**** IF KEY HAS BEEN PROVIDED****/
		if (intcycle != null) {

			/**** ADD EACH NUMBER PROVIDED TO THE ARRAYLIST ****/
			// FOR EVERY STRING PROVIDED
			for (String number : intcycle.split(",")) {

				// ADD TO ARRAYLIST IF THE STRING PROVIDED IS NUMERIC
				if (isNumeric(number)) {
					cycle.add(Integer.parseInt(number));
				} else {
					errorMsg("ErrorCycle");
				}
			}

			/**** THEN CHECK IF THE CYCLE ARRAYLIST IS VALID ****/
			if (checkCycle(cycle) == false) {
				errorMsg("ErrorCycle");
			}	
		}
		/**** IF KEY IS NOT PROVIDED ****/
		else {
			cycle = getRandPiCycle();
		}

		return cycle;
	}

	/**** GET THE INVERESE OF THE PiCYCLE ****/
	static ArrayList<Integer> inversePiCycle(String intcycle) {

		/**** DECLARE VARIABLES ****/
		ArrayList<Integer> picycle = new ArrayList<Integer>();
		ArrayList<Integer> inversecycle = new ArrayList<Integer>();

		/**** IF KEY HAS BEEN PROVIDED****/
		if (intcycle != null) {

			picycle = piCycle(intcycle);
			inversecycle = copyArrayListInt(picycle);
			int moveToWhere = 0;

			/**** INVERESE THE CYCLE ****/
			for (int i = 0; i < picycle.size() ; i++) {
				
				moveToWhere = (picycle.size() - 1) - i;
				inversecycle.set(moveToWhere, picycle.get(i));
			}
		}
		/**** IF KEY IS NOT PROVIDED ****/
		else {
			errorMsg("noKeyDecrypt");
		}

		return inversecycle;
	}

	/**** RETURNS A RANDOM PiCYCLE ACCORDING TO THE MSG'S LEN ****/
	static ArrayList<Integer> getRandPiCycle() {

		if (msglen == 0) {
			errorMsg("noMsg");
		}

		/**** DECLARE VARIABLES ****/
		// FOR CREATING PiCYCLE
		ArrayList<Integer> al = new ArrayList<Integer>();
		int min = 7;
		int max = (int)(msglen - (msglen*.1));
		int cycleLen = min;

		// FOR SHUFFLING
		int currIndex = 0;
		int temp = 0;
		int randIndex = 0;

		/**** GETTING A RANDOM PiCYCLE LENGTH***/
		// IF MSG LEN IS 7 OR HIGHER
		if (msglen > min) {
			// GET RANDOM LEN FROM MIN TO MAX
			cycleLen = (int)(Math.random()*(max-min+1)+min);
		}

		/**** CREATE A CYCLE ****/
		for (int i = 0; i < cycleLen; i++) {
			al.add(i);
		}

		// System.out.println("min: " + min);
		// System.out.println("max: " + max);
		// System.out.println("msglen: " + msglen + "\n");

		// System.out.println("Original: " + al + "\n");

		/**** SHUFFLE ARRAYLIST ****/
		// WHILE THERE ARE STILL ELEMENTS TO SHUFFLE
		currIndex = al.size();
		while(currIndex != 0) {
			// PICK A REMAINING ELEMENT
			randIndex = (int)(Math.random() * currIndex);
			currIndex -= 1;

			// SWAP THE VALUE
			temp = al.get(currIndex);
			al.set(currIndex, al.get(randIndex));
			al.set(randIndex, temp);
		}

		// System.out.println("Shuffled: " + al  + "\n");

		return al;
	}

	/**** RETURNS ALL POSSIBLE CYCLE LEN ****/
	static ArrayList<Integer> getPossiblePiCycleLen(int lenNumber) {

		/**** DECLARE VARIABLES ****/
		ArrayList<Integer> possibleLenValue = new ArrayList<Integer>();
		int smallVal = 0;
		int bigVal = 0;

		/**** TO AVOID ITERATING ON A ALREADY FOUND FACTOR AGAIN ****/
		int sqrLen = (int)Math.sqrt(lenNumber);

		/**** CHECK IF IT A FACTOR ****/
		for (int number = 1; number <= sqrLen; number++) {
			
			// IF ITS A FACTOR - DOES NOT HAVE REMAINDER
			if ((lenNumber % number) == 0) {

				// GET BOTH VALUE(FACTOR)
				smallVal = number;
				bigVal = lenNumber/number;

				/**** ADD TO THE ARRAYLIST IF ITS NOT ADDED YET ****/
				possibleLenValue.add(smallVal);
				
				if (!possibleLenValue.contains(bigVal)) {
					possibleLenValue.add(bigVal);
				}
			}
		}

		Collections.sort(possibleLenValue);

		return possibleLenValue;
	}

	/**** CHECK IF THE CYCLE IS VALID (EVERY NUMBER IS PRESENT) ****/
	static Boolean checkCycle(ArrayList<Integer> cycle) {

		/**** DECLARE VARIABLES ****/
		int len = cycle.size();
		int index = 1;

		/**** SET THE INDEX AND LEN ACCORDING TO START STATE ****/
		// IF THE CYCLE STARTS AT 0
		if (cycle.contains(0)) {
			index = 0;
			len = len - 1;
		}

		/**** CHECK IF EVERY NUMBER IS IN THE CYCLE ****/
		for (int i = index; i <= len ; i++ ) {
			
			if (!cycle.contains(i)) { return false; }
		}

		return true;
	}

	/**** CHECK IF THE STRING IS NUMERIC ****/
	static Boolean isNumeric(String num) {

		// IF THE STRING IS NULL
		if (num == null) {
			return false;
		}

		// IF IT CATCHES AN ERROR ITS NOT A NUMERIC
		try { int number = Integer.parseInt(num);}
		catch (NumberFormatException e) { return false; }

		return true;
	}

	/**** RETURNS A COPY OF A STRING ARRAYLIST SO THAT IT DOES NOT CLEAR AS WELL WITH THE ORIGINAL ****/
	static ArrayList<String> copyArrayListStr(ArrayList<String> al) {

		/**** DECLARE VARIABLES ****/
		ArrayList<String> copy = new ArrayList<String>();

		/**** ADD EVERY LETTER IN THE ARRAYLIST PROVIDED TO THE NEW ARRAYLIST ****/
		for (String letter : al) {
			copy.add(letter);
		}

		return copy;
	}

	/**** RETURNS A COPY OF A INTEGER ARRAYLIST SO THAT IT DOES NOT CLEAR AS WELL WITH THE ORIGINAL ****/
	static ArrayList<Integer> copyArrayListInt(ArrayList<Integer> al) {

		/**** DECLARE VARIABLES ****/
		ArrayList<Integer> copy = new ArrayList<Integer>();

		/**** ADD EVERY NUMBER IN THE ARRAYLIST PROVIDED TO THE NEW ARRAYLIST ****/
		for (Integer number : al) {
			copy.add(number);
		}

		return copy;
	}

	/**** PRINTS ERROR MESSAGES ACCORDING TO TYPE PROVIDED ****/
	static void errorMsg(String type) {

		// System.out.println("\n");

		switch (type) {
			case "ErrorCycle":
				System.out.println("There's an error in your key cycle.");
				break;
			case "noMsg":
				System.out.println("Enter a valid text to encrypt/decrypt.");
			case "noKeyDecrypt":
				System.out.println("Enter a valid key to decrypt.");
		}

		// // MIGHT NOT NEED
		// System.out.println("You can type");
		// System.out.println("java LocalTransposition < msg.txt 0,1,2,3,4");
		// System.out.println("\n");

		System.exit(0);
	}
}