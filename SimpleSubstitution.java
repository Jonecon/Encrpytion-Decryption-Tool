import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleSubstitution {
    public static final Map<Character,Character> substitutionMap = new HashMap<>();
//    public static void main(String[] args)
//    {
//        try
//        {
//            if(args.length == 1)
//            {
//                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]))); //args[0] is the file name
//                Stream<String> lines = reader.lines().flatMap(words -> Arrays.stream(words.split(" ")));
//                String joinedWords = lines.collect(Collectors.joining());
//                String output = "";
//                if(joinedWords.charAt(0) >= 97 && joinedWords.charAt(0) <= 122)
//                {
//                    output = encrypt(joinedWords);
//                }
//                else if(joinedWords.charAt(0) >= 65 && joinedWords.charAt(0) <= 90)
//                {
//                    output = decrypt(joinedWords);
//                }
//                System.out.println(output);
//            }
//        }
//        catch (Exception e)
//        {
//            System.err.println(e.getMessage());
//        }
//    }
//
//    public static void Setup()
//    {
//        //Set up alphabet
//        List<Character> alphabetS = new LinkedList<>();
//        List<Character> alphabetC = new LinkedList<>();
//        for(int i = 0; i < 26; i++)
//        {
//            alphabetS.add((char)(97 + i));
//            alphabetC.add((char)(65 + i));
//        }
//
//        //Encryption and Decryption
//        for(int i = 0; i < 26; i++)
//        {
//            int size = alphabetC.size(); //The size is the multiplier for the random
//            int random = (int)(Math.random()*size); //The random number
//            char a = alphabetS.get(i);
//            char b = alphabetC.get(random);
//            substitutionMap.put(a,b); //Put the keys on the map
//            substitutionMap.put(b,a);
//            alphabetC.remove(random); //Remove the element to stop random from getting the same letter
//        }
//    }

    public static void Setup(String key) {
        key = completeKey(key);
        for(int i = 0; i < 26; i++) {
            char a = (char)(97 + i);
            char b = key.charAt(i);
            substitutionMap.put(a, b);
            substitutionMap.put(b, a);
        }
        //Add other characters to stop problems
        substitutionMap.put('\n','\n');
        substitutionMap.put('\t','\t');
        for(int i = 32; i <= 64; i++)
        {
            char c = (char)i;
            substitutionMap.put(c,c);
        }
        for(int i = 91; i <= 96; i++)
        {
            char c = (char)i;
            substitutionMap.put(c,c);
        }
        for(int i = 123; i <= 126; i++)
        {
            char c = (char)i;
            substitutionMap.put(c,c);
        }

    }

    public static String Substitution(String text, String key)
    {
        //Make sure the map is empty otherwise Decryption wouldn't be possible
        if(substitutionMap.isEmpty())
        {
            Setup(key); //Set up
        }
        String output;
        char[] charArray = new char[text.length()]; //Set up charArray
        int i = 0;
        for(char c: text.toCharArray())
        {
            //Ensure the key is in the map to prevent null pointer exceptions
            if(substitutionMap.containsKey(c))
            {
                charArray[i] = substitutionMap.get(c); //Get the value
            }
            i++;
        }
        output = String.valueOf(charArray); //Turn charArray into String
        return output;
    }

    public static String decrypt(String ciphertext, String key) {
        ciphertext = ciphertext.toUpperCase(); // make sure the ciphertext is capitalised
        return Substitution(ciphertext, key);
    }
    public static String encrypt(String plaintext, String key) {
        plaintext = plaintext.toLowerCase(); // make sure the ciphertext is all lower case
        return Substitution(plaintext, key);
    }

    // This method takes in a key, removes duplicate letters and then adds the rest of the alphabet
    // eg. the key "testkey" becomes "teskyabcdfghijlmnopqruvwxz"
    public static String completeKey(String inputKey) {
        inputKey = inputKey.toUpperCase();
        String outputKey = "";
        // remove any duplicates in the input string
        for (int i = 0; i < inputKey.length(); i++) {
            if (Character.isLetter(inputKey.charAt(i))) {
                String c = Character.toString(inputKey.charAt(i));
                if (!outputKey.contains(c)) {
                    outputKey += c;
                }
            }
        }
        // add the rest of the alphabet
        for (int i = 0; i < 26; i++) {
            String c = Character.toString((char)(i + 65));
            if (!outputKey.contains(c)) {
                outputKey += c;
            }
        }
        return outputKey;
    }
    public static String randomKey() {
        ArrayList<Character> alphabet = new ArrayList();
        alphabet.add('A');
        alphabet.add('B');
        alphabet.add('C');
        alphabet.add('D');
        alphabet.add('E');
        alphabet.add('F');
        alphabet.add('G');
        alphabet.add('H');
        alphabet.add('I');
        alphabet.add('J');
        alphabet.add('K');
        alphabet.add('L');
        alphabet.add('M');
        alphabet.add('N');
        alphabet.add('O');
        alphabet.add('P');
        alphabet.add('Q');
        alphabet.add('R');
        alphabet.add('S');
        alphabet.add('T');
        alphabet.add('U');
        alphabet.add('V');
        alphabet.add('W');
        alphabet.add('X');
        alphabet.add('Y');
        alphabet.add('Z');
        Collections.shuffle(alphabet);
        String key = "";
        for(int i = 0; i < 26; i++) {
            key += alphabet.get(i);
        }
        return key;

    }
}
