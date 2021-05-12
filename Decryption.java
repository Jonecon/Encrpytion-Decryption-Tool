public class Decryption {
    public static void main(String[] args) {
        String input = Tools.readStdIn();
        double coincidence = Tools.indexOfCoincidence(input);
        if (coincidence > 0.062) {
            System.out.println(Caesar.decryptWithoutKey(input));
        } else {
            System.out.println("polyalphabetic cypher");
        }
    }
}