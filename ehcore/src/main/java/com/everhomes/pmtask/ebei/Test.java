package com.everhomes.pmtask.ebei;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

public class Test {
	
	public static void main(String[] args) {
		String url = "http://120.24.88.192:13902/ebeitest/rest/crmFeedBackInfoJoin/serviceTypeList";
		JSONObject param = new JSONObject();
		param.put("projectId", "240111044331055940");
		Map<String, String> headers = new HashMap<>();
		headers.put("EBEI_TOKEN", "Kf3j5uHnxjSst37WgYNIrrR//wVj0l1R0unt1KrrwnXnn5f8zmCcnwSrK0pcON3K\r\n");
		headers.put("HTMIMI_USERID", "123456");
		JSONObject result = post(url, param, null);
		System.out.println(result.toJSONString());
	}
	
	public static JSONObject post(String url, JSONObject json, Map<String, String> headers)
    {
        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        JSONObject response = null;
        post.setHeader("Content-Type", "application/json");
        if (headers != null)
        {
            Set<String> keys = headers.keySet();
            for (Iterator<String> i = keys.iterator(); i.hasNext();)
            {
                String key = (String)i.next();
                post.addHeader(key, headers.get(key));
                
            }
        }
        
        try
        {
            StringEntity s = new StringEntity(json.toString(), "utf-8");
            s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(s);
            HttpResponse httpResponse = client.execute(post);
            InputStream inStream = httpResponse.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
            StringBuilder strber = new StringBuilder();
            
            String line = null;
            while ((line = reader.readLine()) != null)
                strber.append(line + "\n");
            inStream.close();
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                HttpEntity entity = httpResponse.getEntity();
                String charset = EntityUtils.getContentCharSet(entity);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return response;
    }
}
