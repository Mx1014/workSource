package com.everhomes.pusher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.everhomes.msgbox.Message;
import com.everhomes.rest.messaging.ChannelType;
import com.everhomes.user.UserLogin;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.messaging.PusherVender;
import com.everhomes.messaging.PusherVendorData;
import com.everhomes.rest.messaging.DeviceMessage;

public class PusherHuawei implements PusherVender {
    private static final Logger LOGGER = LoggerFactory.getLogger(PusherHuawei.class);
    
    private static  String tokenUrl = "https://login.vmall.com/oauth2/token"; //获取认证Token的URL
    private static  String apiUrl = "https://api.push.hicloud.com/pushsend.do"; //应用级消息下发API
    
    private String appId;
    private String appSecret;
    private String appPkgName;
    private String accessToken;     //下发通知消息的认证Token
    private long tokenExpiredTime = 0;  //accessToken的过期时间
    
    private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    
    public PusherHuawei(String appId, String appSecret, String appPkgName) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.appPkgName = appPkgName;
    }
    
    public String getAppId() {
        return appId;
    }
    
    public String getAppSecret() {
        return appSecret;
    }
    
    public String getAppPkgName() {
        return this.appPkgName;
    }
    
    //获取下发通知消息的认证Token
    private String refreshToken() throws IOException {
        String msgBody = MessageFormat.format(
                "grant_type=client_credentials&client_secret={0}&client_id={1}", 
                URLEncoder.encode(appSecret, "UTF-8"), appId);
        String response = httpPost(tokenUrl, msgBody, 5000, 5000);
        JSONObject obj = JSONObject.parseObject(response);
        if(obj == null) {
            //use the old one
            return accessToken;
        }
        
        Long left = obj.getLong("expires_in") - 5*60*1000;
        String token = obj.getString("access_token");
        
        if (left != null && token != null && left > 0) {
            rwl.writeLock().lock();
            accessToken = token;
            tokenExpiredTime = System.currentTimeMillis() + left;
            rwl.writeLock().unlock();
        }
        
        return token;
    }
    
    //发送Push消息
    @Override
    public void sendPushMessage(String deviceToken, Message msgBox, DeviceMessage devMessage, UserLogin senderLogin, UserLogin destLogin) {
        String token;
        
        rwl.readLock().lock();
        if (tokenExpiredTime <= System.currentTimeMillis()) {
            rwl.readLock().unlock();
            
            //refresh to new token
            try {
                token = refreshToken();    
            } catch(Exception ex) {
                LOGGER.error("Pushing refreshToken failed", ex);
                return;
            }
            
        } else {
            token = accessToken;
            rwl.readLock().unlock();
           }
        
        /*PushManager.requestToken为客户端申请token的方法，可以调用多次以防止申请!token失败*/
        /*PushToken不支持手动编写，需使用客户端的onToken方法获取*/
        JSONArray deviceTokens = new JSONArray();//目标设备Token
        deviceTokens.add(deviceToken);
//        deviceTokens.add("22345678901234561234567890123456");
          
        JSONObject body = new JSONObject();//仅通知栏消息需要设置标题和内容，透传消息key和value为用户自定义
        if(devMessage.getTitle() == null) {
            body.put("title", "zuolin");
        } else {
            body.put("title", devMessage.getTitle());//消息标题    
        }
        
        body.put("content", devMessage.getAlert());//消息内容体
        
        JSONObject param = new JSONObject();
//        param.put("appPkgName", appPkgName);//定义需要打开的appPkgName
        param.put("intent",  String.format("zl-1://message/open-session?dstChannel=%s&dstChannelId=%s&srcChannel=%s&srcChannelId=%s&senderUid=%s#Intent;launchFlags=0x10000000;package=com.everhomes.android.oa.debug;end"
                , ChannelType.USER.getCode()
                , String.valueOf(senderLogin.getUserId())
                , msgBox.getChannelType()
                , msgBox.getChannelToken()
                , String.valueOf(msgBox.getSenderUid()))
                );

        JSONObject action = new JSONObject();
        action.put("type", 1);//类型3为打开APP，其他行为请参考接口文档设置
        action.put("param", param);//消息点击动作参数
        
        JSONObject msg = new JSONObject();
        msg.put("type", 3);//3: 通知栏消息，异步透传消息请根据接口文档设置
        msg.put("action", action);//消息点击动作
        msg.put("body", body);//通知栏消息body内容
        
        JSONObject ext = new JSONObject();//扩展信息，含BI消息统计，特定展示风格，消息折叠。
        ext.put("biTag", "Trump");//设置消息标签，如果带了这个标签，会在回执中推送给CP用于检测某种类型消息的到达率和状态
        if(devMessage.getIcon() != null && devMessage.getIcon().startsWith("http://")) {
            ext.put("icon", devMessage.getIcon());//自定义推送消息在通知栏的图标,value为一个公网可以访问的URL    
        }
        
        JSONObject hps = new JSONObject();//华为PUSH消息总结构体
        hps.put("msg", msg);
        hps.put("ext", ext);
        
        JSONObject payload = new JSONObject();
        payload.put("hps", hps);
        
        String postBody;
        try {
            postBody = MessageFormat.format(
                "access_token={0}&nsp_svc={1}&nsp_ts={2}&device_token_list={3}&payload={4}",
                URLEncoder.encode(token, "UTF-8"),
                URLEncoder.encode("openpush.message.api.send","UTF-8"),
                URLEncoder.encode(String.valueOf(System.currentTimeMillis() / 1000),"UTF-8"),
                URLEncoder.encode(deviceTokens.toString(),"UTF-8"),
                URLEncoder.encode(payload.toString(),"UTF-8"));
            String postUrl = apiUrl + "?nsp_ctx=" + URLEncoder.encode("{\"ver\":\"1\", \"appId\":\"" + appId + "\"}", "UTF-8");
            httpPost(postUrl, postBody, 5000, 5000);
        } catch (IOException ex) {
            LOGGER.error("Pushing post failed", ex);
        }
        

    }
    
    public static String httpPost(String httpUrl, String data, int connectTimeout, int readTimeout) throws IOException {
        OutputStream outPut = null;
        HttpURLConnection urlConnection = null;
        InputStream in = null;
        
        try {
            URL url = new URL(httpUrl);
            urlConnection = (HttpURLConnection)url.openConnection();          
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            urlConnection.setConnectTimeout(connectTimeout);
            urlConnection.setReadTimeout(readTimeout);
            urlConnection.connect();
            
            // POST data
            outPut = urlConnection.getOutputStream();
            outPut.write(data.getBytes("UTF-8"));
            outPut.flush();
            
            // read response
            if (urlConnection.getResponseCode() < 400) {
                in = urlConnection.getInputStream();
                } else {
                    in = urlConnection.getErrorStream();
                    }
            List<String> lines = IOUtils.readLines(in, urlConnection.getContentEncoding());
            StringBuffer strBuf = new StringBuffer();
            for (String line : lines) {
                strBuf.append(line);
                }
            return strBuf.toString();
         } finally {
             IOUtils.closeQuietly(outPut);
             IOUtils.closeQuietly(in);
             if (urlConnection != null) {
                 urlConnection.disconnect();
                 }
             }
        }

    @Override
    public boolean checkAppData(PusherVendorData data) {
        // TODO Auto-generated method stub
        return false;
    }
}
