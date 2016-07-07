package com.everhomes.payment.taotaogu;


import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.crypto.Cipher;

/**
 * 数字证书.
 * 
 * @author ipp
 *
 */
public abstract class OrderCertCoder {
	public static final String CERT_TYPE = "X.509";
	
	/**
	 * 由KeyStore 获得私钥.
	 * 
	 * @param keyStorePath KeyStore目录
	 * @param alias 别名
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private static PrivateKey getPrivateKeyByKeyStore(InputStream in, String alias, String password)
		throws Exception {
		// 加载密钥库.
		KeyStore ks = getKeyStore(in, password);
		if(alias == null){
			Enumeration<?> enumas = ks.aliases();
			if (enumas.hasMoreElements()) {
				alias = (String) enumas.nextElement();
			}
		}
		return (PrivateKey) ks.getKey(alias, password.toCharArray());
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
		KeyStore ks = KeyStore.getInstance(/*KeyStore.getDefaultType()*/"PKCS12");
		// 加载密钥库.
		in.reset();
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
		if(alias == null){
			Enumeration<?> enumas = ks.aliases();
			if (enumas.hasMoreElements()) {
				alias = (String) enumas.nextElement();
			}
		}
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
	public static byte[] encryptByPrivateKey(byte[] data, InputStream in, String alias, String password) throws Exception {
		// 取得私钥.
		PrivateKey privateKey = getPrivateKeyByKeyStore(in, alias, password);
		
		// 对数据加密.
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		
		return cipher.doFinal(data);
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
	public static byte[] decryptByPrivateKey(byte[] data, InputStream in, String alias, String password) throws Exception {
		// 取得私钥.
		PrivateKey privateKey = getPrivateKeyByKeyStore(in, alias, password);
		
		// 对数据加密.
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		
		return cipher.doFinal(data);
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
		
		return cipher.doFinal(data);
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
		
		return cipher.doFinal(data);
	}
	
	/**
	 * 签名.
	 * 
	 * @param data
	 * @param keyStorePath pfx p12
	 * @param alias
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static byte[] sign(byte[] data, InputStream in, String alias, String password) throws Exception {
		// 获得证书.
		X509Certificate cert = (X509Certificate) getCert(in, alias, password);
		// 获得私钥.
		PrivateKey privateKey = getPrivateKeyByKeyStore(in, alias, password);
		
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
	 * @param certPath 证书路径. crt cer
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
	public static void main(String[] args) {
		
//		String data = "123456dfsdf";
//		String data2 = "jslfjksklfjsldfjskdlfjksdlfjlsdjfksdjflsdfjlsdkjflsdfsdjlfkjs" +
//				"fjlsdjfksldjafiojksdmfslfksdjflsds" +
//				"sdfjksdjflsda;fjks;dfjs;dfjsd;fjksfjiewjdfldkcnslkdfjioejf";
//		System.out.println(new String(Base64.encodeBase64(data2.getBytes(), false)));
//		System.out.println("test");
//		try{
//			byte[] r=  OrderCertCoder.sign(data.getBytes(), "d:/user.pfx", null, "123456");
//			boolean b = OrderCertCoder.verifySign(data.getBytes(), r, "d:/user.cer");
//			String str = Base64.encodeBase64String(r);
//			System.out.println(str);
//			byte[] r1 = Base64.decodeBase64(str);
//			System.out.println(b);
//			b = OrderCertCoder.verifySign("123456".getBytes(), r1, "d:/user.cer");
//			
//			
//			byte en[] = OrderCertCoder.encryptByPublicKey(data.getBytes(), "d:/client.cer");
//			System.out.println("公钥加密" + Base64.encodeBase64String(en));
//			byte de[] = OrderCertCoder.decryptByPrivateKey(en, "d:/client.pfx", null, "123456");
//			String strDe = new String(de);
//			System.out.println("私钥解密" + strDe);
//			
//			byte en1[] = OrderCertCoder.encryptByPrivateKey(data.getBytes(), "d:/client.pfx",null, "123456");
//			System.out.println("私钥加密" + Base64.encodeBase64String(en1));
//			byte de1[] = OrderCertCoder.decryptByPublicKey(en1, "d:/client.cer");
//			String strDe1 = new String(de1);
//			System.out.println("公钥解密" + strDe1);
//			
//		}
//		catch (Exception e){
//			e.printStackTrace();
//		}
	}
}
