package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.util.RuntimeErrorException;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * @author sw on 2017/8/21.
 */
public class Test {

    static String url = "http://szdas.iok.la:17508";

    static String GET_EMPTY_PLACE = "/ParkingApi/QueryEmptyPlace";

    public static void main(String[] args) {
//        String json =  Utils.post(url + GET_EMPTY_PLACE, createGeneralParam());
//        System.out.println(json);

        Date date = new Date();
        date.setTime(1503483940813L);
        System.out.println(date.getTime());
    }

    private static Map<String, String> createGeneralParam() {


        Map<String, String> params = new TreeMap<>();
        params.put("TimeStamp", String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)));

        params.put("From", "zl_parking");

        StringBuilder sb = new StringBuilder();
        params.forEach((key, value) -> sb.append(key).append("=").append(value).append("&"));
        String p = sb.substring(0,sb.length() - 1);

        String md5Sign = Utils.md5(p);
        params.put("SignString", md5Sign);

        return params;
    }

}
