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

			ArrayList<Integer> cycle = piCycle(strCycle);
			System.out.println("\nSize is: " + cycle.size());
			System.out.println("Cycle is: " + checkCycle(cycle) + "\n");

			cycle.forEach((cycleNum) -> System.out.println(cycleNum));

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
		ArrayList<Integer> cycle = piCycle(intcycle);
		String[] blockString = msg.split("(?<=\\G.{"+ cycle.size() +"})");

		System.out.println("\n");
		for (String block : blockString) {
			System.out.println(block);
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