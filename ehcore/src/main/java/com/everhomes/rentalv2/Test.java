package com.everhomes.rentalv2;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.rentalv2.RentalGoodItem;
import com.everhomes.rest.rentalv2.admin.AttachmentConfigDTO;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2017/4/27.
 */
public class Test {
    private static final String GET_CARD = "70111003";

    static String version = "1.4";
    static String licensekey = "42011520000220161231235959102642";
    static String parkName = "测试停车场";
    static String serverUrl = "http://parking.tnar.cn:8085/kesb_req";

    public static void main(String[] args) {
        Map<java.sql.Date, Map<String,Set<Byte>>> dayMap= new HashMap<>();
        String s = String.valueOf((int) 8.5 / 1)
                + ":"
                + String.valueOf((int) (( 8.5 % 1) * 60))
                + ":00";
        System.out.println(s);
//        AttachmentConfigDTO a = new AttachmentConfigDTO();
//
//        a.setAttachmentType((byte)1);
//        a.setContent("123");
//        a.setId(1L);
//        a.setMustOptions((byte)2);
//        RentalGoodItem item = new RentalGoodItem();
//        item.setId(2L);
//        item.setItemName("ceshi");
//        a.setGoodItems(Collections.singletonList(item));
//
//        RentalConfigAttachment rca = ConvertHelper.convert(item, RentalConfigAttachment.class);
//        System.out.println(rca);
//
//        JSONObject param = new JSONObject();
//
//        param.put("version", version);
//        param.put("licensekey", licensekey);
//        param.put("car_id", "苏FX967J");
//
//        String json = post(createRequestParam(GET_CARD, param));
//        System.out.println(new Date(1493827200000L));
        Byte b = 5;
        System.out.println(Byte.SIZE);
//        List<Integer> list = new ArrayList<>();
//        list.add(1);
//        list.add(2);
//        list.add(3);
//        list.add(4);
//        list.add(5);
//        list.add(6);
//
//
//        System.out.println(convertOpenWeekday(list));
//        System.out.println(convertOpenWeekday2(list));
//
//        System.out.println(10 ^ 2);
    }

    private static String convertOpenWeekday(List<Integer> openWeekdays) {
        int openWorkdayInt=0;
        for(Integer i : openWeekdays) {
            openWorkdayInt += Math.pow(10, i - 1);
        }
        String openWorkday = String.valueOf(openWorkdayInt);
        for(; openWorkday.length() <= 7;){
            openWorkday = "0" + openWorkday;
        }

        return openWorkday;
    }

    private static String convertOpenWeekday2(List<Integer> openWeekdays) {
        int openWorkdayInt=0;
        String openWorkday;
        //list的数字:1234567代表从星期天到星期六,经过-1作为10的次方放到7位字符串内
        for(Integer weekdayInteger : openWeekdays)
            openWorkdayInt+=Math.pow(10,weekdayInteger-1);
        openWorkday = String.valueOf(openWorkdayInt);
        for( ;openWorkday.length()<7; ){
            openWorkday ="0"+openWorkday;
        }
        return openWorkday;
    }


    public static String post(JSONObject param) {
        System.out.println(param.toJSONString());
        CloseableHttpClient httpclient;
        httpclient = HttpClients.createDefault();



        HttpPost httpPost = new HttpPost(serverUrl);
        System.out.println(serverUrl);

        CloseableHttpResponse response = null;
        String json = null;
        try {
            StringEntity stringEntity = new StringEntity(param.toString(), "utf8");
            httpPost.setEntity(stringEntity);
            response = httpclient.execute(httpPost);

            int status = response.getStatusLine().getStatusCode();

            if (status == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                if (null != entity) {
                    json = EntityUtils.toString(entity, "utf8");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (null != response)
                    response.close();
            } catch (IOException e) {
            }
        }
        return json;
    }

    private static JSONObject createRequestParam(String type, JSONObject command) {

        JSONObject header = new JSONObject();
        header.put("SERVICE_ID", type);

        JSONObject childParam = new JSONObject();
        childParam.put("REQ_MSG_HDR", header);
        childParam.put("REQ_COMM_DATA", command);

        JSONArray array = new JSONArray();
        array.add(childParam);

        JSONObject param = new JSONObject();
        param.put("REQUESTS",array);

        return param;
    }
}
