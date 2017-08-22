package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.util.RuntimeErrorException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Utils {

    public static class MimeType {
        public static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";
        public static final String APPLICATION_JSON = "application/json";
        public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
        public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    /**
     * 原有时间加n秒
     * @param oldPeriod
     * @param seconds
     * @return
     */
    static Timestamp addSeconds(Long oldPeriod, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(oldPeriod);
        calendar.add(Calendar.SECOND, seconds);
        Timestamp time = new Timestamp(calendar.getTimeInMillis());

        return time;
    }

    /**
     * 原有时间计算月
     * @param oldPeriod
     * @param month
     * @return
     */
    static Timestamp getTimestampByAddMonth(Long oldPeriod, int month) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(oldPeriod);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        //如果当前天数是当前月的最后一天，直接往上加月数
        if(currentDay == calendar.getActualMaximum(Calendar.DAY_OF_MONTH)){
            calendar.add(Calendar.MONTH, month);
            //获取新的月份的最大天数
            int d = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DAY_OF_MONTH, d);
        }else{
            calendar.add(Calendar.MONTH, month);
        }

        return new Timestamp(calendar.getTimeInMillis());
    }

    static Long getLongByAddNatureMonth(Long oldPeriod, int month) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(oldPeriod);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        if(currentDay == calendar.getActualMaximum(Calendar.DAY_OF_MONTH)){
            calendar.add(Calendar.MONTH, month);
            int d = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DAY_OF_MONTH, d);
        }else{
            calendar.add(Calendar.MONTH, month - 1);
            int d = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DAY_OF_MONTH, d);
        }

        return calendar.getTimeInMillis();
    }

    static Timestamp getTimestampByAddNatureMonth(Long oldPeriod, int month) {

        return new Timestamp(getLongByAddNatureMonth(oldPeriod, month));
    }

    static long strToDate(String str) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long ts;
        try {
            ts = sdf.parse(str).getTime();
        } catch (ParseException e) {
            LOGGER.error("Str format is not yyyy-MM-dd HH:mm:ss, str={}", str);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Str format is not yyyy-MM-dd HH:mm:ss.");
        }
        return ts;
    }

    public static String md5(String pw) {
        try {
            MessageDigest messageDigest =MessageDigest.getInstance("MD5");
            messageDigest.update(pw.getBytes());
            return new BigInteger(1, messageDigest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "Convert md5 failed");
        }
    }

    /**
     *
     * @param url
     * @param params json格式 content-type : application/json HTTP
     * @return
     */
    public static String post(String url, JSONObject params) {
        //设置body json格式
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        return post(url, params, headers);
    }

    /**
     *
     * @param url
     * @param params
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

            if (null != headers) {
                Set<Map.Entry<String, String>> headersEntry = headers.entrySet();
                for (Map.Entry<String, String> e: headersEntry) {
                    httpPost.addHeader(e.getKey(), e.getValue());
                }
            }

            response = httpclient.execute(httpPost);

            StatusLine statusLine = response.getStatusLine();
            LOGGER.info("Parking responseCode={}, responseProtocol={}", statusLine.getStatusCode(), statusLine.getProtocolVersion().toString());
            int status = statusLine.getStatusCode();

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
     * @param params 普通表单提交
     * @return
     */
    public static String post(String url, Map<String, String> params) {
        return post(url, params, null);
    }

    /**
     *
     * @param url
     * @param params 普通表单提交
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

            if (null != headers) {
                Set<Map.Entry<String, String>> headersEntry = headers.entrySet();
                for (Map.Entry<String, String> e: headersEntry) {
                    httpPost.addHeader(e.getKey(), e.getValue());
                }
            }

            response = httpclient.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            LOGGER.info("Parking responseCode={}, responseProtocol", statusLine.getStatusCode(), statusLine.getProtocolVersion().toString());
            int status = statusLine.getStatusCode();

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
