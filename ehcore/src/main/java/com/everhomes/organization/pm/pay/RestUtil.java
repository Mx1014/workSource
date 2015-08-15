package com.everhomes.organization.pm.pay;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class RestUtil {
	//执行调用接口post方法
		public static String restWan(String json,String restUrl){
			try {
				Client client = Client.create();
				WebResource r = client.resource(restUrl);
				json=strEncoder(json);
				ClientResponse response = r.post(ClientResponse.class, json);
				String result = response.getEntity(String.class);
				return strDecode(result);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			return null;
		}
		
		/**
		 * json参数中文编码
		 * @param json
		 * @return
		 */
		public static String strEncoder(String json){
			String newJson="";
			try {
				newJson=URLEncoder.encode(json,"UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return newJson;
		}
		
		/**
		 * json参数中文编码
		 * @param json
		 * @return
		 */
		public static String strDecode(String json){
			String newJson="";
			try {
				newJson=URLDecoder.decode(json,"UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return newJson;
		}
		/** 
		    *  java String 转utf-8编码 
		    */  
		    public static String getUTF8XMLString(String xml) {  
			    // A StringBuffer Object  
			    StringBuffer sb = new StringBuffer();  
			    sb.append(xml);  
			    String xmString = "";  
			    String xmlUTF8="";  
			    try {  
			    xmString = new String(sb.toString().getBytes("UTF-8"));  
			    xmlUTF8 = URLEncoder.encode(xmString, "UTF-8");  
			    System.out.println("utf-8 ���룺" + xmlUTF8) ;  
			    } catch (UnsupportedEncodingException e) {  
			    // TODO Auto-generated catch block  
			    e.printStackTrace();  
			    }  
			    // return to String Formed  
			    return xmlUTF8;  
		    } 
		    /**
			 * json参数中文编码
			 * @param json
			 * @return
			 */
		public static String strEncode(String json){
			String newJson="";
			try {
				newJson=URLDecoder.decode(json,"GBK");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return newJson;
		}
		public static void  main(String[] args){
			try {
				System.out.println(new String("wefzs".getBytes("iso-8859-1"),"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
