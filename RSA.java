import java.io.*;
import java.util.*;
import java.math.BigInteger;

public class RSA {

    public static void main(String[] args) {
        try {
        	if (args.length == 2){
        		if (args[0].contains("-t")){
        			String message = args[1];
	        		String keys = generateKey(1024);
	            	String[] parts = keys.split(";");
	           	 	String publicKey = parts[0];
	            	String privateKey = parts[1];

	        		byte[] encryptedMessage = encrypt(message.getBytes(), publicKey);

	        		if (encryptedMessage == null)
	        			return;

	            	byte[] decryptedMessage = decrypt(encryptedMessage, privateKey);

	            	System.out.println("Original Message: " + message);
		           	System.out.println("Encrypted Message: ");
		            System.out.println(new String(encryptedMessage));
		            System.out.println("Decrypted String: " + new String(decryptedMessage)); 
		            return;
        		}else if (args[0].contains("-g")){
        			System.out.println(generateKey(Integer.parseInt(args[1])));
        			return;
        		}else{
        			System.err.println("Usage: java RSA <e/d> <key N> <key e/d>");
        			return;
        		}
        	}
        	if (args.length != 3){
        		System.err.println("Usage: java RSA <e/d> <key N> <key e/d>");
        		return;
        	}
        	if (!args[0].contains("e") && !args[0].contains("d")){
        		System.err.println("Usage: java RSA <e/d> <key pair \"N,e/d\">");
        		return;
        	}
        	//Setup key
        	String key = args[1] + "," + args[2];

        	//Get bytes from standard input.
        	ArrayList<Byte> message = new ArrayList<Byte>();
        	BufferedInputStream byteStream = new BufferedInputStream(System.in);
        	BufferedOutputStream outputStream = new BufferedOutputStream(System.out);
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

           	//Encrypt/decrypt
           	byte[] output;
           	if (args[0].contains("e")){
           		output = encrypt(messageBytes, key);
           		outputStream.write(output);
           		outputStream.close();
           	}
           	else{
           		output = decrypt(messageBytes, key);
           		System.out.println(new String(output));
           	}
        } catch(Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.err.println("Usage: java RSA <e/d> <key N> <key e/d>");
        	return;
        }
    }


    //Overloaded encrypt method to deal with not being given a key.
    public static byte[] encrypt(byte[] message){
    	//Generate a key
    	String key = generateKey(1024);
    	
    	printKey(key);

    	//Encrypt message with this generated key
    	return encrypt(message, key);
    }

    //Overloaded encrypt method to encrypt a message with a given key
    public static byte[] encrypt(byte[] message, String key){ 
    	//Message
    	BigInteger bIMessage = new BigInteger(message);

    	//Key
    	String[] keyParts = key.split(",");
    	BigInteger e = new BigInteger(keyParts[1]);
    	BigInteger n = new BigInteger(keyParts[0]);

    	//Check to see if the message is too big.
    	if ((message.length * 8) > n.bitLength()){
    		System.err.println("Your message is too big, with a size of: " + (message.length * 8) + ", maximum size: " + (n.bitLength() - 1));
    		return null;
    	}	

    	//Encrypting.
        return bIMessage.modPow(e,n).toByteArray();
    }

    public static byte[] decrypt(byte[] message, String key){
    	//Message
        BigInteger bIMessage = new BigInteger(message);

        //Key
    	String[] keyParts = key.split(",");
    	BigInteger d = new BigInteger(keyParts[1]);
    	BigInteger n = new BigInteger(keyParts[0]);

    	//Decrypting.
        return bIMessage.modPow(d,n).toByteArray();
    }

    //Generates a key from a given bit size
    public static String generateKey(int bitLength){
    	Random rand = new Random();
    	BigInteger p = BigInteger.probablePrime(bitLength, rand);
    	BigInteger q = BigInteger.probablePrime(bitLength, rand);
    	return generateKey(p,q);
    }

    //Generate a public and private key from two distinct prime numbers.
    public static String generateKey(BigInteger p, BigInteger q){
        //Generate Public Key.
        int[] eValues = {65537,257,17,5,3};
        BigInteger  n = p.multiply(q);
        BigInteger  limit = p.add(BigInteger.valueOf(-1)).multiply(q.add(BigInteger.valueOf(-1)));
        int gcd = 1;
        BigInteger e = BigInteger.ZERO;
        BigInteger  d;
        
        
        //Find e
        for (int currE = 0; currE < eValues.length; currE++){
        	if (eValues[currE] < limit.longValue()){
        		e = BigInteger.valueOf(eValues[currE]);
        		break;
        	}
        }

        //Find d
        try {
        	d = e.modInverse(limit);
        }
        //If this d cannot be found, generate another random key.
        catch(Exception ex){
        	System.out.println("Re attempting generation of key.");
        	return generateKey(p.bitLength());
        }

        String publicKey = n + "," + e;
        String privateKey = n + "," + d;
        return publicKey + ";" + privateKey;
    }

    public static void printKey(String key){
    	//Split public and private key
    	String[] keyParts = key.split(";");

    	//Get components
    	String[] publicKeyParts = keyParts[0].split(",");
    	String[] privateKeyParts = keyParts[1].split(",");

    	BigInteger e = new BigInteger(publicKeyParts[1]);
    	BigInteger d = new BigInteger(privateKeyParts[1]);
    	BigInteger n = new BigInteger(privateKeyParts[0]);


    	System.out.println("e: " + e + "\n");
    	System.out.println("d: " + d + "\n");
    	System.out.println("N: " + n);
    }
}