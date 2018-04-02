package com.everhomes.pmtask;


import com.alibaba.fastjson.JSONObject;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.pmtask.zhuzong.*;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.RuntimeErrorException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component(PmTaskHandle.PMTASK_PREFIX + PmTaskHandle.ZHUZONG)
public class ZhuzongPmTaskHandle extends DefaultPmTaskHandle {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZhuzongPmTaskHandle.class);
    final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    private static final String GET_ADDRESSES = "/phoneServer/QueryHouseByPhoneNumber";
    private static final String CREATE_TASK = "/phoneServer/ZLAddWorkBill";
    private static final String QUERY_TASKS = "/phoneServer/QueryBillList";
    private static final String GET_TASK_DETAIL = "/phoneServer/QueryBillDetail";
    private static final  String ACCOUNT_CODE = "sdgj";

    private CloseableHttpClient httpclient = null;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    BigCollectionProvider bigCollectionProvider;
    @PostConstruct
    public void init() {
        httpclient = HttpClients.createDefault();
    }
    @Override
    public Object getThirdAddress(HttpServletRequest req) {
        JSONObject params = new JSONObject();
        params.put("AccountCode",ACCOUNT_CODE);
        User user = UserContext.current().getUser();
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
        params.put("phone",userIdentifier.getIdentifierToken());
        String json = postToZhuzong(params,GET_ADDRESSES);
        ZhuzongAddresses addresses = JSONObject.parseObject(json,ZhuzongAddresses.class);
        if (addresses.isSuccess()){
            return addresses.getResult();
        }
        return null;
    }

    @Override
    public Object createThirdTask(HttpServletRequest req) {
        JSONObject params = new JSONObject();
        params.put("AccountCode",ACCOUNT_CODE);
        params.put("clientid",req.getParameter("clientid"));
        params.put("houseid",req.getParameter("houseid"));
        params.put("billtype",req.getParameter("billtype"));
        params.put("content",req.getParameter("content"));
        params.put("isPub",req.getParameter("isPub"));
        String json = postToZhuzong(params,CREATE_TASK);
        ZhuzongCreateTask task = JSONObject.parseObject(json,ZhuzongCreateTask.class);
        if (task.isSuccess()){
            return task.getResult();
        }
        return null;
    }

    @Override
    public Object listThirdTasks(HttpServletRequest req) {
        JSONObject params = new JSONObject();
        params.put("AccountCode",ACCOUNT_CODE);
        params.put("pageOprator",req.getParameter("pageOprator"));
        params.put("currenpage",Integer.parseInt(req.getParameter("currenpage")));
        params.put("clientid",getClientId());
        String json = postToZhuzong(params,QUERY_TASKS);
        ZhuzongTasks tasks = JSONObject.parseObject(json,ZhuzongTasks.class);
        if (tasks.isSuccess()){
            return tasks.getResult();
        }
        return null;
    }

    @Override
    public Object getThirdTaskDetail(HttpServletRequest req) {
        JSONObject params = new JSONObject();
        params.put("AccountCode",ACCOUNT_CODE);
        params.put("bill_id",req.getParameter("bill_id"));
        String json = postToZhuzong(params,GET_TASK_DETAIL);
        ZhuzongTaskDetail taskDetail = JSONObject.parseObject(json,ZhuzongTaskDetail.class);
        if (taskDetail.isSuccess()){
            return taskDetail.getResult();
        }
        return null;
    }

    private String getClientId(){
        User user = UserContext.current().getUser();
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
        Accessor acc = this.bigCollectionProvider.getMapAccessor("zhuzong-ClientId", "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        String clientId = (String)redisTemplate.opsForValue().get(userIdentifier.getIdentifierToken());
        if (clientId == null){
            clientId = "none";
            ZhuzongAddresses task =  (ZhuzongAddresses)getThirdAddress(null);
            if (task!=null && task.getResult().size()>0){
                clientId = task.getResult().get(0).getPk_client();
                redisTemplate.opsForValue().set(userIdentifier.getIdentifierToken(), clientId);
                redisTemplate.expire(userIdentifier.getIdentifierToken(), 2, TimeUnit.DAYS);
            }
        }
        return clientId;
    }

    public String postToZhuzong(JSONObject params, String method) {
        String url = configProvider.getValue("pmtask.zhuzong.url", "");
        HttpPost httpPost = new HttpPost(url + method);
        CloseableHttpResponse response = null;

        String json = null;
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            nameValuePairs.add(new BasicNameValuePair(entry.getKey(), (String) entry.getValue()));
        }

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            response = httpclient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            if(status == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    json = EntityUtils.toString(entity, "utf8");
                }
            }

        } catch (IOException e) {
            LOGGER.error("Pmtask request error, param={}", params.toJSONString(), e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "Pmtask request error.");
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOGGER.error("Pmtask close instream, response error, param={}", params.toJSONString(), e);
                }
            }

        }
        if(LOGGER.isDebugEnabled())
            LOGGER.debug("Data from zhuzong, param={}, json={}", params, json);
        return json;
    }

    public static void main(String [] args){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://139.129.204.232:8007" + "/LsInterfaceServer/phoneServer/QueryHouseByPhoneNumber");
        CloseableHttpResponse response = null;
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("AccountCode", "sdgj"));
        nameValuePairs.add(new BasicNameValuePair("phone", "15650723221"));

        MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();

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
