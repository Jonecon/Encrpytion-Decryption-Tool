import java.io.*;

public class CryptotronThreeThousand {

    public static void main(String[] args) {
        // args[0] is action(encrypt / decrypt)
        // args[1] is name of cipher
        // args[2] is the key
        try {
            // action would be either 'encrypt' or 'decrypt'
            String action = args[0].toLowerCase();
            // what cipher to use
            String cipher = args[1].toLowerCase();
            String key = args[2];
            if (action.equals("encrypt")) {
                switch (cipher) {
                    case "caesar":

                        break;
                    case "vigenere":

                        break;
                    case "simplesubstitution":

                        break;
                    case "localTransposition":

                        break;
                    case "playfair":

                        break;
                    case "fiestel":

                        break;
                }
            } else if (action.equals("decrypt")) {
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

                                break;
                            case "vigenere":

                                break;
                            case "simplesubstitution":

                                break;
                            case "localTransposition":

                                break;
                            case "playfair":

                                break;
                            case "fiestel":

                                break;
                        }
                    } else {
                        // decrypt with ONLY the cipher, key unknown
                        switch (cipher) {
                            case "caesar":

                                break;
                            case "vigenere":

                                break;
                            case "simplesubstitution":

                                break;
                            case "localTransposition":

                                break;
                            case "playfair":

                                break;
                            case "fiestel":

                                break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}