package ir.ac.aut.ceit.ap.fileserver.util;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    public static String md5Hash(byte[] bytes) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return DatatypeConverter.printHexBinary(digest.digest()).toUpperCase();
    }
}
