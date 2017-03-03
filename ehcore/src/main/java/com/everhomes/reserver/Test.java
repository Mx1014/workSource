package com.everhomes.reserver;

import com.everhomes.util.SignatureHelper;
import com.everhomes.util.StringHelper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Test {
    private static final String GET_RESERVER_DETAIL = "/zl-ec/rest/openapi/shop/queryShopsPositionById";
    private static final String UPDATE_RESERVER = "/zl-ec/rest/openapi/shop/updatePositionReservationResult";
    public static void main(String[] args) {
//        String appKey = configurationProvider.getValue(StatTransactionConstant.BIZ_APPKEY, "");
//        String secretKey = configurationProvider.getValue(StatTransactionConstant.BIZ_SECRET_KEY, "");
        String appKey = "d80e06ca-3766-11e5-b18f-b083fe4e159f";
        String secretKey = "g1JOZUM3BYzWpZD5Q7p3z+i/z0nj2TcokTFx2ic53FCMRIKbMhSUCi7fSu9ZklFCZ9tlj68unxur9qmOji4tNg==";

        Integer nonce = (int)(Math.random()*1000);
        Long timestamp = System.currentTimeMillis();
        Map<String,String> params = new HashMap<>();
        params.put("nonce", String.valueOf(nonce));
        params.put("timestamp", String.valueOf(timestamp));
        params.put("appKey", appKey);
        params.put("id", "1");
//        params.put("status", "1");

        Map<String, String> mapForSignature = new HashMap<>();
        for(Map.Entry<String, String> entry : params.entrySet()) {
            mapForSignature.put(entry.getKey(), entry.getValue());
        }

        String signature = SignatureHelper.computeSignature(params, secretKey);
        try {
            params.put("signature", URLEncoder.encode(signature,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println(post(params, GET_RESERVER_DETAIL));
        System.out.println(new Date(1511798399000L));
    }

    private static String post(Map<String,String> param, String method) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String url = "http://biz-beta.zuolin.com";
        HttpPost httpPost = new HttpPost(url + method);
        CloseableHttpResponse response = null;

        String json = null;
        try {
            String p = StringHelper.toJsonString(param);
            StringEntity stringEntity = new StringEntity(p, StandardCharsets.UTF_8);
            httpPost.setEntity(stringEntity);
            httpPost.addHeader("content-type", "application/json");
            response = httpclient.execute(httpPost);

            int status = response.getStatusLine().getStatusCode();
            if(status == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    json = EntityUtils.toString(entity, "utf8");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                }
            }
        }
        return json;
    }
}
