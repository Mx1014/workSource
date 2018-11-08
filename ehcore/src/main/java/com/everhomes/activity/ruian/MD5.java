package com.everhomes.activity.ruian;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

    private static String MD5(String sourceStr,int type) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            if(type == 32) {
                result = buf.toString();
            }else if(type == 16){
                result = buf.toString().substring(8, 24);
            }
            //Common.println("MD5:"+result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String md5EncryptTo32(String sourceStr){
        return MD5(sourceStr,32).toUpperCase();
    }

    public static String md5EncryptTo16(String sourceStr){
        return MD5(sourceStr,16).toUpperCase();
    }
}
