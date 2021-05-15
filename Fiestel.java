public class Fiestel {

    //key is in the form of a hex number, each digit corresponding to one of 16 functions
    //recommended key length is 16+ digits

    public static String encrypt(String m, String k){

        System.out.println("test1");

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

        System.out.println("test2");

        int[] keySchedule = new int[k.length()];

        //just assume string entered is a hex num for now and create a check later

        //get key schedule from master key
        for(int i = 0; i < keySchedule.length; i++){
            keySchedule[i] = Integer.parseInt(k.substring(i,i+1));
        }

        System.out.println("test3");

        boolean[] temp;

        for(int i = 0; i < keySchedule.length; i++){
            temp = arrayCopy(rightM);
            rightM = stepCalc(leftM, rightM, keySchedule[i]);
            leftM = temp;
        }

        System.out.println("test4");

        for(int i = 0; i < messageBits.length/2; i++){
            messageBits[i] = rightM[i];
            messageBits[i + messageBits.length/2] = leftM[i];
        }

        return BitArrayToString(messageBits);
    }

    //calculates the output for the right side
    private static boolean[] stepCalc(boolean[] leftM, boolean[] rightM, int key){

        boolean[] a = func1(rightM);
        for(int i = 0; i < rightM.length; i++){
            a[i] = leftM[i] ^ a[i];
        }
        return a;
    }

    //just returns a copy of m at the moment
    private static boolean[] func1(boolean[] m){
        return arrayCopy(m);
    }

    private static boolean[] StringToBitArray(String m){

        int bitLength = m.length() * 8; //num of bits in message
        int arrayIndex = 0; //index for counting where in the bit array we are
        boolean[] StringBits = new boolean[bitLength]; //all the bits
        for(char c : m.toCharArray()){
            int temp = c;
            //loops through the bits of an individual character, extracts then and puts them in into the boolean array
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

            if(charBitCount >= 8){
                charBitCount = 0;
                message += (char)c;
                c = 0;
            }
            else{
                c = c << 1;
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

    //does nothing at the moment
    private static String decrypt(){
        return "";
    }
}
