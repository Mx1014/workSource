package com.everhomes.payment.taotaogu;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.crypto.Cipher;

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
	private static PrivateKey getPrivateKeyByKeyStore(InputStream in, String alias, String ksPassword, String pkPassword)
		throws Exception {
		// 加载密钥库.
		KeyStore ks = getKeyStore(in, ksPassword);
		
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
	private static KeyStore getKeyStore(InputStream in, String password) throws Exception {
		// 实例化密钥库.
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		
		try {
			ks.load(in, password.toCharArray());
		}
		catch(Exception e) {
			e.printStackTrace();
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
	private static PublicKey getPublicKeyByCert(InputStream in) throws Exception {
		// 获得证书.
		Certificate cert = getCert(in);
		
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
	private static Certificate getCert(InputStream in) throws Exception {
		// 实例化证书工厂.
		CertificateFactory factory = CertificateFactory.getInstance(CERT_TYPE);
		
		// 加载证书.
		in.reset();
		try {
			
			return factory.generateCertificate(in);
		}
		catch(Exception e) {
			e.printStackTrace();
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
	private static Certificate getCert(InputStream in, String alias, String password) throws Exception {
		// 获得密钥库.
		KeyStore ks = getKeyStore(in, password);
		
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
	public static byte[] encryptByPrivateKey(byte[] data, InputStream in, String alias, String ksPassword, String pkPassword) throws Exception {
		// 取得私钥.
		PrivateKey privateKey = getPrivateKeyByKeyStore(in, alias, ksPassword, pkPassword);
		
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
	public static byte[] decryptByPrivateKey(byte[] data, InputStream in, String alias, String ksPassword, String pkPassword) throws Exception {
		// 取得私钥.
		PrivateKey privateKey = getPrivateKeyByKeyStore(in, alias, ksPassword, pkPassword);
		
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
	public static byte[] encryptByPublicKey(byte[] data, InputStream in) throws Exception {
		// 取得公钥.
		PublicKey publicKey = getPublicKeyByCert(in);
		
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
	public static byte[] decryptByPublicKey(byte[] data, InputStream in) throws Exception {
		// 取得公钥.
		PublicKey publicKey = getPublicKeyByCert(in);
		
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
	public static byte[] sign(byte[] data, InputStream in, String alias, String ksPassword, String pkPassword) throws Exception {
		// 获得证书.
		X509Certificate cert = (X509Certificate) getCert(in, alias, ksPassword);
		in.reset();
		// 获得私钥.
		PrivateKey privateKey = getPrivateKeyByKeyStore(in, alias, ksPassword, pkPassword);
		
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
	public static boolean verifySign(byte[] data, byte[] sign, InputStream in) throws Exception {
		// 获得证书.
		X509Certificate cert = (X509Certificate) getCert(in);
		
		// 由证书构建签名.
		Signature signature = Signature.getInstance(cert.getSigAlgName());
		signature.initVerify(cert);
		signature.update(data);
		
		return signature.verify(sign);
	}
	
	public static void main(String[] args) throws Exception {
//		byte[] data = "{\"AppName\":\"ICCard\",\"Version\":\"V0.01\",\"ClientDt\":\"20160511125151\",\"SrcId\":\"10002900\",\"DstId\":\"00000000\",\"MsgType\":\"1010\",\"MsgID\":\"10002900000000000000000000000411\",\"Param\":{\"branchCode\":\"10002900\",\"CardId\":\"3300025840200000090\",\"AcctType\":\"00\"}}".getBytes();
//		//4AA5559EE11221A41D4332DCD2517E11B99E1E3B81FDD1C8A1BA71022F4B9D639A4995A6F0A4FE3B8E5E1128C964884A2AA9FC2924F33351F36722CA04F6B8BD711C6EB4DA65B53BE84C78515B1EF97575FC06E7259B74651254543D2C984098082F55E1C24AD1152F1A78F6EDDEB73AF9E86439703F322E0A81732C85E08C6B
//		byte[] sign = sign(data, "d:/jxd.keystore", "jxd", "123456", "123456");
//		System.out.println(ByteTools.BytesToHexStr(sign));
//		boolean status = verifySign(data, sign, "d:/jxd.cer");
////		
//		System.out.println(status);
		
		//公钥加密-私钥解密
//		byte[] ciphertext = encryptByPublicKey("123456".getBytes(), "h:/pin3.crt");
//		System.out.println("共钥加密-密文：" + ByteTools.BytesToHexStr(ciphertext));
		
//		byte[] plaintext = decryptByPrivateKey(ciphertext, "e:/xuyuji.keystore", "xuyuji", "123456", "123456");
//		System.out.println("私钥解密-明文：" + new String(plaintext));
//		
//		//私钥加密-公钥解密
//		byte[] ciphertext = encryptByPrivateKey("123456".getBytes(), "E:\\一卡通接口\\jxd.keystore","jxd", "123456", "123456");;
//		System.out.println("私钥加密-密文：" + ByteTools.BytesToHexStr(ciphertext));
//		
//		plaintext = decryptByPublicKey(ciphertext, "e:/xuyuji.cer");
//		System.out.println("共钥解密-明文：" + new String(plaintext));
	}
}
