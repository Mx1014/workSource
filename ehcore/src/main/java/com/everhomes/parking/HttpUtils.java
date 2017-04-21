package com.everhomes.parking;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.util.RuntimeErrorException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Created by sw on 2017/4/7.
 */
public class HttpUtils {


    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);


    public static String post(String url, JSONObject params) {
        return post(url, params, null);
    }

    /**
     *
     * @param url
     * @param params json格式 content-type : application/json
     * @param headers
     * @return
     */
    public static String post(String url, JSONObject params, Map<String, String> headers) {

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);
        String result = null;
        CloseableHttpResponse response = null;

        LOGGER.info("The request info, url={}, param={}", url, params);

        if (null == params) {
            params = new JSONObject();
        }

        try {
            StringEntity stringEntity = new StringEntity(params.toString(), StandardCharsets.UTF_8);
            httpPost.setEntity(stringEntity);
            //设置body json格式
            httpPost.addHeader("content-type", "application/json");

            if (null != headers) {
                Set<Map.Entry<String, String>> headersEntry = headers.entrySet();
                for (Map.Entry<String, String> e: headersEntry) {
                    httpPost.addHeader(e.getKey(), e.getValue());
                }
            }

            response = httpclient.execute(httpPost);

            int status = response.getStatusLine().getStatusCode();

            if(status == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();

                if (null != entity) {
                    result = EntityUtils.toString(entity);
                }
            }

        } catch (IOException e) {
            LOGGER.error("The request error, params={}", params, e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "The request error.");
        }finally {
            close(response, httpclient);
        }

        LOGGER.info("Result from third, url={}, result={}", url, result);

        return result;
    }

    /**
     *
     * @param url
     * @param params 表单提交 content-type : application/x-www-form-urlencoded
     * @return
     */
    public static String post(String url, Map<String, String> params) {
        return post(url, params, null);
    }

    /**
     *
     * @param url
     * @param params 表单提交 content-type : application/x-www-form-urlencoded
     * @param headers
     * @return
     */
    public static String post(String url, Map<String, String> params, Map<String, String> headers) {

        LOGGER.info("The request info, url={}, param={}", url, JSONObject.toJSONString(params));

        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = null;

        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> nvps = new ArrayList<>();
        Set<Map.Entry<String, String>> paramsEntry = params.entrySet();
        for (Map.Entry<String, String> e: paramsEntry) {
            nvps.add(new BasicNameValuePair(e.getKey(), e.getValue()));
        }

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, StandardCharsets.UTF_8));

            httpPost.addHeader("content-type", "application/x-www-form-urlencoded");
            if (null != headers) {
                Set<Map.Entry<String, String>> headersEntry = headers.entrySet();
                for (Map.Entry<String, String> e: headersEntry) {
                    httpPost.addHeader(e.getKey(), e.getValue());
                }
            }

            response = httpclient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();

            if(status == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();

                if (null != entity) {
                    result = EntityUtils.toString(entity);
                }
            }
        } catch (IOException e) {
            LOGGER.error("The request error, params={}", params, e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "The request error.");
        }finally {
            close(response, httpclient);
        }
        LOGGER.info("Result from third, url={}, result={}", url, result);

        return result;
    }

    private static void close(CloseableHttpResponse response, CloseableHttpClient httpclient) {
        try {
            if (null != response) {
                response.close();
            }
            httpclient.close();
        } catch (IOException e) {
            LOGGER.error("Close response error", e);
        }
    }
}
