import java.io.*;
import java.util.*;

public class CryptotronThreeThousand {

    public static void main(String[] args) {
        // args[0] is action(encrypt/decrypt/letterfrequency/indexofcoincidence)
        // args[1] is name of cipher
        // args[2] is the key
        // args[3] RSA key part 2
        // args[4] Input is a number (n) or text (b)
        // args[5] RSA decrypt with public key
        try {
            // if no args provided, show how to use
            if (args.length == 0) {
                howToUse();
                return;
            }
            // action would be either 'encrypt' or 'decrypt'
            String action = args[0].toLowerCase();

            // what cipher to use
            String cipher = null;
            if (action.equals("encrypt")) {
                if (args.length > 1) {
                    cipher = args[1].toLowerCase();
                } else {
                    System.err.println("ERROR: A cipher must be provided to encrypt the message");
                    howToUse();
                    return;
                }
            } else {
                cipher = args.length > 1 ? args[1].toLowerCase() : null;
            }

            // key is stored as a String, if the cipher needs an int, then parse it in your class
            // key equals NULL if it is not provided in args
            String key = args.length > 2 ? args[2] : null;

            // this is the string that will need to be encrypted/decrypted
            // it still has punctuation and line breaks etc.
            String inputText = "";
            byte[] byteInputText = null;

            if (cipher != null && cipher.equals("rsa") && !action.equals("encrypt")) {
                byteInputText = Tools.readStdInBytes();
                //System.out.println(new String(byteInputText));
            } else {
                inputText = Tools.readStdIn();
            }

            switch (action) {
                case "encrypt":
                    switch (cipher) {
                        case "caesar":
                            if (key == null) {
                                key = String.valueOf(Caesar.randomKey());
                                System.err.println("KEY: " + key);
                            }
                            System.out.println(Caesar.encrypt(inputText, key));
                            break;
                        case "vigenere":
                            if (key == null) {
                                key = Vigenere.randomKey(5, 10);
                                System.err.println("KEY: " + key);
                            }
                            System.out.println(Vigenere.encrypt(inputText, key));
                            break;
                        case "simplesubstitution":
                            if (key == null) {
                                key = SimpleSubstitution.randomKey();
                                System.err.println("KEY: " + key);
                            }
                            System.out.println(SimpleSubstitution.encrypt(inputText, key));
                            break;
                        case "localtransposition":
                            System.out.println(LocalTransposition.transposition(inputText, key, "encrypt"));
                            break;
                        case "playfair":
							if(key == null){
								key = Playfair.randomKey();
								System.err.println("KEY: " + key);
							}
                            System.out.println(Playfair.encrypt(inputText, key));
                            break;
                        case "fiestel":
                            if(key == null){
                                key = Fiestel.genRandomKey(20);
                                System.out.println("Your key is: " + key);
                            }

                            /*
                            Fiestel cypher is encrypting and decrypting in-program for the most part since something weird happens to the ascii when it's fed back in through system.in or when it's put into a word file
                            Decryption from an encrypted text file mostly works but strangely random ascii characters are flipped
                            It's definitely not something wrong with the encryption/decryption since doing both successively in-program works just fine.
                             */

                            String s = Fiestel.encrypt(inputText, key);

                            System.out.print(s);
                            Fiestel.writeToFile(s, "encryptedText.txt");
                            Fiestel.writeToFile(Fiestel.decrypt(s,key), "decryptedText.txt");

                            System.out.println(Fiestel.decrypt(s,key));

                            //byte[] encryptedMessage = Fiestel.encrypt(inputText.getBytes(), key);

                            //System.out.println("Input Length:  " + inputText.length());
                            //System.out.println("Output Length: " + s.length());

                            //System.out.print(inputText);
                            //System.out.print(Fiestel.cleanMessage(inputText));

                            //byteInputText = Tools.readStdInBytes();
                            //byte[] encryptedMessage = Fiestel.encrypt(byteInputText, key);
                            //Fiestel.writeToFile(encryptedMessage);
                            //Tools.outputBytes(encryptedMessage);

                            break;
                        case "rsa":
                            if (args[3] == null || args[3].contains("n") || args[3].contains("b") || args[3].equals("1") || args[3].equals("0")){
                                System.err.println("Incorrect key supplied");
                                break;
                            }
                            //My key is in the form N e/d so I would need 2 args for this.
                            String RSAKey = key + "," + args[3];

                            //
                            boolean isByte = true;
                            if (args[4].contains("n"))
                                isByte = false;

                            byte[] encrpytedMessage = RSA.encrypt(inputText.getBytes(), RSAKey, isByte);
                            Tools.outputBytes(encrpytedMessage);
                            break;
                        default:
                            unrecognisedCipher();
                    }
                    break;
                case "decrypt":
                    // if a cipher is not specified
                    if (cipher == null) {
                        if (key != null) {
                            // attempt to decrypt without knowing the cipher, but knowing the key

                        } else {
                            // attempt to decrypt without knowing anything

                        }
                    } else {
                        if (key != null) {
                            // decrypt with the cipher AND the key
                            switch (cipher) {
                                case "caesar":
                                    System.out.println(Caesar.decrypt(inputText, key));
                                    break;
                                case "vigenere":
                                    System.out.println(Vigenere.decrypt(inputText, key));
                                    break;
                                case "simplesubstitution":
                                    System.out.println(SimpleSubstitution.decrypt(inputText, key));
                                    break;
                                case "localtransposition":
                                    System.out.println(LocalTransposition.transposition(inputText, key, "decrypt"));
                                    break;
                                case "playfair":
                                    System.out.println(Playfair.decrypt(inputText, key));
                                    break;
                                case "fiestel":

                                    //System.out.println(new String(Fiestel.decrypt(byteInputText, key)));
                                    //String s = new String(byteInputText);
                                    //String fixedText = inputText.substring(0,inputText.length());
                                    //System.out.println(fixedText);

                                    String m = Fiestel.decrypt(inputText,key);
                                    System.out.print(m);

                                    //System.out.println("Input Length:  " + inputText.length());
                                    //System.out.println("Output Length: " + m.length());

                                    break;
                                case "rsa":
                                    //System.out.println("Inside RSA");
                                    if (args[3] == null || args[3].contains("n") || args[3].contains("b") || args[3].equals("1") || args[3].equals("0")){
                                        System.err.println("Incorrect key supplied");
                                        break;
                                    }

                                    //My key is in the form N e/d so I would need 2 args for this.
                                    String RSAKey = key + "," + args[3];

                                    //Figure out whether the string is an int or a byte.
                                    boolean isByte = true;
                                    if (args[4].contains("n"))
                                        isByte = false;


                                    if (args[5] == null || args[5].equals("0")){
                                        byte[] output = RSA.decrypt(byteInputText, RSAKey, isByte);
                                        if (isByte)
                                            System.out.println(new String(output));
                                        else
                                            System.out.println(Tools.byteArrayToLong(output));

                                    }
                                    else{
                                        byte[] output = RSA.decrypt(byteInputText, RSAKey, isByte, true);
                                        if (isByte)
                                            System.out.println(new String(output));
                                        else
                                            System.out.println(Tools.byteArrayToLong(output));
                                    }
                                    
                                    break;
                                default:
                                    unrecognisedCipher();
                            }
                        } else {
                            // decrypt with ONLY the cipher, key unknown
                            switch (cipher) {
                                case "caesar":
                                    System.out.println(Caesar.decryptWithoutKey(inputText));
                                    break;
                                case "vigenere":
                                    System.out.println(Vigenere.findKey(inputText));
                                    break;
                                case "simplesubstitution":

                                    break;
                                case "localtransposition":

                                    break;
                                case "playfair":
									System.out.println(Playfair.decrypt(inputText));
                                    break;
                                case "fiestel":

                                    //need to

                                    //key length of about 6 is ok for basically trying to brute force finding the key
                                    if(key == null){
                                        key = Fiestel.genRandomKey(6);
                                        System.out.println("Your key is: " + key);
                                    }
                                    String s = Fiestel.encrypt(Fiestel.cleanMessage(inputText), key);
                                    System.out.print(s);
                                    System.out.println("smart decrypt: ");
                                    System.out.println(Fiestel.smartDecrypt(s));
                                    break;
                                default:
                                    unrecognisedCipher();
                            }
                        }
                    }
                    break;
                case "letterfrequency":
                    Hashtable<Character, Integer> letters = Tools.letterFrequency(inputText);
                    int total = letters.values().stream().mapToInt(Integer::intValue).sum();
                    Enumeration<Character> enumeration = letters.keys();
                    List<Character> list = Collections.list(enumeration);
                    Collections.sort(list);
                    System.out.println(" letter    | count      | %");
                    System.out.println("-----------+------------+-----------");
                    for (Character letter : list) {
                        int count = letters.get(letter);
                        double percentage = (double)count / total * 100.0;
                        System.out.println(" " + padRight(Character.toString(letter), 9) + " | " + padRight(String.valueOf(count), 10) + " | " +  String.format("%.2f", percentage) + "%");
                    }
                    break;
                case "indexofcoincidence":
                    System.out.println("Index of coincidence: " + Tools.indexOfCoincidence(inputText));
                    break;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static void unrecognisedCipher() {
        System.err.println("ERROR: Unrecognised cipher");
        System.err.println("try one of these:");
        System.err.println("--caesar");
        System.err.println("--vigenere");
        System.err.println("--simplesubstitution");
        System.err.println("--localtransposition");
        System.err.println("--playfair");
        System.err.println("--fiestel");
        System.err.println("--rsa");
    }

    private static void howToUse() {
        System.err.println("To use Cryptotron3000, use the command:");
        System.err.println("type(or cat if using linux) filename.txt | java CryptotronThreeThousand <action> <cipher> <key> <optional e|d RSA key> <b|n> <Decrypt Public Key: 0|1> > destinationfilename.txt");
        System.err.println("action being one of the following:");
        System.err.println("encrypt, decrypt, letterfrequency, indexofcoincidence");
    }

    private static String padRight(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(inputString);
        while (sb.length() < length) {
            sb.append(' ');
        }

        return sb.toString();
    }
}