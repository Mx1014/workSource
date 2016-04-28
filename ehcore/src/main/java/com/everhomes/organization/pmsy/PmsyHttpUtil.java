package com.everhomes.organization.pmsy;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.util.RuntimeErrorException;
import com.google.gson.Gson;

public class PmsyHttpUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(PmsyHttpUtil.class);

	static CloseableHttpClient httpclient = HttpClients.createDefault();
	public static String post(String p0, String p1, String p2, String p3, String p4, String p5, String p6, String p7){
		String result = null;
        try {

            HttpPost httpPost = new HttpPost("http://sysuser.kmdns.net:9090/NetApp/SYS86Service.asmx/GetSYS86Service");
            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            nvps.add(new BasicNameValuePair("strToken", "syswin"));
            nvps.add(new BasicNameValuePair("p0", p0));
            nvps.add(new BasicNameValuePair("p1", p1));
            nvps.add(new BasicNameValuePair("p2", p2));
            nvps.add(new BasicNameValuePair("p3", p3));
            nvps.add(new BasicNameValuePair("p4", p4));
            nvps.add(new BasicNameValuePair("p5", p5));
            nvps.add(new BasicNameValuePair("p6", p6));
            nvps.add(new BasicNameValuePair("p7", p7));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
            CloseableHttpResponse response = httpclient.execute(httpPost);

            int status = response.getStatusLine().getStatusCode();
            if(status != 200){
            	LOGGER.error("the request of siyuan is {}.",response.getStatusLine());
    			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
    					"the request of siyuan is fail.");
            }
            if(status == 200){
                HttpEntity entity = response.getEntity();
                byte[] bytes = new byte[1024];
                StringBuilder sb = new StringBuilder();
                if (entity != null) {
                	InputStream instream = entity.getContent();
                	while(instream.read(bytes, 0, bytes.length) != -1){
                		sb.append(new String(bytes));
                	}
                			//sb.substring(sb.indexOf(">{")+2, sb.indexOf("</string>"));
                	result = sb.substring(sb.indexOf(">{")+1, sb.indexOf("</string>"));
                			// do something useful with the response
                	instream.close();
                }
                	// do something useful with the response body
                	// and ensure it is fully consumed
                EntityUtils.consume(entity);
             }
             response.close();
        } catch (IOException e) {
			e.printStackTrace();
		} finally {
            try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return result;
	}
	public static void main(String[] args) {
//		String json = post("UserRev_OwnerVerify","尹秀容","13800010001","","","","","");
//		Gson gson = new Gson();
//		Map map = gson.fromJson(json, Map.class);
//		System.out.println(map);
//		List list = (List) map.get("UserRev_OwnerVerify");
//		Map map2 = (Map) list.get(0);
//		List list2 = (List) map2.get("Syswin");
//		Map map3 = (Map) list2.get(0);
//		System.out.println(map3.get("ProjectID"));
//		System.out.println(map3.get("ProjectName"));
//		System.out.println(map3.get("CttID"));
//		System.out.println(map3.get("CusID"));
//		System.out.println(map3.get("ResID"));
//		System.out.println(map3.get("ResCode"));
//		System.out.println(map3.get("ResName"));
//		System.out.println(map3.get("BuildingCode"));
//		System.out.println(map3.get("BuildingName"));
//		System.out.println(map3.get("IDCardName"));
//		System.out.println(map3.get("IDCardNo"));
//		System.out.println(map3.get("CusProperty"));
//		StringBuilder resourceName = new StringBuilder();
//		resourceName.append(map3.get("ProjectName"))
//					.append(" ")
//					.append(map3.get("BuildingName"))
//					.append(" ")
//					.append(map3.get("ResName"));
//		System.out.println(resourceName.toString());
		HashSet<String> set = new HashSet<String>();
		set.add("123");
		set.add("123");
		set.add("123");
		set.add("123");
		System.out.println(set);
	}
	
}
