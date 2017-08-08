package com.everhomes.openapi;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.http.HttpUtils;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ying.xiong on 2017/8/7.
 */
public class ZJGKOpenServiceImpl {

    private final static Logger LOGGER = LoggerFactory.getLogger(ZJGKOpenServiceImpl.class);
    private CloseableHttpClient httpclient = null;

    private String appKey = "ee4c8905-9aa4-4d45-973c-ede4cbb3cf21";
    private String secretKey = "2CQ7dgiGCIfdKyHfHzO772IltqC50e9w7fswbn6JezdEAZU+x4+VHsBE/RKQ5BCkz/irj0Kzg6te6Y9JLgAvbQ==";


    private static final String SYNC_COMMUNITIES = "/openapi/syncCommunities";
    private static final String SYNC_BUILDINGS = "/openapi/syncBuildings";
    private static final String SYNC_APARTMENTS = "/openapi/syncApartments";
    private static final String SYNC_ENTERPRISES = "/openapi/syncEnterprises";
    private static final String SYNC_INDIVIDUALS = "/openapi/syncIndividualCustomer";

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private AppProvider appProvider;


    @Scheduled(cron="0 0 1 * * ?")
    public void syncData() {
        Map<String, String> params = generateParams();
        String communities = postToShenzhou(params, SYNC_COMMUNITIES, null);
        String buildings = postToShenzhou(params, SYNC_BUILDINGS, null);
        String apartments = postToShenzhou(params, SYNC_APARTMENTS, null);
        String enterprises = postToShenzhou(params, SYNC_ENTERPRISES, null);
        String individuals = postToShenzhou(params, SYNC_INDIVIDUALS, null);
    }

    private Map<String, String> generateParams() {
        Map<String, String> params= new HashMap<String,String>();
        params.put("appKey", appKey);
        params.put("timestamp", ""+System.currentTimeMillis());
        params.put("nonce", ""+(long)(Math.random()*100000));
        String signature = SignatureHelper.computeSignature(params, secretKey);
        params.put("signature", signature);

        return params;
    }

    public String postToShenzhou(Map<String, String> params, String method, Map<String, String> headers) {

        String shenzhouUrl = configurationProvider.getValue(999971, "shenzhou.host.url", "");
        HttpPost httpPost = new HttpPost(shenzhouUrl + method);
        CloseableHttpResponse response = null;

        String json = null;
        try {
            StringEntity stringEntity = new StringEntity(params.toString(), "utf8");
            httpPost.setEntity(stringEntity);

            response = httpclient.execute(httpPost);

            int status = response.getStatusLine().getStatusCode();
            if(status == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    json = EntityUtils.toString(entity, "utf8");
                }
            }

        } catch (Exception e) {
            LOGGER.error("sync from shenzhou request error, param={}", params, e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "sync from shenzhou request error.");
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOGGER.error("sync from shenzhou close instream, response error, param={}", params, e);
                }
            }
        }

        if(LOGGER.isDebugEnabled())
            LOGGER.debug("Data from shenzhou, param={}, json={}", params, json);

        return json;
    }
}
