// @formatter:off
package com.everhomes.parking.elivejieshun;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/4/11 16:27
 */
public class EliveJieShunSignTools {
    public static String getSn(String jsonString){
        MessageDigest md5Tool = null;
        try {
            md5Tool = MessageDigest.getInstance("MD5");
            byte[] md5Data = md5Tool.digest(jsonString.getBytes("UTF-8"));
            String sn = toHexString(md5Data);
            return sn;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toHexString(byte[] bytes) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            buffer.append(String.format("%02X", bytes[i]));
        }
        return buffer.toString();
    }
}
