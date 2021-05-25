import java.io.*;
import java.util.*;
import java.math.BigInteger;

public class RSA {

    public static void main(String[] args) {
        try {
            //String keys = generateKey(BigInteger.valueOf(9967),BigInteger.valueOf(9973));
            String keys = generateKey(1024);
            String[] parts = keys.split(";");
            String publicKey = parts[0];
            String privateKey = parts[1];

            //Encrypt
            String message = "Testing out a larger string now that I have set up random generation from a given bit size. Hopefully this string will work, but I should still setup breaking messages into block if they're above the given n-1";
            message += "Testing out a larger string now that";
            byte[] encryptedMessage = encrypt(message.getBytes(), publicKey);
            byte[] decryptedMessage = decrypt(encryptedMessage, privateKey);
            System.out.println("Original Message: " + message);
            System.out.println("Encrypted Message: ");
            System.out.println(new String(encryptedMessage));
        	System.out.println("Decrypted String: " + new String(decryptedMessage)); 	

        } catch(Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static byte[] encrypt(byte[] message, String key){
    	//Message
    	BigInteger bIMessage = new BigInteger(message);

    	//Key
    	String[] keyParts = key.split(",");
    	BigInteger e = new BigInteger(keyParts[1]);
    	BigInteger n = new BigInteger(keyParts[0]);

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
    	//Check if they're prime


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
        }catch(Exception ex){
        	System.out.println("Re attempting generation of key.");
        	return generateKey(1024);
        }

        String publicKey = n + "," + e;
        String privateKey = n + "," + d;

        System.out.println("Key generated with N value: " + n);
        System.out.println("e value: " + e);
        System.out.println("d value: " + d);

        return publicKey + ";" + privateKey;
    }
}