import java.io.*;
import java.util.*;
import java.math.BigInteger;

public class RSA {

    public static void main(String[] args) {
        try {
        	if (args.length  == 1 && args[0].contains("-t")){
        		String message = "t";
        		String keys = generateKey(32);
            	String[] parts = keys.split(";");
           	 	String publicKey = "27756768072734402834091752684548002341515313684541231730375463129226702955993773195423563147385686679836905393210192315834279479600785518875524335868708881490870018380735246442562293960088453105749148291607765353732864085524334314860798227020143255156312165827986525091461024270071663366370132151298404670669285540149280350250047416913764790582920606034077868714156535219718311247744592725428905184889664585462090210602986256756112925513953807497490863278034996623207375752633245561412727181726973702550955297519594484668325059124882590061698300839959908707763455435379954388873305802067162836349135157836061827766229,65537";
            	String privateKey = "27756768072734402834091752684548002341515313684541231730375463129226702955993773195423563147385686679836905393210192315834279479600785518875524335868708881490870018380735246442562293960088453105749148291607765353732864085524334314860798227020143255156312165827986525091461024270071663366370132151298404670669285540149280350250047416913764790582920606034077868714156535219718311247744592725428905184889664585462090210602986256756112925513953807497490863278034996623207375752633245561412727181726973702550955297519594484668325059124882590061698300839959908707763455435379954388873305802067162836349135157836061827766229,19836790610230270313568753841130579453889447900313680372241566849010346618702112625754206435053533225865101208504902230751028395960480207326745705770522268068234417976082163321944705467272882159909255207501608341437449916439618021959125479636578568469050962218066204979000564471021659476184107901954062278714644953869822405114940432156101775643518612480052239051009336904914510143196291548792706737304579692612719920611214222104821127806763726753444972379687222593204865263108212561641533070978766482190301709325125470442743777966055642077865669330583719739963108669003750269029739967967099938153225423807951419353249";

        		byte[] encryptedMessage = encrypt(message.getBytes(), publicKey);
            	byte[] decryptedMessage = decrypt(encryptedMessage, privateKey);

            	System.out.println("Original Message: " + message);
	           	System.out.println("Encrypted Message: ");
	            System.out.println(new String(encryptedMessage));
	            System.out.println("Decrypted String: " + new String(decryptedMessage)); 
	            return;
        	}
        	if (args.length == 2 && args[0].contains("-g")){
        		System.out.println(generateKey(Integer.parseInt(args[1])));
        		return;
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

        //System.out.println("Key generated with N value: " + n);
        //System.out.println("e value: " + e);
        //System.out.println("d value: " + d);

        return publicKey + ";" + privateKey;
    }
}