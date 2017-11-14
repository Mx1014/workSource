package com.everhomes.rest.aclink;

public class DataUtil {
    /**
     * int到byte[]
     *
     * @param i
     * @return
     */
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        //由高位到低位
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }
    
    public static byte[] longToByteArray(long i) {
        //TODO use 8 bytes?
        byte[] result = new byte[4];
        //由高位到低位
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }
    
    public static byte[] shortToByteArray(short i) {
        byte[] result = new byte[2];
        result[0] = (byte)((i >> 8) & 0xFF);
        result[1] = (byte)(i & 0xFF);
        return result;
    }
    
    public static short byteToShort(byte[] b) {
        short s = (short)(((int)b[0] << 8) + (int)b[1]);
        return s;
    }

    /**
     * byte[]转int
     *
     * @param bytes
     * @return
     */
    public static int byteArrayToInt(byte[] bytes) {
        int value = 0;
        //由高位到低位
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (bytes[i] & 0x000000FF) << shift;//往高位游
        }
        return value;
    }


    public static byte[] mergeArray(byte[] arrayA, byte[] arrayB) {
        if (null == arrayA || null == arrayB) {
            return null;
        }
        byte[] c = new byte[arrayA.length + arrayB.length];

        for (int i = 0; i < arrayA.length; i++) {
            c[i] = arrayA[i];
        }
        for (int i = 0; i < arrayB.length; i++) {
            c[arrayA.length + i] = arrayB[i];
        }
        return c;
    }

    public static String byteArrayToString(byte[] data) {
        if (data != null && data.length > 0) {
            final StringBuilder stringBuilder = new StringBuilder(data.length);
            for (byte byteChar : data) {
                stringBuilder.append(String.format("%02X", byteChar));
            }
            return stringBuilder.toString();
        }
        return null;
    }

//    public static byte[] getByte(String data) {
//        int len = data.length();
//        byte[] cmd = new byte[len / 2];
//
//        try {
//            for (int i = 0; i < len / 2; i++) {
//                String sub = data.substring(2 * i, 2 * (i + 1));
//                cmd[i] = (byte) (Integer.valueOf(sub, 16) & 0xFF);
//
//            }
//        } catch (NumberFormatException e) {
//                //TODO
//        }
//        return cmd;
//    }
}
