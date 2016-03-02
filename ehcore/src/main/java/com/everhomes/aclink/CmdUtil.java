package com.everhomes.aclink;

import java.util.Random;

public class CmdUtil {
    public final static int CMD_ACTIVE = 0x01;

    private final static int PACKAGE_MAX_LENGTH = 20;
    private final static int SINGLE_PACKAGE_DATA_LENGTH = 18;
    private final static int MULTI_PACKAGE_HEAD_DATA_LENGTH = 16;
    private final static int MULTI_PACKAGE_OTHER_DATA_LENGTH = 19;

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

    public static byte[] initServerKeyCmd(byte ver, String pubKey, String devName, int time, byte[] uuidBytes, byte[] serverKeyBytes, byte[] aesIvBytes) {
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
            byte[] encryptData = RSAUtil.encryptByRawPublicKey(resultArray, pubKey);
            byte[] headArr = {cmd, ver};
            resultArray = DataUtil.mergeArray(headArr, encryptData);

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
        int expireTime = (int) Math.ceil((System.currentTimeMillis() / 1000)) + 60;
        byte[] timeBytes = DataUtil.intToByteArray(expireTime);
        byte[] newNameBytes = DataUtil.getByte(newDevName);
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
        int expireTime = curTime + 60;
        byte[] extTimeBytes = DataUtil.intToByteArray(expireTime);
        byte[] curTimeBytes = DataUtil.intToByteArray(curTime);
        byte[] dataArr = new byte[extTimeBytes.length + curTimeBytes.length];
        System.arraycopy(extTimeBytes, 0, dataArr, 0, extTimeBytes.length);
        System.arraycopy(curTimeBytes, 0, dataArr, extTimeBytes.length, curTimeBytes.length);
        try {
            dataArr = addPaddingTo16Bytes(dataArr);
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

    public static byte[] addUndoListCmd(byte[] aesRandomKey) {
        byte cmd = 0x6;
        byte ver = 0x0;
        int curTime = (int) Math.ceil((System.currentTimeMillis() / 1000));
        int availableTime = curTime + 3600;
        int expireTime = curTime + 60;
        byte[] extTimeBytes = DataUtil.intToByteArray(expireTime);
        byte[] availableTimeBytes = DataUtil.intToByteArray(availableTime);
        byte[] keyId = {0xa, 0xa};
        byte[] dataArr = new byte[extTimeBytes.length + keyId.length + availableTimeBytes.length];

        System.arraycopy(extTimeBytes, 0, dataArr, 0, extTimeBytes.length);
        System.arraycopy(keyId, 0, dataArr, extTimeBytes.length, keyId.length);
        System.arraycopy(availableTimeBytes, 0, dataArr, extTimeBytes.length + keyId.length, availableTimeBytes.length);
        dataArr = addPaddingTo16Bytes(dataArr);

        try {
            byte[] curServerKey = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
            byte[] serverkeyEncryptResult = AESUtil.encrypt(dataArr, curServerKey);
            byte[] aeskeyEncryptResult = AESUtil.encrypt(serverkeyEncryptResult, aesRandomKey);
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

    public static byte[] openDoorCmd(byte[] aesRandomKey) {
        byte cmd = 0x8;
        byte ver = 0x0;
        byte[] dataArr = new byte[16];
        int curTime = (int) Math.ceil((System.currentTimeMillis() / 1000));
        byte[] curTimeBytes = DataUtil.intToByteArray(curTime);
        System.arraycopy(curTimeBytes, 0, dataArr, 0, curTimeBytes.length);
        byte[] uidBytes = {1, 2, 3, 4};
        System.arraycopy(uidBytes, 0, dataArr, curTimeBytes.length, uidBytes.length);
        byte[] uidPadding = {1, 1, 1};
        System.arraycopy(uidPadding, 0, dataArr, curTimeBytes.length + uidBytes.length, uidPadding.length);
        byte[] type = {0x3a};
        System.arraycopy(type, 0, dataArr, curTimeBytes.length + uidBytes.length + uidPadding.length, type.length);
        byte[] keyId = {1, 2};
        System.arraycopy(keyId, 0, dataArr, curTimeBytes.length + uidBytes.length + uidPadding.length + type.length, keyId.length);
        byte[] checkSum = DataUtil.shortToByteArray(getCheckSum(dataArr));
        System.arraycopy(checkSum, 0, dataArr, curTimeBytes.length + uidBytes.length + uidPadding.length + type.length + keyId.length, checkSum.length);
        try {
            byte[] curServerKey = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
            byte[] serverkeyEncryptResult = AESUtil.encrypt(dataArr, curServerKey);
            byte[] serverkeyEncryptPaddingResult = addPaddingTo16Bytes(serverkeyEncryptResult);
            byte[] aeskeyEncryptResult = AESUtil.encrypt(serverkeyEncryptPaddingResult, aesRandomKey);

            byte[] resultArr = new byte[2 + aeskeyEncryptResult.length];
            resultArr[0] = cmd;
            resultArr[1] = ver;
            System.arraycopy(aeskeyEncryptResult, 0, resultArr, 2, aeskeyEncryptResult.length);


            return resultArr;
        } catch (Exception e) {
        }
        return null;
    }

    public static short getCheckSum(byte[] data) {
        short sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += data[i];
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

}
