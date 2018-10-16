package com.everhomes.parking.handler;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.util.RuntimeErrorException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Utils {


    public static class MimeType {
        public static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";
        public static final String APPLICATION_JSON = "application/json";
        public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
        public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    }

    public static class DateStyle {
        public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
        public static final String DATE = "yyyy-MM-dd";
        public static final String DATE_TIME_STR = "yyyyMMddHHmmss";
        public static final String DATE_HOUR_MINUTE = "yyyy-MM-dd HH:mm";
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    /**
     * 原有时间加n秒
     * @param source
     * @param second
     * @return
     */
    static Timestamp addSecond(Long source, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(source);
        calendar.add(Calendar.SECOND, second);
        Timestamp time = new Timestamp(calendar.getTimeInMillis());

        return time;
    }

    /**
     * 原有时间加n秒
     * @param source
     * @param second
     * @return
     */
    static Long getLongByAddSecond(Long source, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(source);
        calendar.add(Calendar.SECOND, second);

        return calendar.getTimeInMillis();
    }

    /**
     * 原有时间计算月
     * @param source
     * @param month
     * @return
     */
    static Timestamp getTimestampByAddMonth(Long source, int month) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(source);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        //精确到秒,毫秒记为0
        calendar.set(Calendar.MILLISECOND, 0);

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

    /**
     * 计算从soucre之后month个月的月底
     * @param source 时间戳
     * @param month 月数量
     * @param isIgnoreLastDay
     *          是否忽略最后一天，true:如果是最后一天,不会忽略，如source是5月31日，month是2个月，那么计算的结果是6月30日
     *                          false:如果是最后一天,忽略，如source是5月31日，month是2个月，那么计算的结果是7月31日
     * @return
     */
    static Long getLongByAddNatureMonth(Long source, int month, boolean isIgnoreLastDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(source);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        //精确到秒,毫秒记为0，mysql不能存储毫秒，会自动转成秒，999ms 会转成 1S
        calendar.set(Calendar.MILLISECOND, 0);

        if(isLastDayOfMonth(calendar) && !isIgnoreLastDay){
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

    /**
     * 计算从soucre之后month个月的月底
     * @param source 时间戳
     * @param month 月数量
     *              如果是最后一天,忽略，如source是5月31日，month是2个月，那么计算的结果是7月31日
     * @return
     */
    public static Long getLongByAddNatureMonth(Long source, int month) {
        return getLongByAddNatureMonth(source,month,false);
    }

    static Long getFirstDayOfMonth(Long time) {
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.setTimeInMillis(time);
        tempCalendar.set(Calendar.DAY_OF_MONTH, 1);
        tempCalendar.set(Calendar.HOUR_OF_DAY, 0);
        tempCalendar.set(Calendar.MINUTE, 0);
        tempCalendar.set(Calendar.SECOND, 0);
        //精确到秒,毫秒记为0
        tempCalendar.set(Calendar.MILLISECOND, 0);
        return tempCalendar.getTimeInMillis();
    }

    static Long getLastDayOfMonth(Long time) {
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.setTimeInMillis(time);
        tempCalendar.set(Calendar.DAY_OF_MONTH, tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        tempCalendar.set(Calendar.HOUR_OF_DAY, 23);
        tempCalendar.set(Calendar.MINUTE, 59);
        tempCalendar.set(Calendar.SECOND, 59);
        //精确到秒,毫秒记为0
        tempCalendar.set(Calendar.MILLISECOND, 0);
        return tempCalendar.getTimeInMillis();
    }

    static Long getNewDay(Long time) {
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.setTimeInMillis(time);
        tempCalendar.set(Calendar.HOUR_OF_DAY, 0);
        tempCalendar.set(Calendar.MINUTE, 0);
        tempCalendar.set(Calendar.SECOND, 0);
        tempCalendar.set(Calendar.MILLISECOND, 0);
        return tempCalendar.getTimeInMillis();
    }

    static boolean isLastDayOfMonth(Calendar calendar) {
        //获取当前天
        int curDay = calendar.get(Calendar.DAY_OF_MONTH);
        //获取当月的最后一天
        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if(curDay == lastDay){
            return true;
        }
        return false;
    }

    static Timestamp getTimestampByAddNatureMonth(Long source, int month) {

        return new Timestamp(getLongByAddNatureMonth(source, month));
    }

    /* rp6.4
    * 深圳湾项目生态园停车场（对接科拓系统），月卡充值计算有效期采用独立规则：
    1，首先获得车辆当前的月卡有效期，得到该有效期距离该月自然月底的剩余天数；
    2，用户选择充值月数后，得到目标月份的自然月天数；
    3，用户新有效期=目标月份自然月月底-「1」所得剩余天数。*/
    static Timestamp getTimestampByAddDistanceMonth(Long source,int month){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(source);
        int daysToEndOfMonth = getDaysToEndOfMonth(calendar);

        //加month个月
        calendar.add(Calendar.MONTH,month);
        //计算最后一天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);

        calendar.add(Calendar.DAY_OF_MONTH,-daysToEndOfMonth);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /*#35812
    深圳湾停车场月卡充值规则修改
    根据用深圳湾要求，修改月卡充值规则为以下，其中软基&创投是小猫停车，生态园是科拓停车。原先在4月左右修改过一次，将三个园区的规则统一成生态园的充值规则，
    但由于甲方当时给出的附件文档与邮件内描述内容不符，造成现在部分月份充值结果与邮件内容不符，现在需统一修改成以下描述的内容。
    月卡（含固定车位月卡）收费及管理规则（生态园、软基、创投）
    月卡计费周期规则：
    当月1-28日缴费1个月的，有效期至下月对应缴费日的前一天；
    29日及之后缴费1个月的，按下一自然月完整天数扣减缴费当月剩余使用天数计算。
    */
    static Timestamp getTimestampByAddDistanceMonthV2(Long source,int month){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(source);
        int today = calendar.get(Calendar.DAY_OF_MONTH);
        if(today>28) {
            int daysToEndOfMonth = getDaysToEndOfMonth(calendar);

            //加month个月
            calendar.add(Calendar.MONTH, month);
            //计算最后一天
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 0);

            calendar.add(Calendar.DAY_OF_MONTH, -daysToEndOfMonth);
        }else{
            calendar.add(Calendar.SECOND, 1);
            calendar.add(Calendar.MONTH, month);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 中百畅停车场，要求月卡时间计算为当月时间
     * @param source
     * @param month
     * @return
     */
    static Timestamp getTimestampByAddThisMonth(long source, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(source+1000);
        calendar.add(Calendar.MONTH,month);
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 中天停车场，要求月卡时间计算为30天
     * @param source
     * @param month
     * @return
     */
    public static Timestamp getTimestampByAddThirtyDays(long source, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(source+1000);
        calendar.add(Calendar.DAY_OF_MONTH,30*month-1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return new Timestamp(calendar.getTimeInMillis());
    }


    public static Boolean isLastDayOfMonth(long now) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        return isLastDayOfMonth(calendar);
    }

    /**
     *
     * @param source
     * @param month
     * @return
     */
    static Long getTimestampByAddRealMonth(Long source,int month){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(source);
        calendar.add(Calendar.MONTH,month);

        return calendar.getTimeInMillis();
    }

    //获取到月底的天数
    static int getDaysToEndOfMonth(Calendar calendar){
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)-calendar.get(Calendar.DAY_OF_MONTH);
    }

    static Long strToLong(String str, String style) {

        SimpleDateFormat sdf = new SimpleDateFormat(style);
        long ts;
        try {
            ts = sdf.parse(str).getTime();
        } catch (ParseException e) {
            LOGGER.error("Str={} format is not style={}", str, style);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid str format");
        }
        return ts;
    }

    static Timestamp strToTimeStamp(String str, String style) {
        return new Timestamp(strToLong(str,style));
    }

    static String dateToStr(Date date, String style) {
        SimpleDateFormat sdf = new SimpleDateFormat(style);
        return sdf.format(date);
    }
    public static String longToString(long time, String style) {
        return dateToStr(new Date(time),style);
    }

    static Long subtractionTime(Long startTime, String endtimeStamp) {
        Long enttime = strToLong(endtimeStamp, DateStyle.DATE_TIME);
        return subtractionTime(startTime,enttime);
    }

    static Long subtractionTime(long startTime, long endtime) {
        return (endtime-startTime)/(60*1000);
    }

    /**
     *
     * @param url
     * @param param json格式
     * @return
     */
    public static String post(String url, JSONObject param) {
        //设置body json格式
        Map<String, String> headers = new HashMap<>();
        headers.put(HTTP.CONTENT_TYPE, MimeType.APPLICATION_JSON);
        return post(url, param, headers);
    }

    /**
     *
     * @param url
     * @param param urlencoded
     * @return
     */
    public static String postUrlencoded(String url, Map<String,String> param) {
        //设置body json格式
        Map<String, String> headers = new HashMap<>();
        headers.put(HTTP.CONTENT_TYPE, MimeType.APPLICATION_FORM_URLENCODED);
        return post(url, param, headers);
    }
    
    /**
    *
    * @param url
    * @param params
    * @param charset
    * @return
    */
    public static String post(String url, JSONObject params,Charset charset) {
    	  //设置body json格式
        Map<String, String> headers = new HashMap<>();
        headers.put(HTTP.CONTENT_TYPE, MimeType.APPLICATION_JSON);
        return post(url, params, headers,charset);
    }

    /**
    *
    * @param url
    * @param params
    * @param headers
    * @return
    */
    public static String post(String url, JSONObject params, Map<String, String> headers) {
    	return post(url, params, headers,null);
    }
    /**
     *
     * @param url
     * @param param
     * @param headers
     * @param charset
     * @return
     */

    public static String post(String url, JSONObject param, Map<String, String> headers, Charset charset) {

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);
        String result = null;
        CloseableHttpResponse response = null;

        LOGGER.info("The request info, url={}, param={}", url, param);

        if (null == param) {
            param = new JSONObject();
        }

        try {
            StringEntity stringEntity = new StringEntity(param.toString(), StandardCharsets.UTF_8);
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
                	if(charset==null){
                		result = EntityUtils.toString(entity);
                	}else{
                		result = EntityUtils.toString(entity,charset);
                	}
                }
            }

        } catch (IOException e) {
            LOGGER.error("The request error, param={}", param, e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "The request error.");
        }finally {
            close(response, httpclient);
        }

        LOGGER.info("SendResult from third, url={}, result={}", url, result);

        return result;
    }

    /**
     *
     * @param url
     * @param param 普通表单提交
     * @return
     */
    public static String post(String url, Map<String, String> param) {
        return post(url, param, null);
    }

    /**
     *
     * @param url
     * @param param 普通表单提交
     * @param headers
     * @return
     */
    public static String post(String url, Map<String, String> param, Map<String, String> headers) {

        LOGGER.info("The request info, url={}, param={}", url, JSONObject.toJSONString(param));

        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = null;

        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> nvps = new ArrayList<>();
        Set<Map.Entry<String, String>> paramsEntry = param.entrySet();
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
            LOGGER.error("The request error, param={}", param, e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "The request error.");
        }finally {
            close(response, httpclient);
        }
        LOGGER.info("SendResult from third, url={}, result={}", url, result);

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

    public static String get(String url, Charset charset) {

        String result = null;
        CloseableHttpResponse response = null;
        CloseableHttpClient httpclient = null;
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] arg0,
                                               String arg1) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] arg0,
                                               String arg1) throws CertificateException {
                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(
                    ctx, new AllowAllHostnameVerifier());

            httpclient = HttpClients.custom()
                    .setSSLSocketFactory(ssf).build();

            HttpGet httpGet = new HttpGet(url);

            LOGGER.info("The request info, url={}", url);
            response = httpclient.execute(httpGet);

            StatusLine statusLine = response.getStatusLine();
            LOGGER.info("Parking responseCode={}, responseProtocol={}", statusLine.getStatusCode(), statusLine.getProtocolVersion().toString());
            int status = statusLine.getStatusCode();

            if(status == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();

                if (null != entity) {
                    if(charset==null){
                        result = EntityUtils.toString(entity);
                    }else{
                        result = EntityUtils.toString(entity,charset);
                    }
                }
            }

        } catch (IOException e) {
            LOGGER.error("The request error, url={}", url, e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "The request error.");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } finally {
            close(response, httpclient);
        }

        LOGGER.info("Result from third, url={}, result={}", url, result);

        return result;
    }

    public static String get(Map<String,Object> map,String url) {
        String result = null;
        CloseableHttpResponse response = null;
        CloseableHttpClient httpclient = null;
        StringBuffer urlparam=new StringBuffer(url).append("?");
        ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
        if(map!=null) {
            Set set = map.entrySet();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                urlparam.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        String geturl = urlparam.toString().substring(0,urlparam.length()-1);
        LOGGER.info("The request info, geturl={}", geturl);
        try {
            httpclient = HttpClients.createDefault();
            response = httpclient.execute(new HttpGet(geturl));
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
            LOGGER.error("The request error, geturl={}", geturl, e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "The request error.");
        } finally {
            close(response, httpclient);
        }
        LOGGER.info("Result from third, geturl={}, result={}", geturl, result);

        return result;
    }

    public static void main(String[] args) {
        Timestamp timestampByAddThirtyDays = Utils.getTimestampByAddDistanceMonthV2(1564329599000L, 1);
        System.out.println(timestampByAddThirtyDays);
    }
}
