package com.everhomes.parking.chean;

import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Map;

import static java.util.Arrays.sort;

public class EncryptUtil {

    public static String encode(String content, String seed) throws Exception {
        String macKey = seed;
        String macData = content;
        Mac mac = Mac.getInstance("HMACSHA1");
        byte[] secretByte = macKey.getBytes("UTF-8");
        byte[] dataBytes = macData.getBytes("UTF-8");
        SecretKey secret = new SecretKeySpec(secretByte, "HMACSHA1");
        mac.init(secret);
        byte[] doFinal = mac.doFinal(dataBytes);
        String checksum = Base64.encode(doFinal);
        System.err.println("ENCODE:" + checksum);
        return checksum;
    }

    public static String signing(Map<String, Object> map) {
        String[] array = map.values().toArray(new String[0]);
        sort(array);
        StringBuilder builder = new StringBuilder();
        for (String string : array) {
            builder.append(string);
        }
        return builder.toString();
    }
}
