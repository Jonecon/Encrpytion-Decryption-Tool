public class Fiestel {

    //key is in the form of a hex number, each digit corresponding to one of 16 functions
    //recommended key length is 16+ digits

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

        //just assume string entered is a hex num for now and create a check later

        //get key schedule from master key
        for(int i = 0; i < keySchedule.length; i++){
            keySchedule[i] = Integer.parseInt(k.substring(i,i+1));
        }

        boolean[] temp;

        for(int i = 0; i < keySchedule.length; i++){
            temp = arrayCopy(rightM);
            rightM = stepCalc(leftM, rightM, keySchedule[i]); //this line here is causing the trouble
            leftM = temp;
        }

        for(int i = 0; i < messageBits.length/2; i++){
            messageBits[i] = rightM[i];
            messageBits[i + messageBits.length/2] = leftM[i];
        }

        return BitArrayToString(messageBits);
    }

    //calculates the output for the right side
    private static boolean[] stepCalc(boolean[] leftM, boolean[] rightM, int key){

        boolean[] a = func1(rightM);

        System.out.println("");

        for(int i = 0; i < rightM.length; i++){ //xor with func(rightM) and leftM
            a[i] = leftM[i] ^ a[i];

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
        return a;
    }

    //just returns a copy of m at the moment
    private static boolean[] func1(boolean[] m){
        return arrayCopy(m);
    }


    /*
    IMPORTANT NOTE: removes new line from end of string
     */
    private static boolean[] StringToBitArray(String m){

        m = m.substring(0,m.length()-1);

        System.out.println(m);
        System.out.println(m.length());

        int arrayIndex = 0; //index for counting where in the bit array we are
        boolean[] StringBits = new boolean[m.length() * 8]; //all the bits
        for(char c : m.toCharArray()){

            //System.out.println(c);

            int temp = c;
            //loops through the bits of an individual character, extracts then and puts them in into the boolean array

            for(int i = 7; i >= 0; i--){ //count from 7 to 0
                //this operation takes the next bit of the character
                temp = (c >> i) & 0x1;
                if(temp == 0){
                    StringBits[arrayIndex] = false;
                    //System.out.println("0");
                }
                else if(temp == 1){
                    StringBits[arrayIndex] = true;
                    //System.out.println("1");
                }
                else{
                    System.out.println("something has gone horribly wrong with the bit conversion");
                }
                arrayIndex++;
            }
        }

        //testing
        for(int i = 0; i < StringBits.length; i++){
            if(i % 8 == 0){
                System.out.println("");
            }
            if(StringBits[i]){
                System.out.print("1");
            }
            else{
                System.out.print("0");
            }
        }

        return StringBits;
    }

    private static String BitArrayToString(boolean[] m){

        if((m.length / 8) * 8 != m.length){
            System.out.println("boolean array 'm' cannot be converted into a String (bits != multiple of 8)");
            return null;
        }

        System.out.println((m.length / 8) * 8 != m.length);

        int c = 0; //the char to add to the message string
        String message = "";
        int charBitCount = 0; //cycles from 0 to 7 to count the bit position in the new char

        int sum = 0;

        System.out.println(m.length);

        for(int i = 0; i < m.length; i++){
            if(m[i]){ // == 1
                c += 1;
            }

            if(charBitCount >= 7){
                charBitCount = 0;
                message += (char)c;
                c = 0;
                sum++;
            }
            else{
                c = c * 2;
                charBitCount++;
            }
        }

        System.out.println(sum);

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

    //basically an exact copy of encrypt with some minor changes
    //honestly this cipher is kind of nuts how encryption/decryption work together
    public static String decrypt(String m, String k){
        //convert message into bits
        boolean[] messageBits = StringToBitArray(m);

        //convert to left and right arrays
        //could do a slightly more efficient thing here but it's not really super relevant atm
        boolean[] leftM = new boolean[messageBits.length/2];
        boolean[] rightM = new boolean[messageBits.length/2];

        for(int i = 0; i < messageBits.length/2; i++){
            rightM[i] = messageBits[i];
            leftM[i] = messageBits[i + messageBits.length/2];
        }

        int[] keySchedule = new int[k.length()];

        //just assume string entered is a hex num for now and create a check later

        //get key schedule from master key
        for(int i = 0; i < keySchedule.length; i++){
            keySchedule[i] = Integer.parseInt(k.substring(i,i+1));
        }

        //System.out.println(keySchedule[3]);

        boolean[] temp;

        for(int i = 0; i < keySchedule.length; i++){
            temp = arrayCopy(leftM);
            leftM = stepCalc(rightM, leftM, keySchedule[keySchedule.length-1-i]);
            rightM = temp;
        }

        for(int i = 0; i < messageBits.length/2; i++){
            messageBits[i] = leftM[i];
            messageBits[i + messageBits.length/2] = rightM[i];
        }

        return BitArrayToString(messageBits);
    }

    private void writeToFile(){

    }
}
