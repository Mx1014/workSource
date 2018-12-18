package com.everhomes.util.xml;

import com.thoughtworks.xstream.XStream;

public class XMLToObject {
	
	/**
	 * 该clazz需要先进行标记 参考：Brocadesoft.class
	 * @param clazz
	 * @param xml
	 * @return
	 */
	public static <T> T  parseObject(Class<T> clazz, String xml) {
		XStream xstream = new XStream();
		XStream.setupDefaultSecurity(xstream); 
		xstream.allowTypes(new Class[] { clazz });
		xstream.ignoreUnknownElements(); // 忽略未知参数
		xstream.processAnnotations(clazz);
		xstream.autodetectAnnotations(true);
		return (T) xstream.fromXML(xml);
	}

}
