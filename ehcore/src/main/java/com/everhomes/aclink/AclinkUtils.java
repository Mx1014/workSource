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

import com.everhomes.rest.aclink.DataUtil;
import com.everhomes.rest.aclink.DoorAccessType;
import com.everhomes.rest.aclink.DoorMessage;
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
        
        //TODO now use static server key for test
        //return "s87SHk+R/IOw6dV7QkX/pA==";
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
    
    //DoorMessage is just for debug!!!, and should be removed later.
    public static String packInitServerKey(String rsaAclinkPub, String aesKey, String aesIv, String devName, Long time, String uuid, DoorMessage doorMessage) {
        String pub = StringHelper.toHexString(Base64.decodeBase64(rsaAclinkPub));
        byte[] result = CmdUtil.initServerKeyCmd((byte)0, pub, (devName+"\0\0\0\0\0\0").substring(0, 6), (int)(time.longValue()/1000), uuid.getBytes(), Base64.decodeBase64(aesKey), Base64.decodeBase64(aesIv), doorMessage);
        return Base64.encodeBase64String(result);
    }
    
    public static String packUpdateDeviceName(Byte ver, String aesKey, String aesIv, String devName) {
        byte[] key = Base64.decodeBase64(aesKey);
        byte[] binaryData = CmdUtil.updateDevName(key, ver.byteValue(), (devName+"\0\0\0\0\0\0\0\0\0\0\0\0").substring(0, 12));
        return Base64.encodeBase64String(binaryData);
    }
    
    public static String packAesUserKey(String aesServerKey, Long userId, Integer keyId, Long expireTime) {
        try {
            byte[] serverKey = Base64.decodeBase64(aesServerKey);
            SecretKeySpec skeySpec = new SecretKeySpec(serverKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");

            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] data = new byte[16];
            
            int curTime = (int) Math.ceil((expireTime.longValue() / 1000));
            byte[] curTimeBytes = DataUtil.intToByteArray(curTime);
            byte[] uidBytes = DataUtil.intToByteArray(userId.intValue());
            byte[] uidPadding = {7, 9, 8};
            System.arraycopy(curTimeBytes, 0, data, 0, curTimeBytes.length);
            System.arraycopy(uidBytes, 0, data, 4, uidBytes.length);
            System.arraycopy(uidPadding, 0, data, 8, uidPadding.length);
            data[11] = 0x3a;
            byte[] keyIdBytes = DataUtil.shortToByteArray(keyId.shortValue());
            System.arraycopy(keyIdBytes, 0, data, 12, keyIdBytes.length);
            byte[] checkSum = DataUtil.shortToByteArray(CmdUtil.getCheckSum(data, data.length));
            System.arraycopy(checkSum, 0, data, 14, checkSum.length);
            
            return Base64.encodeBase64String(cipher.doFinal(data));   
        } catch(Exception ex) {
            //TODO log here
            return "";
        }
    }
    
    public static String packAddUndoList(Byte ver, String aesKey, int availableTime, short keyId) {
        byte[] key = Base64.decodeBase64(aesKey);
        byte[] binaryData = CmdUtil.addUndoListCmd(key, ver, availableTime, keyId);
        return Base64.encodeBase64String(binaryData);
    }
    
    public static String packUpgrade(Byte ver, String aesKey, int firmVersion, short checksum, String uuid) {
        byte[] key = Base64.decodeBase64(aesKey);
        byte[] binaryData = CmdUtil.upgrade(key, ver, firmVersion, checksum, uuid);
        return Base64.encodeBase64String(binaryData);
    }
    
    public static String packWifiCmd(Byte ver, String aesKey, String ssid, String pwd, String borderUrl) {
        byte[] key = Base64.decodeBase64(aesKey);
        byte[] binaryData = CmdUtil.wifiCmd(key, ver, ssid, pwd, borderUrl);
        return Base64.encodeBase64String(binaryData);
    }
    
    public static boolean isZuolinDevice(DoorAccess doorAccess) {
        DoorAccessType dt = DoorAccessType.fromCode(doorAccess.getDoorType());
        if(dt == DoorAccessType.ACLINK_ZL_GROUP || dt == DoorAccessType.ZLACLINK_WIFI || dt == DoorAccessType.ZLACLINK_NOWIFI || dt == DoorAccessType.ACLINK_BUS || dt == DoorAccessType.ZLACLINK_WIFI_2) {
            return true;
        }
        
        return false;
    }
    
    public static String packSetServerKeyCmd(Byte ver, String oldKey, String newKey) {
    	byte[] key0 = Base64.decodeBase64(oldKey);
    	byte[] key1 = Base64.decodeBase64(newKey);
    	return Base64.encodeBase64String(CmdUtil.setServerKeyCmd(ver, key0, key1));
    }
    
    private static short getCheckSum(byte[] data1, byte[] data2) {
        short sum = 0;
        for (int i = 0; i < data1.length; i++) {
            sum += (data1[i] & 0xFF);
            }
        for (int i = 0; i < data2.length; i++) {
            sum += (data2[i] & 0xFF);
            }
        return sum;
    }
    
    public static String createZlQrCodeForFlapDoor(byte[] qrArr, long curTime, long qrImagePeriod) {
        String resultCode = null;
        byte[] type = new byte[]{0, 1};
        byte[] cmdArr = new byte[]{9, 0};
        byte[] currTimeArr = DataUtil.intToByteArray((int) (curTime / 1000));
        int imageTimeout = (int) ((curTime + qrImagePeriod) / 1000);
        byte[] imageTimeArr = DataUtil.intToByteArray(imageTimeout);

        short checkSum = getCheckSum(currTimeArr, imageTimeArr);
        byte[] checkSumArr = DataUtil.shortToByteArray(checkSum);
        byte[] randomArr = {0, 0, 0, 0, 0, 0, 0, 0};
        if (null != checkSumArr && checkSumArr.length == 2) {
            randomArr[0] = checkSumArr[0];
            randomArr[1] = checkSumArr[1];
        }

        byte[] encryptArr = new byte[currTimeArr.length + imageTimeArr.length + randomArr.length];
        System.arraycopy(currTimeArr, 0, encryptArr, 0, currTimeArr.length);
        System.arraycopy(imageTimeArr, 0, encryptArr, currTimeArr.length, imageTimeArr.length);
        System.arraycopy(randomArr, 0, encryptArr, currTimeArr.length + imageTimeArr.length, randomArr.length);

        encryptArr = xorResult(encryptArr);

        byte[] lengthArr = DataUtil.shortToByteArray((short) (cmdArr.length + qrArr.length + encryptArr.length));
        byte[] resultArr = new byte[type.length + lengthArr.length + cmdArr.length + qrArr.length + encryptArr.length];
        System.arraycopy(type, 0, resultArr, 0, type.length);
        System.arraycopy(lengthArr, 0, resultArr, type.length, lengthArr.length);
        System.arraycopy(cmdArr, 0, resultArr, type.length + lengthArr.length, cmdArr.length);
        System.arraycopy(qrArr, 0, resultArr, cmdArr.length + type.length + lengthArr.length, qrArr.length);
        System.arraycopy(encryptArr, 0, resultArr, cmdArr.length + type.length + lengthArr.length + qrArr.length, encryptArr.length);
        resultCode = Base64.encodeBase64String(resultArr);
        return resultCode;
    }

    private static byte[] xorResult(byte[] srcData) {
        String key = "thisjustfortime!";//TODO use constants
        byte[] keyData = key.getBytes();

        if (null == srcData || null == keyData || srcData.length < 16 || keyData.length < 16) {
            return srcData;
        }
        byte[] resData = new byte[srcData.length];
        for (int i = 0; i < srcData.length; i++) {
            resData[i] = (byte) (srcData[i] ^ keyData[i]);
        }
        return resData;
    }

}
