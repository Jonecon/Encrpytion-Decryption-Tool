import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleSubstitution {
    private static final Map<Character,Character> substitutionMap = new HashMap<>();
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
        //Set up map
        //Encryption
        substitutionMap.put('a','Q');
        substitutionMap.put('b','W');
        substitutionMap.put('c','E');
        substitutionMap.put('d','R');
        substitutionMap.put('e','T');
        substitutionMap.put('f','Y');
        substitutionMap.put('g','U');
        substitutionMap.put('h','I');
        substitutionMap.put('i','O');
        substitutionMap.put('j','P');
        substitutionMap.put('k','A');
        substitutionMap.put('l','S');
        substitutionMap.put('m','D');
        substitutionMap.put('n','F');
        substitutionMap.put('o','G');
        substitutionMap.put('p','H');
        substitutionMap.put('q','J');
        substitutionMap.put('r','K');
        substitutionMap.put('s','L');
        substitutionMap.put('t','Z');
        substitutionMap.put('u','X');
        substitutionMap.put('v','C');
        substitutionMap.put('w','V');
        substitutionMap.put('x','B');
        substitutionMap.put('y','N');
        substitutionMap.put('z','M');
        //Decryption
        substitutionMap.put('Q','a');
        substitutionMap.put('W','b');
        substitutionMap.put('E','c');
        substitutionMap.put('R','d');
        substitutionMap.put('T','e');
        substitutionMap.put('Y','f');
        substitutionMap.put('U','g');
        substitutionMap.put('I','h');
        substitutionMap.put('O','i');
        substitutionMap.put('P','j');
        substitutionMap.put('A','k');
        substitutionMap.put('S','l');
        substitutionMap.put('D','m');
        substitutionMap.put('F','n');
        substitutionMap.put('G','o');
        substitutionMap.put('H','p');
        substitutionMap.put('J','q');
        substitutionMap.put('K','r');
        substitutionMap.put('L','s');
        substitutionMap.put('Z','t');
        substitutionMap.put('X','u');
        substitutionMap.put('C','v');
        substitutionMap.put('V','w');
        substitutionMap.put('B','x');
        substitutionMap.put('N','y');
        substitutionMap.put('M','z');
    }

    public static String Substitution(String text)
    {
        Setup(); //Set up
        String output;
        char[] charArray = new char[text.length()]; //Set up charArray
        int i = 0;
        for(char c: text.toCharArray())
        {
            charArray[i] = substitutionMap.get(c); //Get the value
            i++;
        }
        output = String.valueOf(charArray); //Turn charArray into String
        return output;
    }

    public static String decrypt(String text) { return Substitution(text);} //Decrypt
    public static String encrypt(String text) { return Substitution(text);} //Encrypt
}
