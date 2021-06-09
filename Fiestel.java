import java.io.FileWriter;
import java.io.IOException;

public class Fiestel {

    //key is in the form of a decimal number each digit representing a function (0-9)
    //recommended key length is 20+ digits

    /*
    Pretty self explanatory
     */
    public static String encrypt(String m, String k){

        //convert message into bits
        boolean[] messageBits = StringToBitArray(m);
        //boolean[] messageBits = BytesToBitArray(bytes);

        //convert to left and right arrays
        //could do a slightly more efficient thing here but it's not really super relevant atm
        boolean[] leftM = new boolean[messageBits.length/2];
        boolean[] rightM = new boolean[messageBits.length/2];

        for(int i = 0; i < messageBits.length/2; i++){
            leftM[i] = messageBits[i];
            rightM[i] = messageBits[i + messageBits.length/2];
        }

        int[] keySchedule = new int[k.length()];

        try{
            //parsing key String
            for(int i = 0; i < keySchedule.length; i++){
                keySchedule[i] = Integer.parseInt(k.substring(i,i+1));
            }
        }
        catch(Exception e){ //bad key scenario
            //return "Please enter the key for the Fiestel cipher as a decimal number \n Key entered:" + k;
            return null;
        }

        boolean[] temp;

        //loops through the steps for encryption
        for(int i = 0; i < keySchedule.length; i++){
            temp = arrayCopy(rightM);
            rightM = stepCalc(leftM, rightM, keySchedule[i]);
            leftM = temp;
        }

        //reconstructs bit array again
        //also swaps left and right as final step
        for(int i = 0; i < messageBits.length/2; i++){
            messageBits[i] = rightM[i];
            messageBits[i + messageBits.length/2] = leftM[i];
        }

        //return BitArrayToString(messageBits);
        return BitArrayToString(messageBits);
    }

    public static String decrypt(String m, String k){
        String inverseKey = "";
        for(int i = 0; i < k.length(); i++){
            inverseKey += k.substring(k.length()-1-i,k.length()-i);
        }
        System.out.println("Inverse Key: " + inverseKey);
        return encrypt(m,inverseKey);
    }

    //calculates the output for the right side
    private static boolean[] stepCalc(boolean[] leftM, boolean[] rightM, int key){

        boolean[] a;

        switch(key){
            case 1:
                a = func1(rightM);
                break;
            case 2:
                a = func2(rightM);
                break;
            case 3:
                a = func3(rightM);
                break;
            case 4:
                a = func4(rightM);
                break;
            case 5:
                a = func5(rightM);
                break;
            case 6:
                a = func6(rightM);
                break;
            case 7:
                a = func7(rightM);
                break;
            case 8:
                a = func8(rightM);
                break;
            case 9:
                a = func9(rightM);
                break;
            case 0:
                a = func0(rightM);
                break;
            default:
                a = arrayCopy(rightM);
        }

        for(int i = 0; i < rightM.length; i++){ //xor with func(rightM) and leftM
            a[i] = leftM[i] ^ a[i];
        }
        return a;
    }

    /*
    A whole bunch of little functions for encryption and decryption
     */

    //just returns a copy of m, a red herring function (and absolutely not me being too lazy to think of anything else)
    private static boolean[] func1(boolean[] m){
        return arrayCopy(m);
    }

    //shifts all bits over by 1
    private static boolean[] func2(boolean[] m){

        boolean temp = m[m.length-1];
        boolean[] a = arrayCopy(m);

        for(int i = m.length-1; i > 0; i--){
            a[i] = a[i-1];
        }

        a[0] = temp;

        return a;
    }

    //swaps every pair of bits (kinda like bubble sort)
    private static boolean[] func3(boolean[] m){

        boolean[] a = arrayCopy(m);
        boolean temp;

        for(int i = 0; i < a.length/2; i++){
            temp = a[2*i];
            a[2*i] = a[2*i + 1];
            a[2*i + 1] = temp;
        }

        return a;
    }

    //reverses array
    private static boolean[] func4(boolean[] m){

        boolean[] a = new boolean[m.length];

        for(int i = 0; i < m.length; i++){
            a[i] = m[m.length - 1 - i];
        }

        return a;
    }

    //xor with a reverse copy of itself
    private static boolean[] func5(boolean[] m){

        boolean[] a = func4(m);

        for(int i = 0; i < m.length; i++){
            a[i] = a[i] ^ m[i];
        }

        return a;
    }

    // and with a bit-shifted copy of itself
    private static boolean[] func6(boolean[] m){
        boolean[] a = func2(m);

        for(int i = 0; i < m.length; i++){
            a[i] = a[i] & m[i];
        }

        return a;
    }

    // or with a copy of m with bits bubble swapped
    private static boolean[] func7(boolean[] m){
        boolean[] a = func3(m);

        for(int i = 0; i < m.length; i++){
            a[i] = a[i] | m[i];
        }

        return a;
    }

    // picks the lowest prime which does not divide the length of m and scale shifts over bits by that amount
    // (i.e. 0 => 0p mod m, 1 => 1p mod p, 2 => 2p mod 2, etc)
    private static boolean[] func8(boolean[] m){

        int prime = 3; //not gonna be less than 3

        while(m.length % prime == 0){
            prime++;
        }

        boolean[] a = arrayCopy(m);

        for(int i = 0; i < m.length; i++){
            int n = (i * prime) % m.length;
            a[n] = m[i];
        }

        return a;
    }

    // shift one copy of m over by one then add them
    private static boolean[] func9(boolean[] m){

        boolean a[] = new boolean[m.length];

        boolean carry = false;
        int j = 1;

        for(int i = 0; i < m.length; i++){

            if(j <= m.length){ //more efficient than calcing mod all the time
                j = 0;
            }

            if(m[i] && m[j] && carry){ //1+1+1
                a[i] = true;
                //carry = true
            }
            else if((m[i] && m[j]) || (m[i] && carry) || (m[j] && carry)){ //1+1+0
                a[i] = false;
                carry = true;
            }
            else if(!m[i] && !m[j] && !carry){ //0+0+0
                a[i] = false;
                //carry = false
            }
            else{ //1+0+0, annoying to write out the if statement for it so I stuck it here
                a[i] = true;
                carry = false;
            }

            j++;
        }

        return func2(m);
    }

    //just flips the bits
    //yeah I've really run out of ideas at this point that aren't annoyingly complicated
    private static boolean[] func0(boolean[] m){
        boolean[] a = arrayCopy(m);

        for(boolean b : a){
            if(b){
                b = false;
            }
            else{
                b = true;
            }
        }

        return m;
    }

    /*
    End of little functions
     */

    /*
    converts a string into a boolean array containing all the chars as ascii binary
     */
    private static boolean[] StringToBitArray(String m){

        //m = m.substring(0,m.length()-1); //removing new line char

        int arrayIndex = 0; //index for counting where in the bit array we are
        boolean[] StringBits = new boolean[m.length() * 8]; //all the bits
        for(char c : m.toCharArray()){

            int temp = c;
            //loops through the bits of an individual character, extracts then and puts them into the boolean array

            for(int i = 7; i >= 0; i--){ //count from 7 to 0
                //this operation takes the next bit of the character
                temp = (c >> i) & 0x1;
                if(temp == 0){
                    StringBits[arrayIndex] = false;
                }
                else if(temp == 1){
                    StringBits[arrayIndex] = true;
                }
                else{
                    System.out.println("something has gone horribly wrong with the bit conversion");
                }
                arrayIndex++;
            }
        }

        return StringBits;
    }

    /*
    converts a boolean array of ascii chars back into a string
     */
    private static String BitArrayToString(boolean[] m){

        if((m.length / 8) * 8 != m.length){
            System.out.println("boolean array 'm' cannot be converted into a String (bits != multiple of 8)");
            return null;
        }

        int c = 0; //the char to add to the message string
        String message = "";
        int charBitCount = 0; //cycles from 0 to 7 to count the bit position in the new char

        for(int i = 0; i < m.length; i++){
            if(m[i]){ // == 1
                c += 1;
            }

            if(charBitCount >= 7){
                charBitCount = 0;
                message += (char)c;
                c = 0;
                //sum++;
            }
            else{
                c = c * 2;
                charBitCount++;
            }
        }

        return message;
    }

    //copies contents of a into b
    //really I should make this generic type for general use but I can't be bothered rn
    private static boolean[] arrayCopy(boolean[] a){
        boolean[] b = new boolean[a.length];
        for(int i = 0; i < a.length; i++){
            b[i] = a[i];
        }
        return b;
    }

    //generates a random key 20 digits long
    public static String genRandomKey(){

        String key = "" + (int)(Math.random() * 10);

        for(int i = 0; i < 10; i++){
            key += (int)(Math.random() * 10);
        }

        return "" + key;
    }

    /*
    Really basic brute forcing method
    As far as I can tell there really isn't way to be able to find the key any other way
    This method is here mostly for completeness and very small keys/text
     */
    public static String smartDecrypt(String m){

        int count = 0;
        String key = "";
        String out = "";

        while(count < 100){

            key = "" + count;
            out = decrypt(m,key);

            //simple check of first 50 letters to see if they fit standard text characters. If not break loop early and continue
            for(int i = 0; i < 50; i++){
                if(!(out.charAt(i) >= 'A' && out.charAt(i) <= 'Z')){ //check if it's a capital letter
                    if(!(out.charAt(i) >= 'a' && out.charAt(i) <= 'z')){ //check if it's a lowercase letter
                        if(out.charAt(i) == ' ' || out.charAt(i) == '?' || out.charAt(i) == '!' || out.charAt(i) == '.'){
                            //do nothing and continue on
                        }
                        else{
                            break; //not a char
                        }
                    }
                }

                if(i == 49){ //if it got to here then it's text, YAY!
                    return out;
                }
            }

            count++;
        }

        return "Could not find the key in a reasonable amount of time.";
    }

    //used for de-bugging
    //prints out each char in the boolean array as a sequence of 8 bits
    private static void printBits(boolean[] a){
        for(int i = 0; i < a.length; i++){
            if(i % 8 == 0){
                System.out.println("");
            }
            if(a[i]){
                System.out.print("1");
            }
            else{
                System.out.print("0");
            }
        }
    }

    //gens a random key and feeds it into
    public static String encrypt(String m){
        String key = "" + genRandomKey();
        System.out.println("Key: " + key);
        return encrypt(m,key);
    }

    //takes String s and writes to a text file
    public static void writeToFile(String s){
        try {
            FileWriter myWriter = new FileWriter("decrypt.txt");
            myWriter.write(s);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

    public static byte[] encrypt(byte[] bytes, String k){
        String s = new String(bytes);
        String m = encrypt(s,k);
        return m.getBytes();
    }

    public static byte[] decrypt(byte[] bytes, String k){
        String s = new String(bytes);
        String m = decrypt(s,k);
        return m.getBytes();
    }

}
