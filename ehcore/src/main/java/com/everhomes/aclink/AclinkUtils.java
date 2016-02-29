package com.everhomes.aclink;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.everhomes.util.StringHelper;

public class AclinkUtils {
    public static byte AES_USER_KEY_TYPE = 0x3a;
    
    public static String generateAESKey() {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            SecretKey key = generator.generateKey();
            return Base64.encodeBase64String(key.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException for AES", e);
        }
    }
    
    public static String generateAESIV(String base64Key) {
        byte[] key = Base64.decodeBase64(base64Key);
        IvParameterSpec iv = new IvParameterSpec(key);
        return Base64.encodeBase64String(iv.getIV());
    }
    
    public static byte[] encryptByRawPublicKey(byte[] data, String base64Key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {
        //http://stackoverflow.com/questions/32435842/rsa-key-size-less-than-512-bits-in-java
        //http://www.mysamplecode.com/2011/08/java-rsa-encrypt-string-using-bouncy.html
        
        String publicKey = StringHelper.toHexString(Base64.decodeBase64(base64Key));
        
        BigInteger modulus = new BigInteger(publicKey, 16);
        BigInteger pubExp = new BigInteger("010001", 16);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(modulus, pubExp);
        RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);

        Cipher cipher = Cipher.getInstance("RSA/None/NoPadding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        int keyLen = publicKey.length()/2;
        if(data.length < (keyLen-1) ) {
            byte[] newData = new byte[keyLen-data.length];
            newData[newData.length-1] = (byte)data.length;
            cipher.update(newData);
        }
        cipher.update(data);
        
        byte[] cipherData = cipher.doFinal();
        return cipherData;
    }
    
    public static String packInitServerKey(String rsaAclinkPub, String aesKey, String aesIv, String devName, Long time, String uuid) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream d = new DataOutputStream(b);
//        d.writeShort(19);
        byte[] result = b.toByteArray();
        return Base64.encodeBase64String(result);
    }
    
    public static String packAesUserKey(String aesServerKey, Long userId, Integer keyId, Long expireTime) {
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
