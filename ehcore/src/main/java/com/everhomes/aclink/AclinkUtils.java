package com.everhomes.aclink;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AclinkUtils {
    public static byte AES_USER_KEY_TYPE = 0x3a;
    
    public static String packAesUserKey(String aesServerKey, Long userId, Byte keyId, Long expireTime) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(aesServerKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");

            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            String data = "aa";

            byte[] original = Base64.encodeBase64(cipher.doFinal(data.getBytes()));
            return new String(original);    
        } catch(Exception ex) {
            //TODO log here
            return "";
        }
    }
}
