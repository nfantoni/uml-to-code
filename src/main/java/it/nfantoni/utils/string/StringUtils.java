package it.nfantoni.utils.string;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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

    public static String toString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        byte[] byteArray = buffer.toByteArray();

        return new String(byteArray, StandardCharsets.UTF_8);
    }

}
