import java.io.*;
import java.util.*;

public class LocalTransposition {
	
	// EXAMPLE CALL:
	// type testfiles/message.txt | java LocalTrans cycle > outputfile.txt
	public static void main(String[] args) {
		try {

			if (args.length == 0) {
				errorMsg("NoCycle");
			}

			String strCycle = args[0];

			// FOR DEBUGGING
			// ArrayList<Integer> cycle = piCycle(strCycle);
			// System.out.println("\nSize is: " + cycle.size());
			// System.out.println("Cycle is: " + checkCycle(cycle) + "\n");
			// cycle.forEach((cycleNum) -> System.out.println(cycleNum));

			String encryptMsg = encrypt(Tools.readStdIn(), strCycle);

		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	// ENCRYPT THE MSG USING LOCAL TRANSPOSITION
	public static String encrypt(String msg, String intcycle) {

		// DECLARE VARIABLES
		String encyptedMsg = "";
		ArrayList<String> blockMessages = new ArrayList<String>();
		ArrayList<String> tempBlock = new ArrayList<String>();
		ArrayList<String> copy = new ArrayList<String>();
		ArrayList<ArrayList<String>> eachBlock = new ArrayList<ArrayList<String>>();
		ArrayList<Integer> cycle = piCycle(intcycle);
		String[] blockLetters = {};

		// SEPARATE THE MESSAGE INTO BLOCK SIZE OF THE CYCLE LEN
		String[] blocks = msg.split("(?<=\\G.{"+ cycle.size() +"})");
		Collections.addAll(blockMessages, blocks);

		// MAKE EACH BLOCK AN ARRAYLIST AND PUT INTO AN ARRAYLIST
		for (String block : blockMessages) {

			// SPLIT THE BLOCK THEN CONVERT INTO ARRAYLIST
			blockLetters = block.split("");
			Collections.addAll(tempBlock, block.split(""));
			copy = copyArrayList(tempBlock);

			// ADD THIS ARRAYLIST TO THE MAIN ARRAYLIST
			eachBlock.add(copy);
			tempBlock.clear();
		}

		int blockIndex = 0;

		System.out.println("BlockString:\n");
		for (String block : blockMessages ) {
			System.out.println(blockIndex + " " + block);
			blockIndex++;
		}

		blockIndex = 0;
		System.out.println("\nEachBlock:\n");
		for (ArrayList<String> block : eachBlock) {
			
			System.out.print(blockIndex + " ");
			for (String letter : block) {
				System.out.print(letter + " ");
			}
			System.out.print("\n");
			blockIndex++;
		}

		return encyptedMsg;
	}

	// GET CYCLE INTO ARRAYLIST
	static ArrayList<Integer> piCycle(String intcycle) {

		// VARIABLES
		ArrayList<Integer> cycle = new ArrayList<Integer>();

		// ADD EACH NUMBER TO THE ARRAYLIST
		for (String number : intcycle.split(",")) {

			// ADD TO ARRAYLIST IF THE STRING PROVIDED IS NUMERIC
			if (isNumeric(number)) {
				cycle.add(Integer.parseInt(number));
			} else {
				errorMsg("ErrorCycle");
			}
		}

		// THEN CHECK THE CYCLE ARRAY LIST IF VALID
		if (checkCycle(cycle) == false) {
			errorMsg("ErrorCycle");
		}

		return cycle;
	}

	// CHECK IF THE CYCLE IS VALID (EVERY NUMBER IS PRESENT)
	static Boolean checkCycle(ArrayList<Integer> cycle) {

		// VARIABLES
		boolean check = true;
		int len = cycle.size();
		int index = 1;

		// IF THE CYCLE STARTS AT 0
		if (cycle.contains(0) == true) {
			index = 0;
			len = len - 1;
		}

		// CHECK IF EVERY NUMBER IS IN THE CYCLE
		for (int i = index; i <= len ; i++ ) {
			
			if (cycle.contains(i) == false) {
				check = false;
			}
		}

		return check;
	}

	// CHECK IF THE STRING IS NUMERIC
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

	// RETURNS A COPY OF AN ARRAYLIST SO THAT IT DOES NOT CLEAR AS WELL WITH THE ORIGINAL
	static ArrayList<String> copyArrayList(ArrayList<String> al) {

		// CREATE A NEW ARRAYLIST
		ArrayList<String> copy = new ArrayList<String>();

		for (String letter : al) {
			copy.add(letter);
		}

		return copy;
	}

	static void errorMsg(String type) {

		System.out.println("\n");

		switch (type) {
			case "ErrorCycle":
				System.out.println("There's an error in your cycle.");
				break;
			case "NoCycle":
				System.out.println("Enter a valid cycle.");
				break;
		}

		System.out.println("You can type");
		System.out.println("java LocalTransposition < msg.txt 0,1,2,3,4");
		System.out.println("\n");

		System.exit(0);
	}
}