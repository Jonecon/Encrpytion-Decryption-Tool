import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleSubstitution {
    public static final Map<Character,Character> substitutionMap = new HashMap<>();
    public static void main(String[] args)
    {
        try
        {
            if(args.length == 1)
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]))); //args[0] is the file name
                Stream<String> lines = reader.lines().flatMap(words -> Arrays.stream(words.split(" ")));
                String joinedWords = lines.collect(Collectors.joining());
                String output = "";
                if(joinedWords.charAt(0) >= 97 && joinedWords.charAt(0) <= 122)
                {
                    output = encrypt(joinedWords);
                }
                else if(joinedWords.charAt(0) >= 65 && joinedWords.charAt(0) <= 90)
                {
                    output = decrypt(joinedWords);
                }
                System.out.println(output);
            }
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
    }

    public static void Setup()
    {
        //Set up alphabet
        List<Character> alphabetS = new LinkedList<>();
        List<Character> alphabetC = new LinkedList<>();
        for(int i = 0; i < 26; i++)
        {
            alphabetS.add((char)(97 + i));
            alphabetC.add((char)(65 + i));
        }

        //Encryption and Decryption
        for(int i = 0; i < 26; i++)
        {
            int size = alphabetC.size(); //The size is the multiplier for the random
            int random = (int)(Math.random()*size); //The random number
            char a = alphabetS.get(i);
            char b = alphabetC.get(random);
            substitutionMap.put(a,b); //Put the keys on the map
            substitutionMap.put(b,a);
            alphabetC.remove(random); //Remove the element to stop random from getting the same letter
        }
    }

    public static String Substitution(String text)
    {
        //Make sure the map is empty otherwise Decryption wouldn't be possible
        if(substitutionMap.isEmpty())
        {
            Setup(); //Set up
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

    public static String decrypt(String text) { return Substitution(text);} //Decrypt
    public static String encrypt(String text) { return Substitution(text);} //Encrypt
}
