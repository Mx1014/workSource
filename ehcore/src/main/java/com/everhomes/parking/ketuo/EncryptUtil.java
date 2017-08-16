package com.everhomes.parking.ketuo;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Encoder;
import com.alibaba.fastjson.JSONObject;

import java.security.MessageDigest;

/**
 * 测试环境
http://220.160.111.114:9099/
user:  ktapi
pwd:  0306A9
key: F7A0B971B199FD2A1017CEC5
科拓软件江棋 2016/9/6 16:28:39
浙A185VK
浙A111VK
 * @author Administrator
 *
 */
public class EncryptUtil {
	
	public static final String CHARSET = "gb2312";

	public final static char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

        public static String getEncString(JSONObject data, String key, String iv) throws Exception {
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        DESedeKeySpec dks = new DESedeKeySpec(key.getBytes(CHARSET));
        IvParameterSpec ivs = new IvParameterSpec(iv.getBytes(CHARSET));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey securekey = keyFactory.generateSecret(dks);
        cipher.init(Cipher.ENCRYPT_MODE, securekey, ivs);
        BASE64Encoder base64Encoder = new BASE64Encoder();

        return base64Encoder.encode(cipher.doFinal(data.toJSONString().getBytes(CHARSET)));

    }

    public static String md5Encode(String s){
            try {
                    byte[] btInput = s.getBytes();
                    // 获得MD5摘要算法的 MessageDigest 对象
                    MessageDigest mdInst = MessageDigest.getInstance("MD5");
                    // 使用指定的字节更新摘要
                    mdInst.update(btInput);
                    // 获得密文
                    byte[] md = mdInst.digest();
                    // 把密文转换成十六进制的字符串形式
                    int j = md.length;
                    char str[] = new char[j * 2];
                    int k = 0;
                    for (int i = 0; i < j; i++) {
                            byte byte0 = md[i];
                            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                            str[k++] = hexDigits[byte0 & 0xf];
                    }
                    return new String(str);
            } catch (Exception e) {
                    e.printStackTrace();
                    return null;
            }
    }
}
