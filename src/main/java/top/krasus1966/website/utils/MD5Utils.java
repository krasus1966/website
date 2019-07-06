package top.krasus1966.website.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Krasus1966
 * @date 2020/3/5 15:52
 **/
public class MD5Utils {
    public static String code(String str){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[]byteDigest = md.digest();
            int i;
            StringBuffer buffer = new StringBuffer("");
            for(int offset = 0;offset<byteDigest.length;offset++){
                i = byteDigest[offset];
                if(i<0){
                    i+=256;
                }
                if(i<16){
                    buffer.append("0");
                }
                buffer.append(Integer.toHexString(i));
            }
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(code("111"));
    }
}
