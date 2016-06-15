package com.everhomes.payment.util;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.crypto.Cipher;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;

/**
 * 数字证书.
 * 
 * @author haichen.ma
 *
 */
public abstract class CertCoder {
	public static final String CERT_TYPE = "X.509";
	
	/**
     * 最大文件加密块
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    
    /**
     * 最大文件解密块
     */
    private static final int MAX_DECRYPT_BLOCK = 128;
    
	/**
	 * 由KeyStore 获得私钥.
	 * 
	 * @param keyStorePath KeyStore目录
	 * @param alias 别名
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private static PrivateKey getPrivateKeyByKeyStore(String keyStorePath, String alias, String ksPassword, String pkPassword)
		throws Exception {
		// 加载密钥库.
		KeyStore ks = getKeyStore(keyStorePath, ksPassword);
		
		return (PrivateKey) ks.getKey(alias, pkPassword.toCharArray());
	}

	/**
	 * 获得KeyStore.
	 * 
	 * @param keyStorePath
	 * @param password
	 * @return
	 * @throws Exception 
	 */
	private static KeyStore getKeyStore(String keyStorePath, String password) throws Exception {
		// 实例化密钥库.
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		
		// 加载密钥库.
		FileInputStream is = null;
		
		try {
			is = new FileInputStream(keyStorePath);
			ks.load(is, password.toCharArray());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			IOUtils.closeQuietly(is);
		}
		
		return ks;
	}
	
	/**
	 * 由Cert 获得公钥.
	 * 
	 * @param certPath
	 * @return
	 * @throws Exception
	 */
	private static PublicKey getPublicKeyByCert(String certPath) throws Exception {
		// 获得证书.
		Certificate cert = getCert(certPath);
		
		// 获得公钥.
		return cert.getPublicKey();
	}
	
	/**
	 * 从证书文件获得Cert.
	 * 
	 * @param certPath
	 * @return
	 * @throws Exception
	 */
	private static Certificate getCert(String certPath) throws Exception {
		// 实例化证书工厂.
		CertificateFactory factory = CertificateFactory.getInstance(CERT_TYPE);
		
		// 加载证书.
		FileInputStream is = null;
		
		try {
			is = new FileInputStream(certPath);
			
			return factory.generateCertificate(is);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			IOUtils.closeQuietly(is);
		}
		
		return null;
	}
	
	/**
	 * 由密钥库获得Cert.
	 * 
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private static Certificate getCert(String keyStorePath, String alias, String password) throws Exception {
		// 获得密钥库.
		KeyStore ks = getKeyStore(keyStorePath, password);
		
		return ks.getCertificate(alias);
	}
	
	/**
	 * 私钥加密.
	 * 
	 * @param data
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String keyStorePath, String alias, String ksPassword, String pkPassword) throws Exception {
		// 取得私钥.
		PrivateKey privateKey = getPrivateKeyByKeyStore(keyStorePath, alias, ksPassword, pkPassword);
		
		// 对数据加密.
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		
		int dataLen = data.length;
		int offset = 0;
		byte[] ciphertext = null;
		
		while(dataLen - offset > 0){
			if(dataLen - offset > MAX_ENCRYPT_BLOCK){
				ciphertext = ArrayUtils.addAll(ciphertext, cipher.doFinal(data, offset, MAX_ENCRYPT_BLOCK));
				offset = offset + MAX_ENCRYPT_BLOCK;
			}else{
				ciphertext = ArrayUtils.addAll(ciphertext, cipher.doFinal(data, offset, dataLen - offset));
				offset = dataLen;
			}
		}
		
		return ciphertext;
	}
	
	/**
	 * 私钥解密.
	 * 
	 * @param data
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] data, String keyStorePath, String alias, String ksPassword, String pkPassword) throws Exception {
		// 取得私钥.
		PrivateKey privateKey = getPrivateKeyByKeyStore(keyStorePath, alias, ksPassword, pkPassword);
		
		// 对数据加密.
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		
		int dataLen = data.length;
		int offset = 0;
		byte[] plaintext = null;
		
		while(dataLen - offset > 0){
			if(dataLen - offset > MAX_DECRYPT_BLOCK){
				plaintext = ArrayUtils.addAll(plaintext, cipher.doFinal(data, offset, MAX_DECRYPT_BLOCK));
				offset = offset + MAX_DECRYPT_BLOCK;
			}else{
				plaintext = ArrayUtils.addAll(plaintext, cipher.doFinal(data, offset, dataLen - offset));
				offset = dataLen;
			}
		}
		
		return plaintext;
	}
	
	/**
	 * 公钥加密.
	 * 
	 * @param data
	 * @param certPath
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(byte[] data, String certPath) throws Exception {
		// 取得公钥.
		PublicKey publicKey = getPublicKeyByCert(certPath);
		
		// 对数据加密.
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		
		int dataLen = data.length;
		int offset = 0;
		byte[] ciphertext = null;
		
		while(dataLen - offset > 0){
			if(dataLen - offset > MAX_ENCRYPT_BLOCK){
				ciphertext = ArrayUtils.addAll(ciphertext, cipher.doFinal(data, offset, MAX_ENCRYPT_BLOCK));
				offset = offset + MAX_ENCRYPT_BLOCK;
			}else{
				ciphertext = ArrayUtils.addAll(ciphertext, cipher.doFinal(data, offset, dataLen - offset));
				offset = dataLen;
			}
		}
		
		return ciphertext;
	}
	
	/**
	 * 公钥解密.
	 * 
	 * @param data
	 * @param certPath
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] data, String certPath) throws Exception {
		// 取得公钥.
		PublicKey publicKey = getPublicKeyByCert(certPath);
		
		// 对数据加密.
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		
		int dataLen = data.length;
		int offset = 0;
		byte[] plaintext = null;
		
		while(dataLen - offset > 0){
			if(dataLen - offset > MAX_DECRYPT_BLOCK){
				plaintext = ArrayUtils.addAll(plaintext, cipher.doFinal(data, offset, MAX_DECRYPT_BLOCK));
				offset = offset + MAX_DECRYPT_BLOCK;
			}else{
				plaintext = ArrayUtils.addAll(plaintext, cipher.doFinal(data, offset, dataLen - offset));
				offset = dataLen;
			}
		}
		
		return plaintext;
	}
	
	/**
	 * 签名.
	 * 
	 * @param data
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static byte[] sign(byte[] data, String keyStorePath, String alias, String ksPassword, String pkPassword) throws Exception {
		// 获得证书.
		X509Certificate cert = (X509Certificate) getCert(keyStorePath, alias, ksPassword);
		
		// 获得私钥.
		PrivateKey privateKey = getPrivateKeyByKeyStore(keyStorePath, alias, ksPassword, pkPassword);
		
		// 构建签名, 由证书指定签名算法, 由私钥初始化签名.
		Signature sign = Signature.getInstance(cert.getSigAlgName());
		sign.initSign(privateKey);
		sign.update(data);
		
		return sign.sign();
	}
	
	/**
	 * 验证签名.
	 * 
	 * @param data 待验证数据.
	 * @param sign 签名数据.
	 * @param certPath 证书路径.
	 * @return
	 * @throws Exception
	 */
	public static boolean verifySign(byte[] data, byte[] sign, String certPath) throws Exception {
		// 获得证书.
		X509Certificate cert = (X509Certificate) getCert(certPath);
		
		// 由证书构建签名.
		Signature signature = Signature.getInstance(cert.getSigAlgName());
		signature.initVerify(cert);
		signature.update(data);
		
		return signature.verify(sign);
	}
	
}
