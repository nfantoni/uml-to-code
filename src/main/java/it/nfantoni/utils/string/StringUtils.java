package it.nfantoni.utils.string;

public class StringUtils {

    private StringUtils() {
    }

    public static String decapitalize(String string){

        if (string != null && string.length() != 0) {
            char[] c = string.toCharArray();
            c[0] = Character.toLowerCase(c[0]);
            return new String(c);
        } else return string;
    }

    public static String capitalize(String string){
        if (string != null && string.length() != 0) {
            char[] c = string.toCharArray();
            c[0] = Character.toUpperCase(c[0]);
            return new String(c);
        } else return string;
    }

}
