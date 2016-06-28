package com.everhomes.payment.taotaogu;

import java.security.MessageDigest;

public class SHA1 {
	public static String EnCodeSHA1(String decript) throws Exception {
		MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
		digest.update(decript.getBytes("GBK"));
		byte messageDigest[] = digest.digest();
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < messageDigest.length; i++) {
			String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
			if (shaHex.length() < 2) {
				hexString.append(0);
			}
			hexString.append(shaHex);
		}
		return hexString.toString();
	}
	
}
