package com.everhomes.openapi.lufu;

import java.security.MessageDigest;

import org.apache.tomcat.util.codec.binary.Base64;

import com.everhomes.util.StringHelper;

/**
*@author created by yangcx
*@date 2018年5月7日---下午3:43:08
**/
public class TestMD5 {
	
	public static void main(String[] args) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			String phoneNumAndKey = "15018490384" + "70f2ea6d54fb44d5a18ac11f66d25154";
			md5.update(phoneNumAndKey.getBytes());
			String encodeMD5 = StringHelper.toHexString(md5.digest()).toUpperCase();
			System.out.println(encodeMD5);
			String base64 = Base64.encodeBase64String(encodeMD5.getBytes());
			System.out.println(base64);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
