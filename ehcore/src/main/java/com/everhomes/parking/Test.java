package com.everhomes.parking;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/7.
 */
public class Test {


    public static void main(String[] args) {
        String url = "http://112.74.77.141:8086/AppApi/";

        JSONObject param = new JSONObject();
        param.put("CompanyID", "175c8e26-ea36-4993-b113-a7320114e370");
        param.put("listPlateNumber", new String[]{"湘B33333"});

        JSONObject q = new JSONObject();
        q.put("data", param);

        Map<String, String> params = new HashMap<>();
        params.put("data", param.toString());

        String result = HttpUtils.post(url + "OISGetAccountCardCarZL", params);

        System.out.println(result);
    }
}
