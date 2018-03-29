package com.everhomes.pmtask;


import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

@Component(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.ZHUZONG)
public class ZhuzongPmTaskHandle extends DefaultPmTaskHandle {
    private static final String GET_ADDRESSES = "/phoneServer/QueryHouseByPhoneNumber";
    private static final  String ACCOUNT_CODE = "sdgj";

    private CloseableHttpClient httpclient = null;
    @PostConstruct
    public void init() {
        httpclient = HttpClients.createDefault();
    }
    @Override
    public Object getThirdAddress(HttpServletRequest req) {
        return super.getThirdAddress(req);
    }

    public static void main(String [] args){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://139.129.204.232:8007" + "LsInterfaceServer/phoneServer/QueryHouseByPhoneNumber");
        CloseableHttpResponse response = null;
        JSONObject object = new JSONObject();
        object.put("AccountCode","sdgj");
        object.put("phone","15650723221");
        try {
            StringEntity stringEntity = new StringEntity(object.toJSONString(), "utf8");
            httpPost.setEntity(stringEntity);
            response = httpclient.execute(httpPost);
            System.out.print(EntityUtils.toString(response.getEntity(), "utf8"));
        }catch (Exception e){

        }
    }
}
