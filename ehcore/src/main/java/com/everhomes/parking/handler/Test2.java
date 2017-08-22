package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;

/**
 * @author sw on 2017/8/21.
 */
public class Test2 {
    public static void main(String[] args) {
        String appId = "1";
        String appkey = "b20887292a374637b4a9d6e9f940b1e6";
        String str = "120170822" + appkey;
        String key = Utils.md5(str);

        JSONObject params = new JSONObject();
        params.put("appId", appId );
        params.put("key", key );
        params.put("parkId", 1 );
        String json = Utils.post("http://220.160.111.114:8099/api/find/GetFreeSpaceNum", params);
        System.out.println(json);


    }
}
