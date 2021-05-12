import java.io.*;
import java.util.*;

import java.util.LinkedList;

public class LocalTransposition {
	
	// EXAMPLE CALL:
	// type testfiles/message.txt | java LocalTrans cycle > outputfile.txt
	public static void main(String[] args) {
		try {

			int intCycle = Integer.parseInt(args[0]);

			LinkedList<Integer> cycle = llCycle(intCycle);
			System.out.println("Size is: " + cycle.size() + "\n");
			System.out.println("Cycle is: " + checkCycle(cycle) + "\n");

			while(!cycle.isEmpty()) {
				System.out.println(cycle.pop());
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public static String ltcypher(String msg, int intcycle) {

		// DECLARE VARIABLES
		String encyptedMsg = "";
		LinkedList<Integer> cycle = llCycle(intcycle);
		char msgBlock = new char[][]; 




		return encyptedMsg;

	}

	// GET CYCLE INTO LINKEDLIST
	static LinkedList<Integer> llCycle(int intcycle) {

		// VARIABLES
		LinkedList<Integer> cycle = new LinkedList<Integer>();

		while (intcycle > 0) {

			// GET THE LAST NUMBER AND PUSH TO STACK
			cycle.push(intcycle % 10);
			intcycle = intcycle / 10;
		}

		if (checkCycle(cycle) == false) {
			errorMsg("ErrorCycle");
		}

		return cycle;
	}

	// CHECK IF THE CYCLE IS VALID (EVERY NUMBER IS PRESENT)
	static Boolean checkCycle(LinkedList<Integer> cycle) {

		// VARIABLES
		boolean check = true;
		int len = cycle.size();

		for (int i = 1; i <= len ; i++ ) {
			
			if (cycle.contains(i) == false) {
				check = false;
			}
		}

		return check;
	}



	static void errorMsg(String type) {

		switch (type) {
			case "ErrorCycle":
				System.out.println("There's an error in your cycle.");
				break;
		}

		System.exit(0);
	}
}