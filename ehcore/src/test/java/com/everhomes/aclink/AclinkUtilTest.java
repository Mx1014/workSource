package com.everhomes.aclink;

public class AclinkUtilTest {
	
    private static byte[] xorResult(byte[] srcData) {
        String key = "thisjustfortime!";//TODO use constants
        byte[] keyData = key.getBytes();

//        if (null == srcData || null == keyData || srcData.length < 16 || keyData.length < 16) {
//            return srcData;
//        }
        byte[] resData = new byte[srcData.length];
        for (int i = 0; i < srcData.length; i++) {
//            resData[i] = (byte) (srcData[i] ^ keyData[i+4]);
            resData[i] = (byte) (srcData[i] ^ keyData[i]);
        }
        return resData;
    }
    
    public static long byteArrayToLong(byte[] bytes) {  
		long result = 0;
		for (int i = 0; i < bytes.length; i++) {
			long paramLong = (bytes[i] & 0xff) << (8 * (bytes.length - i - 1));
			result = result | paramLong;
		}
		return result;  
    } 
	
    // \x2f \x03 \xa3 \x5a
    // \x31 \x1e \xb9 \x11
	public static void main(String[] args) {
		byte[] dataArr = new byte[4];
		dataArr[0] = (byte) 0x2f;
		dataArr[1] = (byte) 0x03;
		dataArr[2] = (byte) 0xa3;
		dataArr[3] = (byte) 0x5a;
		byte[] resArr = xorResult(dataArr);
		long res = byteArrayToLong(resArr);
		System.out.println(res);
	}

}
