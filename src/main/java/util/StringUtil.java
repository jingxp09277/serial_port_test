package util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.text.NumberFormat;
import java.util.regex.Pattern;

public class StringUtil {
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }


    public static byte[] hexStringsToByte(String hexs) {
        hexs = hexs.toUpperCase();
        int len =0;
        len = (hexs.length() / 2);
        if(hexs.length()%2!=0){
            len+=1;
            hexs = "0"+hexs;
        }


        byte[] result = new byte[len];
        char[] achar = hexs.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

}
