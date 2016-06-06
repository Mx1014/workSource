package com.everhomes.payment.util;

import java.io.UnsupportedEncodingException;

public class ByteTools {

	private static final String[] HEX_STR = {"0","1" ,"2" ,"3" ,"4" ,"5" ,"6" ,"7" ,"8" ,"9" ,"A" ,"B" ,"C" ,"D" ,"E" ,"F" };
    
    /**
     * 字节数组转化十六进制字符串
     * @param bytes
     * @return
     */
    public static String BytesToHexStr( byte[] bytes){
          StringBuilder sb = new StringBuilder();
          
           for( byte b : bytes){
               sb.append( HEX_STR[(b >> 4) & 0x0f]).append(HEX_STR[b & 0x0f]);
          }
          
           return sb.toString();
    }
	
	/**
     * 十六进制字符串转化为字节数组
     * @param hexStr
     * @return
     * @throws Exception
     */
    public static byte[] HexStrToBytes(String hexStr) throws Exception{
           if(hexStr.length() % 2 != 0){
                throw new Exception("输入的十六进制字符串非偶数！");
          }
          
           byte[] bytes = new byte[hexStr.length() / 2];
           for( int i = 0; i < bytes. length; i++){
               bytes[i] = (byte)Integer. parseInt(hexStr.substring(i * 2, i * 2 + 2), 16);
          }
          
           return bytes;
    }
    
    public static String BytesToGBKStr( byte[] bytes){
    	try {
    		return new String(bytes,"GBK");
    	} catch (UnsupportedEncodingException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    		return e.getMessage();
    	}
    }
    
}
