import java.io.*;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CryptotronThreeThousand {

    public static void main(String[] args) {
        // args[0] is action(encrypt/decrypt/letterfrequency/indexofcoincidence)
        // args[1] is name of cipher
        // args[2] is the key
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
            String inputText = Tools.readStdIn();

            switch (action) {
                case "encrypt":
                    switch (cipher) {
                        case "caesar":
                            System.out.println(Caesar.encrypt(inputText, key));
                            break;
                        case "vigenere":
                            System.out.println(Vigenere.encrypt(inputText, key));
                            break;
                        case "simplesubstitution":

                            break;
                        case "localtransposition":

                            break;
                        case "playfair":

                            break;
                        case "fiestel":
                            String s = Fiestel.encrypt(inputText, key); //I just have encrypt + decrypt here rn for ease of testing
                            System.out.println("String s: " + s);
                            System.out.println(Fiestel.decrypt(s, key));
                            //System.out.println(Fiestel.encrypt(inputText, key));
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

                                    break;
                                case "localtransposition":

                                    break;
                                case "playfair":

                                    break;
                                case "fiestel":
                                    System.out.println(Fiestel.decrypt(inputText, key));
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

                                    break;
                                case "fiestel":

                                    break;
                                default:
                                    unrecognisedCipher();
                            }
                        }
                    }
                    break;
                case "letterfrequency":

                    break;
                case "indexofcoincidence":
                    System.out.println(Tools.indexOfCoincidence(inputText));
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
    }

    private static void howToUse() {
        System.err.println("To use Cryptotron3000, use the command:");
        System.err.println("type(or cat if using linux) filename.txt | java CryptotronThreeThousand action cipher key > destinationfilename.txt");
        System.err.println("action being one of the following:");
        System.err.println("encrypt, decrypt, letterfrequency, indexofcoincidence");
    }
}