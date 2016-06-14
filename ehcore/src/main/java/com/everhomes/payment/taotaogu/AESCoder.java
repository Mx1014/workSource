package com.everhomes.payment.taotaogu;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 * AES 编码.
 * 
 * @author mhc
 */
public abstract class AESCoder {
	/**
	 * 密钥算法.
	 */
	public static final String KEY_ALGORITHM = "AES";
	
	/**
	 * 加密解密算法/工作模式/填充方式
	 */
	public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	
	/**
	 * 密钥长度.
	 */
	public static int KEY_SIZE = 256;
	
	/**
	 * 生成密钥.
	 * 
	 * @return 
	 * @throws Exception
	 */
	public static byte[] initKey() throws Exception {
		KeyGenerator gen = KeyGenerator.getInstance(KEY_ALGORITHM);
		gen.init(KEY_SIZE);
		
		// 生成密钥.
		SecretKey key = gen.generateKey();
		
		return key.getEncoded();
	}
	
	/**
	 * 转换密钥.
	 * 
	 * @param key 字节数组密钥
	 * @return Key 密钥
	 */
	private static Key toKey(byte[] key) throws Exception {
		return new SecretKeySpec(key, KEY_ALGORITHM);
	}
	
	/**
	 * 加密.
	 * 
	 * @param data 字节数组
	 * @return Key 密钥
	 */
	public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		// 还原密钥.
		Key k = toKey(key);
		
		// 实例化, 设置加密模式.
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, k);
		
		// 执行.
		return cipher.doFinal(data);
	}
	
	/**
	 * 解密.
	 * 
	 * @param data 字节数组
	 * @return Key 密钥
	 */
	public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		// 还原密钥.
		Key k = toKey(key);
		
		// 实例化, 设置解密模式.
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, k);
		
		// 执行.
		return cipher.doFinal(data);
	}
}
