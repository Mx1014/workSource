package com.everhomes.generate;

import org.apache.tomcat.util.codec.binary.Base64;

public class Test {
	public static void main(String[] args) {
		String a = Base64.encodeBase64URLSafeString("http://alpha.lab.everhomes.com/qr?qrid=UfkYVHhEYGRT3cAUTUqZiXu4yjGj5GNj-SNVwzPpqgVpoOWAQTT_dOBSr6mMvZXNqiur5tIhWvJ5X2pJEQFipA".getBytes());
		System.out.println(a);
		
		System.out.println(Base64.encodeBase64String("http://alpha.lab.everhomes.com/qr?qrid=UfkYVHhEYGRT3cAUTUqZiXu4yjGj5GNj-SNVwzPpqgVpoOWAQTT_dOBSr6mMvZXNqiur5tIhWvJ5X2pJEQFipA".getBytes()));
	}
}
