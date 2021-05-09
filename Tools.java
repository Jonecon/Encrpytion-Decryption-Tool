


public class Tools {



    // removes spaces from a string
    public static String removeSpaces(String message) {
        return message.replaceAll("\\s", "");
    }

    // removes all non letter characters and removes capitals
    public static String simplifyMessage(String message) {
        String[] stringArray = message.split("\\W+");
        String result = "";
        for(String s: stringArray) {
            result += s;
        }
        return result.toLowerCase();
    }

}