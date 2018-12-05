// @formatter:off
package com.everhomes.aclink;

import java.util.Arrays;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.rest.aclink.DataUtil;
import com.everhomes.rest.aclink.DoorMessage;
import com.everhomes.util.StringHelper;

public class CmdUtil {
    public final static int CMD_ACTIVE = 0x01;

    private final static int PACKAGE_MAX_LENGTH = 20;
    private final static int SINGLE_PACKAGE_DATA_LENGTH = 18;
    private final static int MULTI_PACKAGE_HEAD_DATA_LENGTH = 16;
    private final static int MULTI_PACKAGE_OTHER_DATA_LENGTH = 19;
    private final static int EXPIRE_TIME = (2*30*24*3600);//2 months
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CmdUtil.class);

    /**
     * 获取激活指令
     *
     * @return 返回激活指令的数据
     */

    public static byte[] activeCmd() {
        byte cmd = 0x1;
        byte ver = 0x0;
        int uid = 5;
        int time = (int) System.currentTimeMillis();
        byte[] uidBytes = {(byte) 0xff, (byte) 0xee, (byte) 0xbb, (byte) 0xcc};

        byte[] timeBytes = DataUtil.intToByteArray(time);
        byte[] dataArray = new byte[uidBytes.length + timeBytes.length];
        for (int i = 0; i < uidBytes.length; i++) {
            dataArray[i] = uidBytes[i];
        }
        for (int i = 0; i < timeBytes.length; i++) {
            dataArray[uidBytes.length + i] = timeBytes[i];
        }
        dataArray = addPaddingTo16Bytes(dataArray);
        byte[] resultArray = new byte[2 + dataArray.length];
        for (int i = 0; i < resultArray.length; i++) {
            if (i == 0) {
                resultArray[0] = cmd;
            } else if (i == 1) {
                resultArray[1] = ver;
            } else {
                resultArray[i] = dataArray[i - 2];
            }
        }
        return resultArray;
    }

    public static byte[] initServerKeyCmd(byte ver, String pubKey, String devName, int time, byte[] uuidBytes, byte[] serverKeyBytes, byte[] aesIvBytes, DoorMessage doorMessage) {
        /**
         * serverKey:16B
         * devName:6B
         * time:4B
         * aesIv:16B
         * uuid:32B
         */
        byte cmd = 0x2;

        byte[] devNameBytes = devName.getBytes();
        byte[] timeBytes = DataUtil.intToByteArray(time);

        byte[] resultArray = new byte[2 + serverKeyBytes.length + devNameBytes.length + timeBytes.length + aesIvBytes.length + uuidBytes.length];


        //没有加cmd和ver的数据
        for (int i = 0; i < serverKeyBytes.length; i++) {
            resultArray[i] = serverKeyBytes[i];
        }
        int startPosition = serverKeyBytes.length;
        for (int i = 0; i < devNameBytes.length; i++) {
            resultArray[startPosition + i] = devNameBytes[i];
        }
        startPosition = startPosition + devNameBytes.length;
        for (int i = 0; i < timeBytes.length; i++) {
            resultArray[startPosition + i] = timeBytes[i];
        }
        startPosition = startPosition + timeBytes.length;
        for (int i = 0; i < aesIvBytes.length; i++) {
            resultArray[startPosition + i] = aesIvBytes[i];
        }
        startPosition = startPosition + aesIvBytes.length;
        for (int i = 0; i < uuidBytes.length; i++) {
            resultArray[startPosition + i] = uuidBytes[i];
        }
        try {
            //Just for debug info
            doorMessage.setExtra(Base64.encodeBase64String(resultArray));
            
            byte[] encryptData = RSAUtil.encryptByRawPublicKey(resultArray, pubKey);
            byte[] headArr = {cmd, ver};
            resultArray = DataUtil.mergeArray(headArr, encryptData);
            
            LOGGER.info(StringHelper.toHexString(resultArray));

            return resultArray;
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] setServerKeyCmd() {
        byte cmd = 0x3;
        byte ver = 0x0;

        String aesKey = "939C9803F49A585BA4B313FCA74C6B26";

        byte[] aesBytes = {15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0};

        byte[] resultArray = new byte[2 + aesBytes.length];
        resultArray[0] = cmd;
        resultArray[1] = ver;
        for (int i = 0; i < resultArray.length; i++) {
            resultArray[i + 2] = resultArray[i];
        }
        return resultArray;

    }

    public static byte[] updateDevName(byte[] curServerKey, byte ver, String newDevName) {
        byte cmd = 0x4;
        int expireTime = (int) Math.ceil((System.currentTimeMillis() / 1000)) + EXPIRE_TIME;
        byte[] timeBytes = DataUtil.intToByteArray(expireTime);
        byte[] newNameBytes = new byte[12];
        if(newNameBytes.length >= 12) {
            System.arraycopy(newDevName.getBytes(), 0, newNameBytes, 0, 12);
        } else {
            byte[] b = newDevName.getBytes();
            System.arraycopy(b, 0, newNameBytes, 0, b.length);
            newNameBytes[b.length] = '\0';
        }
        byte[] dataArr = new byte[timeBytes.length + newNameBytes.length];
        System.arraycopy(timeBytes, 0, dataArr, 0, timeBytes.length);
        System.arraycopy(newNameBytes, 0, dataArr, timeBytes.length, newNameBytes.length);
        dataArr = addPaddingTo16Bytes(dataArr);
        try {
            byte[] aeskeyEncryptResult = AESUtil.encrypt(dataArr, curServerKey);
            byte[] resultArr = new byte[2 + aeskeyEncryptResult.length];
            resultArr[0] = cmd;
            resultArr[1] = ver;
            System.arraycopy(aeskeyEncryptResult, 0, resultArr, 2, aeskeyEncryptResult.length);
            return resultArr;
        } catch (Exception e) {
        }
        return null;
    }

    public static byte[] updateTime(byte[] curServerKey, byte ver) {
        byte cmd = 0x5;
        int curTime = (int) Math.ceil((System.currentTimeMillis() / 1000));
        int expireTime = curTime + 5*6*EXPIRE_TIME;//5 years 
        byte[] extTimeBytes = DataUtil.intToByteArray(expireTime);
        byte[] curTimeBytes = DataUtil.intToByteArray(curTime);
        byte[] dataArr = new byte[extTimeBytes.length + curTimeBytes.length];
        System.arraycopy(extTimeBytes, 0, dataArr, 0, extTimeBytes.length);
        System.arraycopy(curTimeBytes, 0, dataArr, extTimeBytes.length, curTimeBytes.length);
        try {
            dataArr = addPaddingTo16Bytes(dataArr);
            byte[] chk = DataUtil.shortToByteArray(CmdUtil.getCheckSum(dataArr, 14));
            dataArr[14] = chk[0];
            dataArr[15] = chk[1];
            
            LOGGER.info("updateTime dataArr = " + StringHelper.toHexString(dataArr));
            
            byte[] aeskeyEncryptResult = AESUtil.encrypt(dataArr, curServerKey);
            byte[] resultArr = new byte[2 + aeskeyEncryptResult.length];
            resultArr[0] = cmd;
            resultArr[1] = ver;
            System.arraycopy(aeskeyEncryptResult, 0, resultArr, 2, aeskeyEncryptResult.length);
            return resultArr;
//            return aeskeyEncryptResult;
        } catch (Exception e) {
        }
        return null;
    }
    
    public static byte[] updateTime(byte[] curServerKey, byte ver, int curTime) {
        byte cmd = 0x5;
//        int curTime = (int) Math.ceil((System.currentTimeMillis() / 1000));
        int expireTime = curTime + 5*6*EXPIRE_TIME;//5 years
        byte[] extTimeBytes = DataUtil.intToByteArray(expireTime);
        byte[] curTimeBytes = DataUtil.intToByteArray(curTime);
        byte[] dataArr = new byte[extTimeBytes.length + curTimeBytes.length];
        System.arraycopy(extTimeBytes, 0, dataArr, 0, extTimeBytes.length);
        System.arraycopy(curTimeBytes, 0, dataArr, extTimeBytes.length, curTimeBytes.length);
        try {
            dataArr = addPaddingTo16Bytes(dataArr);
            byte[] chk = DataUtil.shortToByteArray(CmdUtil.getCheckSum(dataArr, 14));
            dataArr[14] = chk[0];
            dataArr[15] = chk[1];
            
            LOGGER.info("updateTime dataArr = " + StringHelper.toHexString(dataArr));
            
            byte[] aeskeyEncryptResult = AESUtil.encrypt(dataArr, curServerKey);
            byte[] resultArr = new byte[2 + aeskeyEncryptResult.length];
            resultArr[0] = cmd;
            resultArr[1] = ver;
            System.arraycopy(aeskeyEncryptResult, 0, resultArr, 2, aeskeyEncryptResult.length);
            return resultArr;
//            return aeskeyEncryptResult;
        } catch (Exception e) {
        }
        return null;
    }

    public static byte[] addUndoListCmd(byte[] curServerKey, byte ver, int availableTime, short id) {
        
        //id = 0xa;
        
        byte cmd = 0x6;
        int expireTime = (int) Math.ceil((System.currentTimeMillis() / 1000)) + EXPIRE_TIME;
        byte[] extTimeBytes = DataUtil.intToByteArray(expireTime);
        byte[] availableTimeBytes = DataUtil.intToByteArray(availableTime);
        byte[] keyId = DataUtil.shortToByteArray(id);
        //byte[] keyId = {0xa, 0xa};
        byte[] dataArr = new byte[extTimeBytes.length + keyId.length + availableTimeBytes.length];

        System.arraycopy(extTimeBytes, 0, dataArr, 0, extTimeBytes.length);
        System.arraycopy(keyId, 0, dataArr, extTimeBytes.length, keyId.length);
        System.arraycopy(availableTimeBytes, 0, dataArr, extTimeBytes.length + keyId.length, availableTimeBytes.length);
        dataArr = addPaddingTo16Bytes(dataArr);

        printArray(dataArr);
        
        try {
            byte[] aeskeyEncryptResult = AESUtil.encrypt(dataArr, curServerKey);
            byte[] resultArr = new byte[2 + aeskeyEncryptResult.length];
            resultArr[0] = cmd;
            resultArr[1] = ver;
            System.arraycopy(aeskeyEncryptResult, 0, resultArr, 2, aeskeyEncryptResult.length);
            return resultArr;
        } catch (Exception e) {
        }
        return null;
    }
    
    public static byte[] upgrade(byte[] curServerKey, byte ver, int firmVersion, short checksum, String uuid) {
        byte cmd = 0xD;
        int expireTime = (int) Math.ceil((System.currentTimeMillis() / 1000)) + EXPIRE_TIME;
        byte[] extTimeBytes = DataUtil.intToByteArray(expireTime);
        byte[] firmVersionBytes = DataUtil.intToByteArray(firmVersion);
        byte[] chkBytes = DataUtil.shortToByteArray(checksum);
        byte[] dataArr = new byte[extTimeBytes.length + firmVersionBytes.length + chkBytes.length + 2];

        System.arraycopy(extTimeBytes, 0, dataArr, 0, extTimeBytes.length);
        System.arraycopy(firmVersionBytes, 0, dataArr, extTimeBytes.length, firmVersionBytes.length);
        System.arraycopy(chkBytes, 0, dataArr, extTimeBytes.length + firmVersionBytes.length, chkBytes.length);
        System.arraycopy(uuid.getBytes(), 0, dataArr, extTimeBytes.length + firmVersionBytes.length + chkBytes.length, 2);
        dataArr = addPaddingTo16Bytes(dataArr);

        try {
            byte[] aeskeyEncryptResult = AESUtil.encrypt(dataArr, curServerKey);
            byte[] resultArr = new byte[2 + aeskeyEncryptResult.length];
            resultArr[0] = cmd;
            resultArr[1] = ver;
            System.arraycopy(aeskeyEncryptResult, 0, resultArr, 2, aeskeyEncryptResult.length);
            return resultArr;
        } catch (Exception e) {
        }
        return null;
    }

    public static byte[] newUserConnCmd(byte[] pubkey32b) {
        byte cmd = 0x7;
        byte ver = 0x0;
        byte[] resultArr = new byte[2 + pubkey32b.length];
        resultArr[0] = cmd;
        resultArr[1] = ver;

        System.arraycopy(pubkey32b, 0, resultArr, 2, pubkey32b.length);

        return resultArr;
    }

//    public static byte[] openDoorCmd(byte[] aesRandomKey) {
//        byte cmd = 0x8;
//        byte ver = 0x0;
//        byte[] dataArr = new byte[16];
//        int curTime = (int) Math.ceil((System.currentTimeMillis() / 1000));
//        byte[] curTimeBytes = DataUtil.intToByteArray(curTime);
//        System.arraycopy(curTimeBytes, 0, dataArr, 0, curTimeBytes.length);
//        byte[] uidBytes = {1, 2, 3, 4};
//        System.arraycopy(uidBytes, 0, dataArr, curTimeBytes.length, uidBytes.length);
//        byte[] uidPadding = {1, 1, 1};
//        System.arraycopy(uidPadding, 0, dataArr, curTimeBytes.length + uidBytes.length, uidPadding.length);
//        byte[] type = {0x3a};
//        System.arraycopy(type, 0, dataArr, curTimeBytes.length + uidBytes.length + uidPadding.length, type.length);
//        byte[] keyId = {1, 2};
//        System.arraycopy(keyId, 0, dataArr, curTimeBytes.length + uidBytes.length + uidPadding.length + type.length, keyId.length);
//        byte[] checkSum = DataUtil.shortToByteArray(getCheckSum(dataArr));
//        System.arraycopy(checkSum, 0, dataArr, curTimeBytes.length + uidBytes.length + uidPadding.length + type.length + keyId.length, checkSum.length);
//        try {
//            byte[] curServerKey = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
//            byte[] serverkeyEncryptResult = AESUtil.encrypt(dataArr, curServerKey);
//            byte[] serverkeyEncryptPaddingResult = addPaddingTo16Bytes(serverkeyEncryptResult);
//            byte[] aeskeyEncryptResult = AESUtil.encrypt(serverkeyEncryptPaddingResult, aesRandomKey);
//
//            byte[] resultArr = new byte[2 + aeskeyEncryptResult.length];
//            resultArr[0] = cmd;
//            resultArr[1] = ver;
//            System.arraycopy(aeskeyEncryptResult, 0, resultArr, 2, aeskeyEncryptResult.length);
//
//
//            return resultArr;
//        } catch (Exception e) {
//        }
//        return null;
//    }
    
    public static byte[] openDoorCmd(byte[] aesUserKey) {
        byte cmd = 0x8;
        byte ver = 0x0;
        try {
            if (null != aesUserKey) {
                byte[] serverkeyEncryptPaddingResult = addPaddingTo16Bytes(aesUserKey);
                byte[] resultArr = new byte[2 + serverkeyEncryptPaddingResult.length];
                resultArr[0] = cmd;
                resultArr[1] = ver;
                System.arraycopy(serverkeyEncryptPaddingResult, 0, resultArr, 2, serverkeyEncryptPaddingResult.length);
                return resultArr;
            }
        } catch (Exception e) {
            LOGGER.error("openDoorCmd error", e);
        }
        return null;
    }
    
    public static byte[] remoteOpenCmd(byte[] aesUserKey) {
        byte cmd = 0xA;
        byte ver = 0x0;
        try {
            if (null != aesUserKey) {
                byte[] serverkeyEncryptPaddingResult = addPaddingTo16Bytes(aesUserKey);
                byte[] resultArr = new byte[2 + serverkeyEncryptPaddingResult.length];
                resultArr[0] = cmd;
                resultArr[1] = ver;
                System.arraycopy(serverkeyEncryptPaddingResult, 0, resultArr, 2, serverkeyEncryptPaddingResult.length);
                return resultArr;
            }
        } catch (Exception e) {
            LOGGER.error("openDoorCmd error", e);
        }
        return null;
    }
    
    public static byte[] faceOpenCmd(byte[] aesUserKey) {
        byte cmd = 0x11;
        byte ver = 0x0;
        try {
            if (null != aesUserKey) {
                byte[] serverkeyEncryptPaddingResult = addPaddingTo16Bytes(aesUserKey);
                byte[] resultArr = new byte[2 + serverkeyEncryptPaddingResult.length];
                resultArr[0] = cmd;
                resultArr[1] = ver;
                System.arraycopy(serverkeyEncryptPaddingResult, 0, resultArr, 2, serverkeyEncryptPaddingResult.length);
                return resultArr;
            }
        } catch (Exception e) {
            LOGGER.error("openDoorCmd error", e);
        }
        return null;
    }
    
    public static byte[] wifiCmd(byte[] curServerKey, byte ver, String wifiSsid, String wifiPwd, String borderUrl) {
        byte cmd = 0xB;

        int time = (int) Math.ceil((System.currentTimeMillis() / 1000)) + EXPIRE_TIME;
        
        byte[] timeBytes = DataUtil.intToByteArray(time);
        byte[] ssidData = wifiSsid.getBytes();
        byte[] pwdData = wifiPwd.getBytes();
        byte[] serverUrlData = borderUrl.getBytes();
        byte[] len = {(byte) ssidData.length, (byte) pwdData.length, (byte) serverUrlData.length};
        byte[] dataArr = new byte[timeBytes.length + len.length + ssidData.length + pwdData.length + serverUrlData.length];
        System.arraycopy(timeBytes, 0, dataArr, 0, timeBytes.length);
        System.arraycopy(len, 0, dataArr, timeBytes.length, len.length);
        System.arraycopy(ssidData, 0, dataArr, timeBytes.length + len.length, ssidData.length);
        System.arraycopy(pwdData, 0, dataArr, timeBytes.length + len.length + ssidData.length, pwdData.length);
        System.arraycopy(serverUrlData, 0, dataArr, timeBytes.length + len.length + ssidData.length + pwdData.length, serverUrlData.length);
        
        try {
            byte[] odata = addPaddingTo16Bytes(dataArr);
            LOGGER.error(StringHelper.toHexString(odata));
            byte[] aeskeyEncryptResult = AESUtil.encrypt(odata, curServerKey);
            byte[] resultArr = new byte[2 + aeskeyEncryptResult.length];
            resultArr[0] = cmd;
            resultArr[1] = ver;
            System.arraycopy(aeskeyEncryptResult, 0, resultArr, 2, aeskeyEncryptResult.length);
            LOGGER.error(StringHelper.toHexString(resultArr));
            return resultArr;
        } catch (Exception e) {
            LOGGER.error("wifiCmd()...", e);
        }
        return null;
    }
    
    public static byte[] setServerKeyCmd(byte oldVer, byte[] oldServerKey, byte[] newServerKey) {
        if (null != oldServerKey && null != newServerKey) {
            byte cmd = 0x3;
            try {
                byte[] serverkeyEncryptPaddingResult = AESUtil.encrypt(newServerKey, oldServerKey);
                byte[] resultArr = new byte[2 + serverkeyEncryptPaddingResult.length];
                resultArr[0] = cmd;
                resultArr[1] = oldVer;
                System.arraycopy(serverkeyEncryptPaddingResult, 0, resultArr, 2, serverkeyEncryptPaddingResult.length);
                return resultArr;
            } catch (Exception e) {
                LOGGER.error("setServerKeyCmd()..." + e.toString());
            }
        }
        return null;
    }
    
    public static byte[] forwardCmd(byte[] msg) {
        byte cmd = 0x10;
        byte ver = 0x0;
        try {
            if (null != msg) {
                byte[] serverkeyEncryptPaddingResult = addPaddingTo16Bytes(msg);
                byte[] resultArr = new byte[2 + serverkeyEncryptPaddingResult.length];
                resultArr[0] = cmd;
                resultArr[1] = ver;
                System.arraycopy(serverkeyEncryptPaddingResult, 0, resultArr, 2, serverkeyEncryptPaddingResult.length);
                return resultArr;
            }
        } catch (Exception e) {
            LOGGER.error("openDoorCmd error", e);
        }
        return null;
    }

    public static short getCheckSum(byte[] data, int len) {
        if(len <= 0 || len > data.length) {
            len = data.length;
        }
        short sum = 0;
        for (int i = 0; i < len; i++) {
            sum += (data[i] & 0xFF);
        }
        return sum;
    }

    static byte[] addPaddingTo16Bytes(byte[] source) {
        byte paddingUnit = 16;
        byte sourceCount = (byte) source.length;
        byte remainder = (byte) (sourceCount % paddingUnit);
        int paddingCount;
        if (remainder == 0) {
            if (source.length != 0) {
                return source;
            }
        }
        paddingCount = paddingUnit - remainder;
        byte[] padding = new byte[paddingCount];
        long rand = new Random().nextLong();
        for (int i = 0; i < padding.length; i++) {
            if ((i % 4) != 0) {
                rand = new Random().nextLong();
            }
            padding[i] = (byte) ((rand >> (8 * (i % 4))) & 0xFF);
        }
        byte[] resultArr = new byte[source.length + padding.length];
        for (int j = 0; j < source.length; ++j) {
            resultArr[j] = source[j];
        }
        for (int j = 0; j < padding.length; ++j) {
            resultArr[source.length + j] = padding[j];
        }
        return resultArr;
    }
    
    public static void printArray(byte[] arr) {

        StringBuffer sb = new StringBuffer();
        if (null != arr) {
            for (int i = 0; i < arr.length; i++) {
                sb.append(Integer.toHexString((arr[i] & 0xFF)) + "  ");
            }
        }
        String sbStr = sb.toString(); 
        LOGGER.info(sbStr);
    }
}
