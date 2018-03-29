package com.everhomes.pmtask;


import com.alibaba.fastjson.JSONObject;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.util.RuntimeErrorException;
import org.apache.http.Header;
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
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.ZHUZONG)
public class ZhuzongPmTaskHandle extends DefaultPmTaskHandle {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZhuzongPmTaskHandle.class);

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

    public String postToZhuzong(JSONObject params,String method){
        String url = configProvider.getValue("pmtask.zhuzong.url", "");
        HttpPost httpPost = new HttpPost(url + method);
        CloseableHttpResponse response = null;

        String json = null;
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        params.keySet()
        try {

        }catch (IOException e) {
            LOGGER.error("Pmtask request error, param={}", params.toJSONString(), e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "Pmtask request error.");
        }finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOGGER.error("Pmtask close instream, response error, param={}", params.toJSONString(), e);
                }
            }
        }
    }

    public static void main(String [] args){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://139.129.204.232:8007" + "/LsInterfaceServer/phoneServer/QueryHouseByPhoneNumber");
        CloseableHttpResponse response = null;
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("AccountCode", "sdgj"));
        nameValuePairs.add(new BasicNameValuePair("phone", "15650723221"));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
           // httpPost.setHeader("Content-Type", "application/json");
           // httpPost.setEntity(stringEntity);
            response = httpclient.execute(httpPost);
            System.out.print(EntityUtils.toString(response.getEntity(), "utf8"));
        }catch (Exception e){

        }
    }
}
