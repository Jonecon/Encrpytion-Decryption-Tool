public class Fiestel {

    //key is in the form of a decimal number each digit representing a function (0-9)
    //recommended key length is 20+ digits

    /*
    Pretty self explanatory
     */
    public static String encrypt(String m, String k){

        //convert message into bits
        boolean[] messageBits = StringToBitArray(m);

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
            return "Please enter the key for the Fiestel cipher as a decimal number \n Key entered:" + k;
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

        return BitArrayToString(messageBits);
    }

    public static String decrypt(String m, String k){
        String inverseKey = "";
        for(int i = 0; i < k.length(); i++){
            inverseKey += k.substring(k.length()-1-i,k.length()-i);
        }
        System.out.println(inverseKey);
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

    //reveres array
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

    //for now just calls func1
    private static boolean[] func8(boolean[] m){
        return func1(m);
    }

    //for now just calls func2
    private static boolean[] func9(boolean[] m){
        return func2(m);
    }

    //for now just calls func3
    private static boolean[] func0(boolean[] m){
        return func3(m);
    }

    /*
    End of little functions
     */

    /*
    converts a string into a boolean array containing all the chars as ascii binary
    IMPORTANT NOTE: removes new line from end of string
     */
    private static boolean[] StringToBitArray(String m){

        m = m.substring(0,m.length()-1); //removing new line char

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
    public int genRandomKey(){

        int key = (int)(Math.random() * 10);

        for(int i = 0; i < 20; i++){
            key *= 10;
            key += (int)(Math.random() * 10);
        }

        return key;
    }

    /*
    This method attempts to figure out the key for a given message string 'm'
    I haven't got a clue where to start on this
     */
    public static String smartDecrypt(String m, String k){
        return "";
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

}
