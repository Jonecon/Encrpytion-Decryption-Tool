import java.io.*;
import java.util.*;
import java.math.BigInteger;

public class RSA {

    public static void main(String[] args) {
        try {
            String keys = generateKey(BigInteger.valueOf(9967),BigInteger.valueOf(9973));
            String[] parts = keys.split(";");
            String publicKey = parts[0];
            String privateKey = parts[1];

            //Encrypt
            String message = "hel";
            byte[] encryptedMessage = encrypt(message.getBytes(), publicKey);
            byte[] decryptedMessage = decrypt(encryptedMessage, privateKey);
            System.out.println("Encrypting String: " + message);
        	System.out.println("String in Bytes: "+ bytesToString(message.getBytes()));
           	System.out.println("Decrypting Bytes: " + bytesToString(decryptedMessage));
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

    private static String bytesToString(byte[] encrypted)
    {
        String test = "";
        for (byte b : encrypted)
        {
            test += Byte.toString(b);
        }
        return test;
    }

    //Generate a public and private key from two distinct prime numbers.
    public static String generateKey(BigInteger p, BigInteger q){
        //Generate Public Key.
        BigInteger  n = p.multiply(q);
        BigInteger  limit = p.add(BigInteger.valueOf(-1)).multiply(q.add(BigInteger.valueOf(-1)));
        System.out.println("LIMIT: " + limit);
        int gcd = 1;
        BigInteger e;
        BigInteger  d;
        
        //Find d
        for (d = limit; d.compareTo(BigInteger.ZERO) > 0; d = d.subtract(BigInteger.ONE)){
        	System.out.println("Loop: " + d.toString());
            if (d.gcd(limit).equals(BigInteger.ONE))
            	break;
        }

        //Find e
        e = d.modInverse(limit);

        /*Find public key
        for (int i = 0; i <= limit.longValue(); i++) {
            //int  x = 1 + (i * limit);
            BigInteger x = limit.multiply(BigInteger.valueOf(i)).add(BigInteger.valueOf(1));
 
            // d is for private key exponent
            if (x.remainder(e).equals(BigInteger.valueOf(0))) {
                d = x.divide(e);
                System.out.println("FOUND D");
                break;
            }
        }*/

        //d = e.modInverse(limit);

        String publicKey = n + "," + e;
        String privateKey = n + "," + d;

        System.out.println("Public Key: " + publicKey);
        System.out.println("Private Key: " + privateKey);

        return publicKey + ";" + privateKey;
    }
}